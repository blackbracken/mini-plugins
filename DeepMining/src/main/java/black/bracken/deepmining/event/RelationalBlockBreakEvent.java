package black.bracken.deepmining.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author BlackBracken
 */
public final class RelationalBlockBreakEvent extends BlockBreakEvent {

    private int depth;

    public RelationalBlockBreakEvent(Block broken, Player player, int depth) {
        super(broken, player);

        this.depth = depth;
    }

    public int getDepth() {
        return this.depth;
    }

}
