package black.bracken.paperdoll;

import black.bracken.paperdoll.listener.PaperDollListener;
import black.bracken.paperdoll.model.PaperDoll;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperDoll paperDoll = new PaperDoll(this);
        this.getServer().getPluginManager().registerEvents(new PaperDollListener(paperDoll), this);
        this.getServer().addRecipe(paperDoll.getRecipe());
    }

}
