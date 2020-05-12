package black.bracken.oakin.repository;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ToggleStateHolder {
    private final Map<UUID, Boolean> toggleStateMap = new HashMap<>();

    public boolean get(Player player, boolean defaultValue) {
        return toggleStateMap.getOrDefault(player.getUniqueId(), defaultValue);
    }

    public void set(Player player, boolean isEnabled) {
        toggleStateMap.put(player.getUniqueId(), isEnabled);
    }

}
