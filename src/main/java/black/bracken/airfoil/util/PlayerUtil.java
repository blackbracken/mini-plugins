package black.bracken.airfoil.util;

import org.bukkit.entity.Player;

public final class PlayerUtil {

    private PlayerUtil() {
    }

    public static boolean isClimbing(Player player) {
        return player.isGliding() && 0.25 < player.getEyeLocation().getDirection().getY();
    }

}
