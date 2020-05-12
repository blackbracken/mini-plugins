package black.bracken.oakin;

import black.bracken.oakin.util.XYZTuple;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.*;

public final class OakinCutter {

    private final Map<Integer, Set<Block>> reserveMap;
    private final Set<XYZTuple> visitedSet;
    private final Material logMaterial;

    private final Oakin instance;

    public OakinCutter(Material material) {
        this.reserveMap = new HashMap<>();
        this.visitedSet = new HashSet<>();
        this.logMaterial = material;
        this.instance = Oakin.getInstance();
    }

    public void cutDown(Block begin, Player player) {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            this.searchCircularly(1, begin, 0);
            this.searchVertically(0, begin.getRelative(BlockFace.UP), 1);
            this.searchVertically(0, begin.getRelative(BlockFace.DOWN), -1);

            this.cut();
        });
    }

    private void cut() {
        this.reserveMap.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .forEach(entry -> instance.getServer().getScheduler().runTaskLater(
                        instance,
                        () -> entry.getValue().forEach(this::cutOnce),
                        2 * entry.getKey() // TODO: set from config
                ));
    }

    private void cutOnce(Block block) {
        block.breakNaturally();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;

                Block wouldLeaves = block.getRelative(x, 0, z);
                if (wouldLeaves.getType().getKey().getKey().toUpperCase().endsWith("_LEAVES")) {
                    wouldLeaves.breakNaturally();
                }
            }
        }
    }

    private void searchVertically(int order, Block block, int modY) {
        if (this.shouldNotVisitAt(block)) return;
        this.reserveToCut(order, block);

        this.searchVertically(order + 1, block.getRelative(0, modY, 0), modY);
        this.searchCircularly(order + 1, block, modY);
    }

    private void searchCircularly(int order, Block center, int modY) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x != 0 || z != 0)
                    this.searchRadially(order, center.getRelative(x, 0, z), new XYZTuple(x, modY, z));
            }
        }
    }

    private void searchRadially(int order, Block block, XYZTuple direction) {
        if (this.shouldNotVisitAt(block)) return;
        this.reserveToCut(order, block);

        this.searchVertically(order + 1, block.getRelative(0, direction.y, 0), direction.y);

        switch (Math.abs(direction.x) + Math.abs(direction.z)) {
            case 2:
                this.searchRadially(order + 1, block.getRelative(direction.x, 0, direction.z), direction);
            case 1:
                if (direction.x != 0)
                    this.searchRadially(order + 1, block.getRelative(direction.x, 0, 0), direction);
                if (direction.z != 0)
                    this.searchRadially(order + 1, block.getRelative(0, 0, direction.z), direction);
        }
    }

    private void reserveToCut(int order, Block block) {
        visitedSet.add(XYZTuple.fromLocation(block.getLocation()));

        Set<Block> set = reserveMap.getOrDefault(order, new HashSet<>());
        set.add(block);
        reserveMap.put(order, set);
    }

    private boolean shouldNotVisitAt(Block block) {
        return block.getType() != logMaterial || visitedSet.contains(XYZTuple.fromLocation(block.getLocation()));
    }

}
