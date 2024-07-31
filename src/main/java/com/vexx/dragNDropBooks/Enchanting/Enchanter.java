package com.vexx.dragNDropBooks.Enchanting;

import com.vexx.dragNDropBooks.Enchanting.Records.ConflictCheckResult;
import com.vexx.dragNDropBooks.Utilities.ConfigManager;
import com.vexx.dragNDropBooks.Utilities.Formatter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Map;

public class Enchanter {

    private final ConfigManager config;
    private final ItemStack item;
    private final ItemStack enchantedBook;
    private final EnchantmentStorageMeta enchantedBookMeta;
    private final ItemMeta itemMeta;
    private final Player player;

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

    private ConflictCheckResult isNotConflictingEnchant(Enchantment bookEnchantment){
        Map<Enchantment, Integer> itemEnchants = item.getEnchantments();
        for(Map.Entry<Enchantment, Integer> itemEnchantment : itemEnchants.entrySet()){
            if(itemEnchantment.getKey() == bookEnchantment){ continue;}
            if(itemEnchantment.getKey().conflictsWith(bookEnchantment)){
                return new ConflictCheckResult(false, itemEnchantment.getKey());
            }
        }
        return new ConflictCheckResult(true, null);
    }

    private boolean isValidEnchantment(Enchantment bookEnchantment, Integer bookPowerLevel){
        if(!isNotConflictingEnchant(bookEnchantment).result()) return false;
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
        return (bookPowerLevel - itemPowerLevel) * config.enchant_costs.getOrDefault(bookEnchantment, 2);
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

    private void cannotAffordEnchantmentCostMessage(Enchantment bookEnchantment, Integer bookPowerLevel){
        player.sendMessage(ChatColor.GOLD + Formatter.getFormattedEnchant(bookEnchantment) + " "
                + Formatter.toRoman(bookPowerLevel) + ChatColor.RED + " costs " + ChatColor.GOLD +
                + calculateEnchantmentCost(bookEnchantment, bookPowerLevel)
                + ChatColor.RED + " experience levels");
    }

    private void invalidEnchantmentMessage(Enchantment bookEnchantment, Integer bookPowerLevel){
        if(itemMeta.hasEnchant(bookEnchantment) && bookPowerLevel <= itemMeta.getEnchantLevel(bookEnchantment)){
            player.sendMessage(ChatColor.RED + "You already have a higher level of "
                    + ChatColor.GOLD + Formatter.getFormattedEnchant(bookEnchantment) + ChatColor.RED
                    + " on your item.");
        }
        if(!bookEnchantment.canEnchantItem(item)){
            player.sendMessage(ChatColor.RED + "Unable to apply " + ChatColor.GOLD
                    + Formatter.getFormattedEnchant(bookEnchantment) + " " + Formatter.toRoman(bookPowerLevel)
                    + ChatColor.RED + " to " + ChatColor.GOLD + Formatter.getFormattedItem(item) + ".");
        }
        if(!isNotConflictingEnchant(bookEnchantment).result()){
            player.sendMessage(ChatColor.RED + "Unable to apply " + ChatColor.GOLD
                    + Formatter.getFormattedEnchant(bookEnchantment) + " " + Formatter.toRoman(bookPowerLevel)
                    + ChatColor.RED + " to " + ChatColor.GOLD + Formatter.getFormattedItem(item)
                    + ChatColor.RED + ". "
                    + ChatColor.GOLD + Formatter.getFormattedEnchant(bookEnchantment) + " "
                    + Formatter.toRoman(bookPowerLevel) + " "
                    + ChatColor.RED + "conflicts with " + ChatColor.GOLD
                    + Formatter.getFormattedEnchant(isNotConflictingEnchant(bookEnchantment).enchantment())
                    + ChatColor.RED + ".");
        }
    }

    public void applyEnchantment() {
        Map<Enchantment, Integer> enchantments = enchantedBookMeta.getStoredEnchants();
        for(Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()){
            Enchantment bookEnchant = enchant.getKey();
            Integer bookPowerLevel = enchant.getValue();
            if(isValidEnchantment(bookEnchant, bookPowerLevel))
            {
                if(canAffordEnchantmentCost(bookEnchant, bookPowerLevel)){
                    item.addUnsafeEnchantment(bookEnchant, bookPowerLevel);
                    enchantedBookMeta.removeStoredEnchant(bookEnchant);
                    enchantedBook.setItemMeta(enchantedBookMeta);
                    applyEnchantmentCost(bookEnchant, bookPowerLevel);
                }
                else{
                    cannotAffordEnchantmentCostMessage(bookEnchant, bookPowerLevel);
                }
            }
            else {
                invalidEnchantmentMessage(bookEnchant, bookPowerLevel);
            }
        }
        if(!enchantedBookMeta.hasStoredEnchants()){
            enchantedBook.setType(Material.BOOK);
        }

    }

}
