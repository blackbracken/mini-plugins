package black.bracken.oakin;

import black.bracken.oakin.listener.LogBreak;
import black.bracken.oakin.repository.OakinConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public final class Oakin extends JavaPlugin {

    public OakinConfig config;

    @Override
    public void onEnable() {
        this.config = new OakinConfig(this);

        Stream.of(new LogBreak(this))
                .forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    public OakinConfig getOakinConfig() {
        return this.config;
    }

}
