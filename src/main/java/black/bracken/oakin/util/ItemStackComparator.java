package black.bracken.oakin.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;

// TODO: refactor
public final class ItemStackComparator {

    private ItemStackComparator() {
    }

    public static boolean compare(ItemStack left, ItemStack right, Condition... conditions) {
        if (!DefaultCondition.FUNDAMENTAL.compare(left, right)) return false;

        return Arrays.stream(conditions).allMatch(condition -> condition.compare(left, right));
    }

    @SuppressWarnings("ConstantConditions")
    public enum DefaultCondition implements Condition {
        FUNDAMENTAL((left, right) ->
                left.getType() == right.getType() && Objects.equals(left.getData(), right.getData())
        ),
        TITLE(new ConditionUsesMeta((left, right) ->
                Objects.equals(left.getItemMeta().getDisplayName(), right.getItemMeta().getDisplayName()))
        ),
        LORE(new ConditionUsesMeta((left, right) ->
                Objects.equals(left.getItemMeta().getLore(), right.getItemMeta().getLore()))
        ),
        DURABILITY(new ItemStackComparator.ConditionUsesMeta((left, right) -> {
            if (!(left.getItemMeta() instanceof Damageable)) return !(right.getItemMeta() instanceof Damageable);

            return ((Damageable) left.getItemMeta()).getDamage() == (((Damageable) right.getItemMeta()).getDamage());
        })),
        ENCHANTMENTS(new ConditionUsesMeta((left, right) ->
                Objects.equals(left.getItemMeta().getEnchants(), right.getItemMeta().getEnchants()))
        ),
        ITEM_FLAG(new ConditionUsesMeta((left, right) ->
                Objects.equals(left.getItemMeta().getItemFlags(), right.getItemMeta().getItemFlags()))
        );

        private final Condition condition;

        DefaultCondition(Condition condition) {
            this.condition = condition;
        }

        @Override
        public boolean compare(ItemStack left, ItemStack right) {
            return condition.compare(left, right);
        }

    }

    @FunctionalInterface
    public interface Condition {
        boolean compare(ItemStack left, ItemStack right);
    }

    public static final class ConditionUsesMeta implements Condition {
        private final BiFunction<ItemStack, ItemStack, Boolean> condition;

        public ConditionUsesMeta(BiFunction<ItemStack, ItemStack, Boolean> condition) {
            this.condition = condition;
        }

        @Override
        public boolean compare(ItemStack left, ItemStack right) {
            if (!left.hasItemMeta()) return !right.hasItemMeta();
            if (!right.hasItemMeta()) return !left.hasItemMeta();

            return condition.apply(left, right);
        }

    }

}
