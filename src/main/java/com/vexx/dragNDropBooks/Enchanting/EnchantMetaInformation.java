package com.vexx.dragNDropBooks.Enchanting;

import java.util.Set;
import org.bukkit.inventory.ItemStack;

public abstract class EnchantMetaInformation {
    private final String name;
    private final Set<EnchantMetaInformation> conflictingEnchants;
    private final int maxLevel;
    private final int cost;

    public EnchantMetaInformation(String name, Set<EnchantMetaInformation> conflictingEnchants, int maxLevel,
      int minLevel, int cost) {
        this.name = name;
        this.conflictingEnchants = conflictingEnchants;
        this.maxLevel = maxLevel;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public Set<EnchantMetaInformation> getConflictingEnchants() {
        return conflictingEnchants;
    }

    public int getMaxLevel() {
        return maxLevel;
    }


    public int getCost() {
        return cost;
    }

    public boolean conflictsWith(EnchantMetaInformation other) {
        return conflictingEnchants.contains(other);
    }

    public abstract boolean canEnchantItem(ItemStack item);

    public abstract void applyEnchantment(ItemStack item, int level);
}