package black.bracken.paperdoll.listener;

import black.bracken.paperdoll.model.PaperDoll;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public final class PaperDollListener implements Listener {

    private final PaperDoll paperDoll;

    public PaperDollListener(PaperDoll paperDoll) {
        this.paperDoll = paperDoll;
    }

    @EventHandler
    public void onClickPaperDoll(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        World world = player.getWorld();
        Location particleLocation = player.getLocation()
                .clone()
                .add(0.0, 1.3, 0.0);

        if (!Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).contains(event.getAction())) {
            return;
        }

        if (!this.paperDoll.isSimilarTo(itemInMainHand)) {
            return;
        }

        if (world.isThundering() || world.hasStorm()) {
            world.setThundering(false);
            world.setStorm(false);

            itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
            world.spawnParticle(Particle.TOTEM, particleLocation, 12, 1.0, 1.0, 1.0);

            BaseComponent[] textComponents = TextComponent.fromLegacyText(ChatColor.GREEN + "ねぇ、今から晴れるよ");
            world.getPlayers().forEach(playerInWorld -> playerInWorld.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponents));
        }
    }

}
