package black.bracken.oakin.util;

import black.bracken.oakin.util.functional.Maybe;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public final class TreeUtil {

    public static final List<Material> NURSERY_MATERIAL_LIST = Arrays.asList(
            Material.DIRT, Material.GRASS_BLOCK, Material.PODZOL, Material.COARSE_DIRT);

    private TreeUtil() {
    }

    public static boolean isNotLog(Material wouldLog) {
        return !wouldLog.getKey().getKey().endsWith("_log");
    }

    public static Maybe<Material> findLeavesOf(Material logMaterial) {
        return excerptTreeName(logMaterial).map(treeName -> Material.matchMaterial(treeName + "_leaves"));
    }

    public static Maybe<Material> findSaplingOf(Material log) {
        return excerptTreeName(log).map(treeName -> Material.matchMaterial(treeName + "_sapling"));
    }

    private static Maybe<String> excerptTreeName(Material log) {
        if (isNotLog(log)) return Maybe.nothing();

        String treeName = log.getKey().getKey();
        String[] splitted = treeName.split("_");

        return splitted.length != 0
                ? Maybe.just(treeName.substring(0, treeName.length() - splitted[splitted.length - 1].length() - 1))
                : Maybe.nothing();
    }

}
