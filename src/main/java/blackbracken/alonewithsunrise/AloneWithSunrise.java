package blackbracken.alonewithsunrise;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AloneWithSunrise extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        super.onEnable();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEnterToBend(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        Location particleLocation = event.getBed().getLocation()
                .clone()
                .add(0.0, 1.0, 0.0);
        World world = Objects.requireNonNull(particleLocation.getWorld());

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            player.getWorld().setTime(world.getTime() + 26000 - world.getTime() % 24000);
            world.spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 12, 0.5, 0.5, 0.5);
        }
    }

}
