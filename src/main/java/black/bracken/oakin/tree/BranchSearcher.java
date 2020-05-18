package black.bracken.oakin.tree;

import black.bracken.oakin.util.BottomBlocks;
import black.bracken.oakin.util.TreeUtil;
import black.bracken.oakin.util.XYZTuple;
import black.bracken.oakin.util.functional.Maybe;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BranchSearcher {

    private final Set<Block> cutLogSet = new HashSet<>();
    private final Set<Block> cutLeavesSet = new HashSet<>();
    private final BottomBlocks bottomBlocks = new BottomBlocks();
    private final Set<XYZTuple> visitedSet = new HashSet<>();

    private final Material logMaterial;
    private final Maybe<Material> leavesMaterial;
    private final int maxDepth;

    private BranchSearcher(Material logMaterial, int maxDepth) {
        this.logMaterial = logMaterial;
        this.leavesMaterial = TreeUtil.findLeavesOf(logMaterial);
        this.maxDepth = maxDepth;
    }

    public static Maybe<BranchSearcher.Result> search(Block begin, Material logMaterial, int maxDepth) {
        BranchSearcher searcher = new BranchSearcher(logMaterial, maxDepth);

        return searcher.trySearch(begin)
                ? Maybe.just(new Result(searcher.cutLogSet, searcher.cutLeavesSet, searcher.bottomBlocks.getBlocks()))
                : Maybe.nothing();
    }

    private boolean trySearch(Block begin) {
        bottomBlocks.addIfInBottom(begin);
        markAsLog(begin);

        return searchAround(1, begin);
    }

    /**
     * @return Returns true if succeed searching all tree logs, otherwise false.
     */
    private boolean searchAround(int depth, Block center) {
        if (depth > maxDepth) return false;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = center.getRelative(x, y, z);
                    if ((x == 0 && y == 0 && z == 0) || hasAlreadyVisited(block)) continue;

                    if (block.getType() == logMaterial) {
                        markAsLog(block);

                        if (!searchAround(depth + 1, block)) {
                            return false;
                        }
                    } else if (block.getType() == leavesMaterial.orElse(null)) {
                        markAsLeaves(block);
                    }
                }
            }
        }

        return true;
    }

    private void markAsLog(Block block) {
        visitedSet.add(XYZTuple.fromLocation(block.getLocation()));
        bottomBlocks.addIfInBottom(block);
        cutLogSet.add(block);
    }

    private void markAsLeaves(Block block) {
        visitedSet.add(XYZTuple.fromLocation(block.getLocation()));
        cutLeavesSet.add(block);
    }

    private boolean hasAlreadyVisited(Block block) {
        return visitedSet.contains(XYZTuple.fromLocation(block.getLocation()));
    }

    public static class Result {
        // omit getters because the fields are readonly.
        public final Set<Block> cutLogSet;
        public final Set<Block> cutLeavesSet;
        public final List<Block> bottomBlockList;

        Result(Set<Block> cutLogSet, Set<Block> cutLeavesSet, List<Block> bottomBlockList) {
            this.cutLogSet = cutLogSet;
            this.cutLeavesSet = cutLeavesSet;
            this.bottomBlockList = bottomBlockList;
        }
    }

}
