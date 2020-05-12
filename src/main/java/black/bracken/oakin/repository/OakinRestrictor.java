package black.bracken.oakin.repository;

import black.bracken.oakin.Oakin;
import org.bukkit.entity.Player;

public final class OakinRestrictor {

    private final Oakin instance;
    private final OakinConfig config;

    public OakinRestrictor(Oakin instance) {
        this.instance = instance;
        this.config = new OakinConfig(instance);
    }

    public boolean shouldCutDown(Player player) {
        if (config.limitsTools && !config.setOfCutterMaterials.contains(player.getInventory().getItemInMainHand().getType())) {
            return false;
        }

        if (!instance.getToggleStateHolder().get(player, config.shouldCutDownDefault)) {
            return false;
        }

        if (player.isSneaking()) return config.shouldCutDownWhenSneaking;
        if (!player.isSneaking()) return config.shouldCutDownWhenNotSneaking;

        return config.shouldCutDownDefault;
    }

    public int getCuttingInterval() {
        return Math.max(0, config.cuttingInterval);
    }

    public boolean shouldReplantSaplings() {
        return config.shouldReplantSaplings;
    }

    public OakinConfig getRawConfig() {
        return this.config;
    }

}
