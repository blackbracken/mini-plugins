package black.bracken.airfoil;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public interface BukkitTaskWorker {

    BukkitTask work(Plugin plugin);

}
