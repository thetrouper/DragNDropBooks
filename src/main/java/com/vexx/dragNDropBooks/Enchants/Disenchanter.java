package com.vexx.dragNDropBooks.Enchants;

import com.vexx.dragNDropBooks.Utilities.ConfigManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class Disenchanter {
    private final ConfigManager config;
    private ItemStack item;
    private ItemStack enchantedBook;
    private EnchantmentStorageMeta enchantedBookMeta;
    private final Player player;
    private final int validStackSize = 1;

    public Disenchanter(Player player, ItemStack enchantedBook, ItemStack item, ConfigManager config){
        this.item = item;
        this.enchantedBook = enchantedBook;
        this.enchantedBookMeta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
        this.player = player;
        this.config = config;
    }

    public ItemStack getEnchantedBook() {
        return enchantedBook;
    }

    public boolean isValidItemStacks(){
        if(item == null || enchantedBook == null) return false;
        if(enchantedBook.getType() != Material.ENCHANTED_BOOK || enchantedBook.getType() != Material.BOOK) return false;
        if(item.getAmount() != validStackSize || enchantedBook.getAmount() != validStackSize) return false;
        return !item.getEnchantments().isEmpty();
    }

    private boolean isValidEnchantment(Enchantment itemEnchantment, Integer itemPowerLevel){
        return !enchantedBookMeta.hasEnchant(itemEnchantment) || itemPowerLevel > enchantedBookMeta.getEnchantLevel(itemEnchantment);
    }

    private int calculateEnchantmentRefund(Enchantment itemEnchantment, Integer itemPowerLevel){
        if(!config.cost_settings_enabled) return 0;
        int enchantmentPowerLevel = 0;
        if(enchantedBookMeta.hasEnchant(itemEnchantment))
            enchantmentPowerLevel += enchantedBookMeta.getEnchantLevel(itemEnchantment);
        return (int) Math.round((itemPowerLevel - enchantmentPowerLevel) * config.player_level_cost_per_enchant_level * config.refund_rate);
    }

    private void applyEnchantmentRefund(Enchantment itemEnchantment, Integer itemPowerLevel){
        player.setLevel(player.getLevel() + calculateEnchantmentRefund(itemEnchantment, itemPowerLevel));
    }

    public void RemoveEnchantment() {
        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        for(Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()){
            Enchantment itemEnchant = enchant.getKey();
            Integer itemPowerLevel = enchant.getValue();
            if(isValidEnchantment(itemEnchant, itemPowerLevel))
            {
                enchantedBookMeta.addStoredEnchant(itemEnchant, itemPowerLevel, true);
                item.removeEnchantment(itemEnchant);
                enchantedBook.setItemMeta(enchantedBookMeta);
                applyEnchantmentRefund(itemEnchant, itemPowerLevel);
            }
        }
        if(enchantedBookMeta.hasStoredEnchants()){
            enchantedBook.setType(Material.ENCHANTED_BOOK);
        }

    }
}
