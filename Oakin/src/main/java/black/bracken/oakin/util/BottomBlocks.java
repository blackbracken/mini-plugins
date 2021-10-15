package black.bracken.oakin.util;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class BottomBlocks {
    private List<Block> blocks = new ArrayList<>();
    private int bottomY = 256;

    public void addIfInBottom(Block block) {
        int y = block.getY();

        if (y < bottomY) {
            bottomY = y;
            blocks = new ArrayList<>();
        }

        if (y <= bottomY) {
            blocks.add(block);
        }
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

}