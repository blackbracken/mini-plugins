package black.bracken.airfoil.workers;

import black.bracken.airfoil.BukkitTaskWorker;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class TrailWorker implements BukkitTaskWorker {

    @Override
    public BukkitTask work(Plugin plugin) {
        return Bukkit.getServer().getScheduler().runTaskTimer(plugin,
                () -> Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(Player::isGliding)
                        .forEach(player -> player.getWorld().spawnParticle(
                                Particle.FIREWORKS_SPARK, player.getLocation(), 2, 0.25, 0.25, 0.25, 0)),
                0, 2L);
    }

}
