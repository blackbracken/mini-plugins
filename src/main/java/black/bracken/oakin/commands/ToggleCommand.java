package black.bracken.oakin.commands;

import black.bracken.oakin.Oakin;
import black.bracken.oakin.repository.ToggleStateHolder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ToggleCommand implements CommandExecutor {

    private final Oakin instance;

    public ToggleCommand(Oakin instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be executed by in-game players.");
            return true;
        }

        Player player = (Player) sender;
        ToggleStateHolder toggleStateHolder = instance.getToggleStateHolder();
        boolean enables = !toggleStateHolder.get(player, instance.getRestrictor().getRawConfig().shouldCutDownDefault);
        toggleStateHolder.set(player, enables);

        player.sendMessage(ChatColor.GREEN + "* Oakin: " + (enables ? "enabled" : (ChatColor.RED + "disabled")));

        return true;
    }

}
