package black.bracken.oakin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakLog(BlockBreakEvent event) {
        if (event.isCancelled()) return;
    }

}
