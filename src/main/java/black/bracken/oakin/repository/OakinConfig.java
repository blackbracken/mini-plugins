package black.bracken.oakin.repository;

import black.bracken.oakin.Oakin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class OakinConfig {

    public final boolean shouldCutDownDefault;
    public final boolean shouldCutDownWhenSneaking;
    public final boolean shouldCutDownWhenNotSneaking;
    public final int cuttingInterval;
    public final boolean limitsTools;
    public final Set<Material> setOfCutterMaterials;
    public final boolean shouldReplantSaplings;

    public OakinConfig(Oakin instance) {
        instance.saveDefaultConfig();

        FileConfiguration config = instance.getConfig();
        Logger logger = instance.getLogger();

        this.shouldCutDownDefault = config.getBoolean("Enable.Default", true);
        this.shouldCutDownWhenSneaking = config.getBoolean("Enable.WhenSneaking", false);
        this.shouldCutDownWhenNotSneaking = config.getBoolean("Enable.WhenNotSneaking", false);
        this.cuttingInterval = config.getInt("CuttingInterval", 2);
        this.limitsTools = config.getBoolean("LimitTools", false);

        List<String> cutterIds = config.getStringList("Cutters");
        cutterIds.parallelStream()
                .filter(cutterId -> Material.matchMaterial(cutterId) == null)
                .forEach(unmatchedName -> logger.log(Level.WARNING, "Couldn't find a material name matches " + unmatchedName));
        this.setOfCutterMaterials = cutterIds.parallelStream().map(Material::matchMaterial).collect(Collectors.toSet());

        this.shouldReplantSaplings = config.getBoolean("ReplantSaplings", false);
    }

}
