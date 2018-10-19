package black.bracken.deepmining;

import black.bracken.deepmining.event.RelationalBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * @author BlackBracken
 */
public final class Miner {

    private final DeepMining instance;
    private final Block broken;
    private final Player breaker;

    public Miner(Block broken, Player breaker) {
        this.instance = DeepMining.getInstance();
        this.broken = broken;
        this.breaker = breaker;
    }

    public void mineRelations() {
        mineRelations(broken, breaker, 0);
    }

    private void mineRelations(Block broken, Player breaker, int depth) {
        if (instance.getMaxDepth() <= depth || !instance.getOreMaterialSet().contains(broken.getType())) return;

        RelationalBlockBreakEvent event = new RelationalBlockBreakEvent(broken, breaker, depth);
        instance.getServer().getPluginManager().callEvent(event);

        broken.breakNaturally();

        Bukkit.getScheduler().runTaskLater(
                instance,
                () -> instance.getRelationSet().forEach(relation -> this.mineRelations(broken.getRelative(relation), breaker, depth + 1)),
                instance.getMineDelay()
        );
    }

}
