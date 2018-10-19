package black.bracken.deepmining;

import black.bracken.deepmining.event.RelationalBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author BlackBracken
 */
public final class Miner {

    private static final DeepMining instance;
    private static final int maxDepth;
    private static final long mineDelay;
    private static final Set<BlockFace> relationSet;
    private static final Set<Material> oreMaterialSet;

    static {
        instance = DeepMining.getInstance();
        maxDepth = instance.getMaxDepth();
        mineDelay = instance.getMineDelay();
        relationSet = instance.getRelationSet();
        oreMaterialSet = instance.getOreMaterialSet();
    }

    private final Block broken;
    private final Player breaker;

    public Miner(Block broken, Player breaker) {
        this.broken = broken;
        this.breaker = breaker;
    }

    public void mineRelations() {
        mineRelations(broken, breaker, 0);
    }

    private void mineRelations(Block broken, Player breaker, int depth) {
        if (maxDepth <= depth || !oreMaterialSet.contains(broken.getType())) return;

        RelationalBlockBreakEvent event = new RelationalBlockBreakEvent(broken, breaker, depth);
        instance.getServer().getPluginManager().callEvent(event);

        broken.breakNaturally();

        Bukkit.getScheduler().runTaskLater(
                instance,
                () -> relationSet.forEach(relation -> this.mineRelations(broken.getRelative(relation), breaker, depth + 1)),
                mineDelay
        );
    }

}
