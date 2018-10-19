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

    private int maxDepth;
    private long mineDelay;
    private Set<BlockFace> relationSet;
    private Set<Material> oreMaterialSet;

    @Override
    public void onEnable() {
        instance = this;

        // get below params from configuration if needed
        this.maxDepth = 100;
        this.mineDelay = 1L;
        this.relationSet = new HashSet<>(Arrays.asList(
                BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.UP, BlockFace.DOWN
        ));
        this.oreMaterialSet = Arrays.stream(Material.values())
                .filter(material -> material.name().endsWith("_ORE"))
                .collect(Collectors.toSet());

        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static DeepMining getInstance() {
        return instance;
    }

    /* package */ int getMaxDepth() {
        return this.maxDepth;
    }

    /* package */ long getMineDelay() {
        return this.mineDelay;
    }

    /* package */ Set<BlockFace> getRelationSet() {
        return this.relationSet;
    }

    /* package */ Set<Material> getOreMaterialSet() {
        return this.oreMaterialSet;
    }

}
