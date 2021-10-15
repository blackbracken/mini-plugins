package black.bracken.oakin;

import black.bracken.oakin.commands.ToggleCommand;
import black.bracken.oakin.listeners.LogBreak;
import black.bracken.oakin.repository.OakinRestrictor;
import black.bracken.oakin.repository.ToggleStateHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.stream.Stream;

public final class Oakin extends JavaPlugin {

    private OakinRestrictor restrictor;
    private ToggleStateHolder toggleStateHolder; // TODO: implement better :/

    @Override
    public void onEnable() {
        this.restrictor = new OakinRestrictor(this);
        this.toggleStateHolder = new ToggleStateHolder();

        Stream.of(new LogBreak(this))
                .forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));


        Optional.ofNullable(getCommand("oakin")).ifPresent(cmd -> cmd.setExecutor(new ToggleCommand(this)));
    }

    public OakinRestrictor getRestrictor() {
        return this.restrictor;
    }

    public ToggleStateHolder getToggleStateHolder() {
        return this.toggleStateHolder;
    }

}
