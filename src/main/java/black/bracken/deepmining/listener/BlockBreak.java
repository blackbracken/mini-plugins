package black.bracken.deepmining.listener;

import black.bracken.deepmining.Miner;
import black.bracken.deepmining.event.RelationalBlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author BlackBracken
 */
public final class BlockBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event instanceof RelationalBlockBreakEvent) return;

        Miner miner = new Miner(event.getBlock(), event.getPlayer());
        miner.mineRelations();
    }

}
