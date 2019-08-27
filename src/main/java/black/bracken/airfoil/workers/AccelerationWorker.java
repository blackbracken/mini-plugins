package black.bracken.airfoil.workers;

import black.bracken.airfoil.BukkitTaskWorker;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public final class AccelerationWorker implements BukkitTaskWorker {

    private final double speed;
    private final boolean shouldShiftSpeed;

    public AccelerationWorker(double speed, boolean shouldShiftSpeed) {
        this.speed = speed;
        this.shouldShiftSpeed = shouldShiftSpeed;
    }

    @Override
    public BukkitTask work(Plugin plugin) {
        return Bukkit.getServer().getScheduler().runTaskTimer(plugin,
                () -> Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(AccelerationWorker::isClimbing)
                        .forEach(player -> {
                            Vector acceleration = calcAcceleration(player);

                            player.setVelocity(acceleration);
                            player.getWorld().spawnParticle(Particle.CRIT_MAGIC, player.getLocation(), 4, 0.4, 0.4, 0.4, 0);
                        }),
                0, 5L);
    }

    private Vector calcAcceleration(Player player) {
        Vector direction = player.getEyeLocation().getDirection();
        return direction.clone().multiply(speed * (shouldShiftSpeed ? direction.getY() : 1.0));
    }

    private static boolean isClimbing(Player player) {
        return player.isGliding() && 0.25 < player.getEyeLocation().getDirection().getY();
    }

}
