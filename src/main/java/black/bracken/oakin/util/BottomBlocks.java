package black.bracken.oakin.util;

import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public final class BottomBlocks {
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