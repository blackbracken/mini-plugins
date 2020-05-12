package black.bracken.oakin.repository;

import org.bukkit.entity.Player;

public final class OakinRule {

    private final OakinConfig config;

    public OakinRule(OakinConfig config) {
        this.config = config;
    }

    public boolean shouldCutDown(Player player) {
        if (config.limitsTools && !config.setOfCutterMaterials.contains(player.getInventory().getItemInMainHand().getType())) {
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

}
