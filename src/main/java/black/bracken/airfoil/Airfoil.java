package black.bracken.airfoil;

import black.bracken.airfoil.workers.AccelerationWorker;
import black.bracken.airfoil.workers.TrailWorker;
import org.bukkit.plugin.java.JavaPlugin;

public final class Airfoil extends JavaPlugin {

    private static final String KEY_ROOT_ACCELERATION = "Acceleration";
    private static final String KEY_ENABLE = KEY_ROOT_ACCELERATION + ".Enable";
    private static final String KEY_SPEED = KEY_ROOT_ACCELERATION + ".Speed";
    private static final String KEY_SHIFTED = KEY_ROOT_ACCELERATION + ".Shifted";

    private static final String KEY_TRAIL = "Trail";

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        if (getConfig().getBoolean(KEY_ENABLE, false)) {
            new AccelerationWorker(
                    getConfig().getDouble(KEY_SPEED, 1.0),
                    getConfig().getBoolean(KEY_SHIFTED, true)
            ).work(this);
        }

        if (getConfig().getBoolean(KEY_TRAIL, true)) {
            new TrailWorker().work(this);
        }
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

}
