package black.bracken.oakin;

import black.bracken.oakin.listener.BlockBreak;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public final class Oakin extends JavaPlugin {

    @Override
    public void onEnable() {
        Stream.of(new BlockBreak())
                .forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

}
