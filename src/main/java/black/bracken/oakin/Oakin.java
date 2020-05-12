package black.bracken.oakin;

import black.bracken.oakin.listener.LogBreak;
import black.bracken.oakin.repository.OakinConfig;
import black.bracken.oakin.repository.OakinRule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public final class Oakin extends JavaPlugin {

    private OakinRule rule;

    @Override
    public void onEnable() {
        this.rule = new OakinRule(new OakinConfig(this));

        Stream.of(new LogBreak(this))
                .forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    public OakinRule getRule() {
        return this.rule;
    }

}
