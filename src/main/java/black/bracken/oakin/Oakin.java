package black.bracken.oakin;

import black.bracken.oakin.listener.BlockBreak;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public final class Oakin extends JavaPlugin {

    private static Oakin instance;

    @Override
    public void onEnable() {
        instance = this;

        Stream.of(new BlockBreak())
                .forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Oakin getInstance() {
        return Oakin.instance;
    }

}
