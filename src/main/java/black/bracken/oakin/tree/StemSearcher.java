package black.bracken.oakin.tree;

import black.bracken.oakin.util.BottomBlocks;
import black.bracken.oakin.util.TreeUtil;
import black.bracken.oakin.util.XYZTuple;
import black.bracken.oakin.util.functional.Maybe;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public final class StemSearcher {

    private final Map<Integer, Set<Block>> cutLogMap = new HashMap<>();
    private final Map<Integer, Set<Block>> cutLeavesMap = new HashMap<>();
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

        return new Result(searcher.cutLogMap, searcher.cutLeavesMap, searcher.bottomBlocks.getBlocks());
    }

    private void startAt(Block begin) {
        bottomBlocks.addIfInBottom(begin);
        markAsLog(0, begin);

        searchAround(1, begin);
    }

    private void searchAround(int order, Block center) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = center.getRelative(x, y, z);
                    if ((x == 0 && y == 0 && z == 0) || hasAlreadyVisited(block)) continue;

                    if (block.getType() == logMaterial) {
                        markAsLog(order, block);
                        searchAround(order + 1, block);
                    } else if (block.getType() == leavesMaterial.orElse(null)) {
                        markAsLeaves(order, block);
                    }
                }
            }
        }
    }

    private void markAsLog(int order, Block block) {
        visitedSet.add(XYZTuple.fromLocation(block.getLocation()));
        bottomBlocks.addIfInBottom(block);

        Set<Block> logSet = cutLogMap.getOrDefault(order, new HashSet<>());
        logSet.add(block);
        cutLogMap.put(order, logSet);
    }

    private void markAsLeaves(int order, Block block) {
        visitedSet.add(XYZTuple.fromLocation(block.getLocation()));

        Set<Block> leavesSet = cutLeavesMap.getOrDefault(order, new HashSet<>());
        leavesSet.add(block);
        cutLeavesMap.put(order, leavesSet);
    }

    private boolean hasAlreadyVisited(Block block) {
        return visitedSet.contains(XYZTuple.fromLocation(block.getLocation()));
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
