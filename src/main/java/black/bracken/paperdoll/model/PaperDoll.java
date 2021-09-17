package black.bracken.paperdoll.model;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.Objects;

public final class PaperDoll {

    private final NamespacedKey namespacedKey;

    public PaperDoll(Plugin instance) {
        this.namespacedKey = new NamespacedKey(instance, "PaperDoll");
    }

    public ShapedRecipe getRecipe() {
        ShapedRecipe paperDollRecipe = new ShapedRecipe(this.namespacedKey, getItemStack());
        paperDollRecipe.shape(" K ", " P ", " P ");
        paperDollRecipe.setIngredient('K', Material.KELP);
        paperDollRecipe.setIngredient('P', Material.PAPER);
        paperDollRecipe.setIngredient(' ', Material.AIR);

        return paperDollRecipe;
    }

    public ItemStack getItemStack() {
        ItemStack paperDoll = new ItemStack(Material.FEATHER);

        ItemMeta meta = Objects.requireNonNull(paperDoll.getItemMeta());
        meta.setDisplayName(ChatColor.GRAY + "てるてる坊主");
        meta.setLore(Collections.singletonList(ChatColor.AQUA + "右クリックで使用します"));

        paperDoll.setItemMeta(meta);

        return paperDoll;
    }

    public boolean isSimilarTo(ItemStack that) {
        return getItemStack().isSimilar(that);
    }

}
