package black.bracken.oakin.listener;

import black.bracken.oakin.Oakin;
import black.bracken.oakin.repository.OakinConfig;
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

import java.util.Comparator;
import java.util.Map;

public final class LogBreak implements Listener {

    private final Oakin instance;
    private final OakinConfig config;

    public LogBreak(Oakin instance) {
        this.instance = instance;
        this.config = instance.getOakinConfig();
    }

    private static void breakLogOnce(Block block) {
        block.breakNaturally();
        block.getWorld().spawnParticle(Particle.CRIT, block.getLocation().clone().add(0.5, 0.5, 0.5), 4, 0.2, 0.2, 0.2, 0);
    }

    private static void breakLeavesOnce(Block block) {
        block.breakNaturally();
    }

    private static void plantSapling(Block block, Material sapling) {
        block.setType(sapling);
        block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation().clone().add(0.5, 0.5, 0.5), 7, 0.4, 0.4, 0.4, 0);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakLog(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material logMaterial = event.getBlock().getType();

        if (TreeUtil.isNotLog(logMaterial)) return;
        if (!config.shouldCut(player)) return;

        BukkitScheduler scheduler = instance.getServer().getScheduler();
        scheduler.runTaskAsynchronously(instance, () -> {
            StemSearcher.Result result = StemSearcher.search(block, logMaterial);

            result.cutLogMap.entrySet().stream()
                    .sorted(Comparator.comparingInt(Map.Entry::getKey))
                    .forEach(entry -> scheduler.runTaskLater(
                            instance,
                            () -> entry.getValue().forEach(LogBreak::breakLogOnce),
                            2 * entry.getKey() // TODO: set interval in config
                    ));
            result.cutLeaveMap.entrySet().stream()
                    .sorted(Comparator.comparingInt(Map.Entry::getKey))
                    .forEach(entry -> scheduler.runTaskLater(
                            instance,
                            () -> entry.getValue().forEach(LogBreak::breakLeavesOnce),
                            2 * entry.getKey()
                    ));
            result.bottomBlockSet.stream()
                    .filter(bottom -> TreeUtil.NURSERY_MATERIAL_LIST.contains(bottom.getRelative(BlockFace.DOWN).getType()))
                    .forEach(bottom -> scheduler.runTaskLater(
                            instance,
                            () -> TreeUtil.findSaplingOf(logMaterial).ifPresent(sapling -> plantSapling(bottom, sapling)),
                            2 * (result.cutLogMap.keySet().size() + 1)
                    ));
        });
    }

}
