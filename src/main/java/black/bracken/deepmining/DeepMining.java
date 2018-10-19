package black.bracken.deepmining;

import black.bracken.deepmining.listener.BlockBreak;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author BlackBracken
 */
public final class DeepMining extends JavaPlugin {

    private static DeepMining instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static DeepMining getInstance() {
        return instance;
    }

    // get below params from configuration if needed

    /* package */ int getMaxDepth() {
        return 100;
    }

    /* package */ long getMineDelay() {
        return 1L;
    }

    /* package */ Set<BlockFace> getRelationSet() {
        return new HashSet<>(Arrays.asList(
                BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.UP, BlockFace.DOWN
        ));
    }

    /* package */ Set<Material> getOreMaterialSet() {
        return Arrays.stream(Material.values())
                .filter(material -> material.name().endsWith("_ORE"))
                .collect(Collectors.toSet());
    }

}
