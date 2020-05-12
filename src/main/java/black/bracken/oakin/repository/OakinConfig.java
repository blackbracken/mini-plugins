package black.bracken.oakin.repository;

import black.bracken.oakin.Oakin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class OakinConfig {

    private final FileConfiguration config;
    private final Logger logger;

    public OakinConfig(Oakin instance) {
        instance.saveDefaultConfig();

        this.config = instance.getConfig();
        this.logger = instance.getLogger();
    }

    public boolean shouldCut(Player player) {
        if (shouldLimitTools()) {
            if (!getSetOfCutterMaterials().contains(player.getInventory().getItemInMainHand().getType())) {
                return false;
            }
        }

        if (forcesDefault()) return shouldCutDefault();

        if (player.isSneaking() && shouldCutWhenSneaking()) return true;
        if (!player.isSneaking() && shouldCutWhenNotSneaking()) return true;

        return shouldCutDefault();
    }

    public boolean shouldCutDefault() {
        return this.config.getBoolean("Enable.Default", true);
    }

    public boolean forcesDefault() {
        return this.config.getBoolean("Enable.ForceDefault", false);
    }

    public boolean shouldCutWhenSneaking() {
        return this.config.getBoolean("Enable.WhenSneaking", false);
    }

    public boolean shouldCutWhenNotSneaking() {
        return this.config.getBoolean("Enable.WhenNotSneaking", false);
    }

    public boolean shouldLimitTools() {
        return this.config.getBoolean("LimitTools", false);
    }

    public Set<Material> getSetOfCutterMaterials() {
        List<String> cutterIds = this.config.getStringList("Cutters");

        cutterIds.parallelStream()
                .filter(cutterId -> Material.matchMaterial(cutterId) == null)
                .forEach(unmatchedName ->
                        logger.log(Level.WARNING, "Couldn't find a material name matches " + unmatchedName));

        return cutterIds.parallelStream().map(Material::matchMaterial).collect(Collectors.toSet());
    }

    public boolean shouldReplantSaplings() {
        return this.config.getBoolean("ReplantSaplings", false);
    }

}
