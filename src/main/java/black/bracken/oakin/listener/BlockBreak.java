package black.bracken.oakin.listener;

import black.bracken.oakin.OakinCutter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakLog(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        if (!isLog(block.getType())) return;

        OakinCutter cutter = new OakinCutter(block.getType());
        cutter.cutDown(event.getBlock(), event.getPlayer());
    }

    // TODO: move to right place
    private static boolean isLog(Material material) {
        // TODO: set from config
        return material.getKey().getKey().toUpperCase().endsWith("_LOG");
    }

}
