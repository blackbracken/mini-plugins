package black.bracken.oakin;

import black.bracken.oakin.util.XYZTuple;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.*;

public final class OakinCutter {

    private static final List<Material> nurseryMaterialList = Arrays.asList(Material.DIRT, Material.GRASS_BLOCK, Material.PODZOL);

    private final Map<Integer, Set<Block>> reserveMap;
    private final BottomBlocks bottomBlocks;
    private final Set<XYZTuple> visitedSet;
    private final Material logMaterial;
    private final Oakin instance;

    public OakinCutter(Material material, Oakin instance) {
        this.logMaterial = material;
        this.instance = instance;

        this.reserveMap = new HashMap<>();
        this.bottomBlocks = new BottomBlocks();
        this.visitedSet = new HashSet<>();
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

        bottomBlocks.getBlocks().forEach(block -> System.out.println("BOTTOM: " + block.getY()));
        this.instance.getServer().getScheduler().runTaskLater(
                instance,
                () -> bottomBlocks.getBlocks().stream()
                        .peek(block -> System.out.println("block: " + block.getType() + " / under: " + block.getRelative(BlockFace.DOWN).getType()))
                        .filter(block -> nurseryMaterialList.contains(block.getRelative(BlockFace.DOWN).getType()))
                        .forEach(onDirt -> findSaplingOf(logMaterial).ifPresent(onDirt::setType)),
                2 * reserveMap.keySet().size()
        );
    }

    private void cutOnce(Block block) {
        block.breakNaturally();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;

                Block wouldLeaves = block.getRelative(x, 0, z);
                if (isLeaves(wouldLeaves)) {
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
                if (x == 0 && z == 0) continue;

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
        bottomBlocks.addIfInBottom(block);

        Set<Block> set = reserveMap.getOrDefault(order, new HashSet<>());
        set.add(block);
        reserveMap.put(order, set);
    }

    private boolean shouldNotVisitAt(Block block) {
        return block.getType() != logMaterial || visitedSet.contains(XYZTuple.fromLocation(block.getLocation()));
    }

    private static Optional<Material> findSaplingOf(Material material) {
        String name = material.getKey().getKey();
        String[] split = name.split("_");

        if (split.length == 0) {
            return Optional.empty();
        } else {
            String saplingName = name.substring(0, name.length() - split[split.length - 1].length()) + "sapling";
            return Optional.ofNullable(Material.matchMaterial(saplingName));
        }
    }

    private static boolean isLeaves(Block block) {
        return block.getType().getKey().getKey().endsWith("_leaves");
    }

    private static class BottomBlocks {
        private Set<Block> blocks = new HashSet<>();
        private int bottomY = 256;

        public void addIfInBottom(Block block) {
            int y = block.getY();

            if (y < bottomY) {
                bottomY = y;
                blocks = new HashSet<>();
            }

            if (y <= bottomY) {
                blocks.add(block);
            }
        }

        public Set<Block> getBlocks() {
            return this.blocks;
        }
    }

}
