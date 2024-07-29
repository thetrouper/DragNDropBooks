package com.vexx.dragNDropBooks.Enchants;

import com.vexx.dragNDropBooks.Utilities.ConfigManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Map;

public class Enchanter {

    private ConfigManager config;
    private ItemStack item;
    private ItemStack enchantedBook;
    private EnchantmentStorageMeta enchantedBookMeta;
    private ItemMeta itemMeta;
    private Player player;

    public Enchanter(Player player, ItemStack enchantedBook, ItemStack item, ConfigManager config){
        this.item = item;
        this.enchantedBook = enchantedBook;
        this.enchantedBookMeta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
        this.itemMeta = item.getItemMeta();
        this.player = player;
        this.config = config;
    }
    public ItemStack getItem(){
        return item;
    }

    public boolean isValidItemStacks(){
        if(item == null || enchantedBook == null) return false;
        if(enchantedBook.getType() != Material.ENCHANTED_BOOK) return false;
        if(item.getAmount() != 1 || enchantedBook.getAmount() != 1) return false;
        return enchantedBookMeta.hasStoredEnchants();
    }

    private boolean isValidEnchantment(Enchantment bookEnchantment, Integer bookPowerLevel){
        if(itemMeta.hasEnchant(bookEnchantment) && bookPowerLevel <= itemMeta.getEnchantLevel(bookEnchantment))
            return false;
        return bookEnchantment.canEnchantItem(item);
    }

    private int calculateEnchantmentCost(Enchantment bookEnchantment, Integer bookPowerLevel){
        if(!config.cost_settings_enabled) return 0;
        int itemPowerLevel = 0;
        int playerLevel = player.getLevel();
        if(itemMeta.hasEnchant(bookEnchantment))
            itemPowerLevel += itemMeta.getEnchantLevel(bookEnchantment);
        return (bookPowerLevel - itemPowerLevel) * config.player_level_cost_per_enchant_level;
    }

    private boolean canAffordEnchantmentCost(Enchantment bookEnchantment, Integer bookPowerLevel){
        int enchantmentCost = calculateEnchantmentCost(bookEnchantment, bookPowerLevel);
        int playerLevel = player.getLevel();
        return playerLevel >= enchantmentCost;
    }

    private void applyEnchantmentCost(Enchantment bookEnchantment, Integer bookPowerLevel){
        int playerLevel = player.getLevel();
        player.setLevel(playerLevel - calculateEnchantmentCost(bookEnchantment, bookPowerLevel));
    }

    public void applyEnchantment() {
        Map<Enchantment, Integer> enchantments = enchantedBookMeta.getStoredEnchants();
        for(Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()){
            Enchantment bookEnchant = enchant.getKey();
            Integer bookPowerLevel = enchant.getValue();
            if(isValidEnchantment(bookEnchant, bookPowerLevel) &&
            canAffordEnchantmentCost(bookEnchant, bookPowerLevel))
            {
                item.addUnsafeEnchantment(bookEnchant, bookPowerLevel);
                enchantedBookMeta.removeStoredEnchant(bookEnchant);
                enchantedBook.setItemMeta(enchantedBookMeta);
                applyEnchantmentCost(bookEnchant, bookPowerLevel);
            }
        }
        if(!enchantedBookMeta.hasStoredEnchants()){
            enchantedBook.setType(Material.BOOK);
        }

    }

}
