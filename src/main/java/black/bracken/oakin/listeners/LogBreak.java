package black.bracken.oakin.listeners;

import black.bracken.oakin.Oakin;
import black.bracken.oakin.repository.OakinRestrictor;
import black.bracken.oakin.tree.StemSearcher;
import black.bracken.oakin.util.TreeUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public final class LogBreak implements Listener {

    private static final int MIN_LEAVES_TO_REPLANT = 3;
    private static final int MAX_AMOUNT_OF_SAPLINGS_REPLANTED = 4;

    private final Oakin instance;

    public LogBreak(Oakin instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakLog(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Block beginBlock = event.getBlock();
        Material logMaterial = event.getBlock().getType();
        OakinRestrictor restrictor = instance.getRestrictor();

        if (TreeUtil.isNotLog(logMaterial) || !restrictor.shouldCutDown(player)) return;

        BukkitScheduler scheduler = instance.getServer().getScheduler();
        scheduler.runTaskAsynchronously(instance, () -> {
            StemSearcher.Result result = StemSearcher.search(beginBlock, logMaterial, restrictor.getRawConfig().recursionLimit).orElse(null);
            if (result == null) return;

            Map<Integer, Set<Block>> logMapGroupedByDistance = new HashMap<>();
            result.cutLogSet.forEach(log -> {
                int distance = Math.round((float) log.getLocation().distance(beginBlock.getLocation()));

                Set<Block> logSet = logMapGroupedByDistance.getOrDefault(distance, new HashSet<>());
                logSet.add(log);
                logMapGroupedByDistance.put(distance, logSet);
            });

            Map<Integer, Set<Block>> leavesMapGroupedByDistance = new HashMap<>();
            result.cutLeavesSet.forEach(leaves -> {
                int distance = Math.round((float) leaves.getLocation().distance(beginBlock.getLocation()));

                Set<Block> leavesSet = leavesMapGroupedByDistance.getOrDefault(distance, new HashSet<>());
                leavesSet.add(leaves);
                leavesMapGroupedByDistance.put(distance, leavesSet);
            });

            logMapGroupedByDistance.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey))
                    .forEach(entry -> scheduler.runTaskLater(
                            instance,
                            () -> entry.getValue().forEach(this::breakLogOnce),
                            restrictor.getCuttingInterval() * entry.getKey())
                    );

            leavesMapGroupedByDistance.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey))
                    .forEach(entry -> scheduler.runTaskLater(
                            instance,
                            () -> entry.getValue().forEach(this::breakLeavesOnce),
                            restrictor.getCuttingInterval() * entry.getKey())
                    );

            if (restrictor.shouldReplantSaplings() && result.cutLeavesSet.size() >= MIN_LEAVES_TO_REPLANT) {
                result.bottomBlockList.stream()
                        .filter(bottom -> TreeUtil.NURSERY_MATERIAL_LIST.contains(bottom.getRelative(BlockFace.DOWN).getType()))
                        .limit(MAX_AMOUNT_OF_SAPLINGS_REPLANTED)
                        .forEach(bottom -> scheduler.runTaskLater(
                                instance,
                                () -> TreeUtil.findSaplingOf(logMaterial).ifPresent(sapling -> plantSapling(bottom, sapling)),
                                restrictor.getCuttingInterval() * (Collections.max(leavesMapGroupedByDistance.keySet()) + 1)
                        ));
            }
        });
    }

    private void breakLogOnce(Block block) {
        block.breakNaturally();
        block.getWorld().spawnParticle(Particle.CRIT, block.getLocation().clone().add(0.5, 0.5, 0.5), 4, 0.2, 0.2, 0.2, 0);
    }

    private void breakLeavesOnce(Block block) {
        block.breakNaturally();
    }

    private void plantSapling(Block block, Material sapling) {
        block.setType(sapling);
        block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation().clone().add(0.5, 0.5, 0.5), 7, 0.4, 0.4, 0.4, 0);
    }

}
