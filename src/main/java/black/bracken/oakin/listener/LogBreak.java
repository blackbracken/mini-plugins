package black.bracken.oakin.listener;

import black.bracken.oakin.Oakin;
import black.bracken.oakin.OakinCutter;
import black.bracken.oakin.repository.OakinConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class LogBreak implements Listener {

    private final Oakin instance;
    private final OakinConfig config;

    public LogBreak(Oakin instance) {
        this.instance = instance;
        this.config = instance.getOakinConfig();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakLog(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        if (!isLog(block.getType())) return;

        Player player = event.getPlayer();
        if (!config.shouldCut(player)) return;

        new OakinCutter(block.getType(), instance).cutDown(block, player);
    }

    private static boolean isLog(Material material) {
        return material.getKey().getKey().endsWith("_log");
    }

}
