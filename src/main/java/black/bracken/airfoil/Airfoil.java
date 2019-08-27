package black.bracken.airfoil;

import black.bracken.airfoil.workers.AccelerationWorker;
import org.bukkit.plugin.java.JavaPlugin;

public final class Airfoil extends JavaPlugin {

    private static final String KEY_ROOT = "Acceleration";
    private static final String KEY_ENABLE = KEY_ROOT + ".Enable";
    private static final String KEY_SPEED = KEY_ROOT + ".Speed";
    private static final String KEY_SHIFTED = KEY_ROOT + ".Shifted";

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        if (getConfig().getBoolean(KEY_ENABLE, false)) {
            new AccelerationWorker(
                    getConfig().getDouble(KEY_SPEED, 1.0),
                    getConfig().getBoolean(KEY_SHIFTED, true)
            ).work(this);
        }
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

}
