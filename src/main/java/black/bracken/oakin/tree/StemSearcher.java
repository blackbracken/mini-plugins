package black.bracken.oakin.tree;

import black.bracken.oakin.util.BottomBlocks;
import black.bracken.oakin.util.TreeUtil;
import black.bracken.oakin.util.XYZTuple;
import black.bracken.oakin.util.functional.Maybe;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public final class StemSearcher {

    private final Map<Integer, Set<Block>> cutBlockMap = new HashMap<>();
    private final Map<Integer, Set<Block>> cutLeaveMap = new HashMap<>();
    private final BottomBlocks bottomBlocks = new BottomBlocks();
    private final Set<XYZTuple> visitedSet = new HashSet<>();

    private final Material logMaterial;
    private final Maybe<Material> leavesMaterial;

    private StemSearcher(Material logMaterial) {
        this.logMaterial = logMaterial;
        this.leavesMaterial = TreeUtil.findLeavesOf(logMaterial);
    }

    public static StemSearcher.Result search(Block begin, Material logMaterial) {
        StemSearcher searcher = new StemSearcher(logMaterial);
        searcher.startAt(begin);

        return new Result(searcher.cutBlockMap, searcher.cutLeaveMap, searcher.bottomBlocks.getBlocks());
    }

    private void startAt(Block begin) {
        bottomBlocks.addIfInBottom(begin);

        searchCircularly(1, begin, 0);
        searchVertically(0, begin.getRelative(BlockFace.UP), 1);
        searchVertically(0, begin.getRelative(BlockFace.DOWN), -1);
    }

    private void searchVertically(int order, Block block, int modY) {
        if (shouldNotVisitAt(block)) return;
        visit(block, order);

        searchVertically(order + 1, block.getRelative(0, modY, 0), modY);
        searchCircularly(order + 1, block, modY);
    }

    private void searchCircularly(int order, Block center, int modY) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;

                searchRadially(order, center.getRelative(x, 0, z), new XYZTuple(x, modY, z));
            }
        }
    }

    private void searchRadially(int order, Block block, XYZTuple direction) {
        if (shouldNotVisitAt(block)) return;
        visit(block, order);

        searchVertically(order + 1, block.getRelative(0, direction.y, 0), direction.y);

        switch (Math.abs(direction.x) + Math.abs(direction.z)) {
            case 2:
                searchRadially(order + 1, block.getRelative(direction.x, 0, direction.z), direction);
            case 1:
                if (direction.x != 0)
                    searchRadially(order + 1, block.getRelative(direction.x, 0, 0), direction);
                if (direction.z != 0)
                    searchRadially(order + 1, block.getRelative(0, 0, direction.z), direction);
        }
    }

    private void visit(Block block, int order) {
        if (shouldNotVisitAt(block)) return;

        visitedSet.add(XYZTuple.fromLocation(block.getLocation()));
        bottomBlocks.addIfInBottom(block);

        Set<Block> set = cutBlockMap.getOrDefault(order, new HashSet<>());
        set.add(block);
        cutBlockMap.put(order, set);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    Maybe.just(block.getRelative(x, y, z))
                            .takeIf(wouldLeaves -> wouldLeaves.getType() == leavesMaterial.orElse(null))
                            .ifPresent(leaves -> {
                                Set<Block> leavesSet = cutLeaveMap.getOrDefault(order, new HashSet<>());
                                leavesSet.add(leaves);
                                cutLeaveMap.put(order, leavesSet);
                            });
                }
            }
        }
    }

    private boolean shouldNotVisitAt(Block block) {
        return block.getType() != logMaterial || visitedSet.contains(XYZTuple.fromLocation(block.getLocation()));
    }

    public static class Result {
        // omit getters because the fields are readonly.
        public final Map<Integer, Set<Block>> cutLogMap;
        public final Map<Integer, Set<Block>> cutLeaveMap;
        public final List<Block> bottomBlockList;

        Result(Map<Integer, Set<Block>> cutLogMap, Map<Integer, Set<Block>> cutLeaveMap, List<Block> bottomBlockList) {
            this.cutLogMap = cutLogMap;
            this.cutLeaveMap = cutLeaveMap;
            this.bottomBlockList = bottomBlockList;
        }
    }

}
