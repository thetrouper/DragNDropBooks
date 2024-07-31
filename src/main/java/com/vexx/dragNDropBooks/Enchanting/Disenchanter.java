package com.vexx.dragNDropBooks.Enchanting;

import com.vexx.dragNDropBooks.Enchanting.Records.ConflictCheckResult;
import com.vexx.dragNDropBooks.Utilities.ConfigManager;
import com.vexx.dragNDropBooks.Utilities.Formatter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.ChatColor;
import java.util.Map;

public class Disenchanter {
    private ConfigManager config;
    private ItemStack enchantedItem;
    private ItemStack book;
    private EnchantmentStorageMeta enchantedBookMeta;
    private Player player;
    private final int validStackSize = 1;

    public Disenchanter(Player player, ItemStack book, ItemStack enchantedItem, ConfigManager config){
        this.enchantedItem = enchantedItem;
        this.book = book;
        this.player = player;
        this.config = config;

        book.setType(Material.ENCHANTED_BOOK);
        if(book.getItemMeta() instanceof EnchantmentStorageMeta){
            this.enchantedBookMeta = (EnchantmentStorageMeta) book.getItemMeta();
        }
    }

    public ItemStack getEnchantedBook() {
        return book;
    }

    public boolean isValidItemStacks(){
        if(enchantedItem == null || book == null) return false;
        if(book.getType() != Material.ENCHANTED_BOOK && book.getType() != Material.BOOK) return false;
        if(enchantedItem.getAmount() != validStackSize || book.getAmount() != validStackSize) return false;
        return !enchantedItem.getEnchantments().isEmpty();
    }

    private boolean isValidEnchantment(Enchantment itemEnchantment, Integer itemPowerLevel){
        if(!isNotConflictingEnchant(itemEnchantment).result()) return false;
        return !enchantedBookMeta.hasEnchant(itemEnchantment) || itemPowerLevel >= enchantedBookMeta.getEnchantLevel(itemEnchantment);
    }

    private int calculateEnchantmentRefund(Enchantment itemEnchantment, Integer itemPowerLevel){
        if(!config.cost_settings_enabled) return 0;
        int enchantmentPowerLevel = 0;
        if(enchantedBookMeta.hasEnchant(itemEnchantment))
            enchantmentPowerLevel += enchantedBookMeta.getEnchantLevel(itemEnchantment);
        return (int) Math.round((itemPowerLevel - enchantmentPowerLevel)
                * config.enchant_costs.getOrDefault(itemEnchantment, 3) * config.refund_rate);
    }

    private ConflictCheckResult isNotConflictingEnchant(Enchantment itemEnchantment){
        Map<Enchantment, Integer> bookEnchantments = enchantedBookMeta.getStoredEnchants();
        for(Map.Entry<Enchantment, Integer> bookEnchantment : bookEnchantments.entrySet()){
            if(bookEnchantment.getKey() == itemEnchantment){ continue;}
            if(bookEnchantment.getKey().conflictsWith(itemEnchantment)){
                return new ConflictCheckResult(false, bookEnchantment.getKey());
            }
        }
        return new ConflictCheckResult(true, null);
    }

    private void applyEnchantmentRefund(Enchantment itemEnchantment, Integer itemPowerLevel){
        player.setLevel(player.getLevel() + calculateEnchantmentRefund(itemEnchantment, itemPowerLevel));
        sendApplyEnchantRefundMessage(itemEnchantment, itemPowerLevel);
    }

    private void sendInvalidEnchantmentMessage(Enchantment itemEnchantment, Integer itemPowerLevel){
        if(enchantedBookMeta.hasEnchant(itemEnchantment) && itemPowerLevel
          <= enchantedBookMeta.getEnchantLevel(itemEnchantment)){
            player.sendMessage(ChatColor.RED + "You already have a higher level of "
            + ChatColor.GOLD + Formatter.getFormattedEnchant(itemEnchantment) + ChatColor.RED
            + " on your book.");
        }

        if(!isNotConflictingEnchant(itemEnchantment).result()) {
             player.sendMessage(ChatColor.RED + "Unable to apply " + ChatColor.GOLD
            + Formatter.getFormattedEnchant(itemEnchantment) + " " + Formatter.toRoman(itemPowerLevel)
            + ChatColor.RED + " to " + ChatColor.GOLD + Formatter.getFormattedItem(enchantedItem)
            + ChatColor.RED + ". "
            + ChatColor.GOLD + Formatter.getFormattedEnchant(itemEnchantment) + " "
            + Formatter.toRoman(itemPowerLevel) + " "
            + ChatColor.RED + "conflicts with " + ChatColor.GOLD
            + Formatter.getFormattedEnchant(isNotConflictingEnchant(itemEnchantment).enchantment())
            + ChatColor.RED + ".");
        }
    }

    private void sendApplyEnchantRefundMessage(Enchantment itemEnchantment, Integer itemPowerLevel){
        if(!config.cost_settings_enabled) return;
        int playerRefund = calculateEnchantmentRefund(itemEnchantment, itemPowerLevel);
        player.sendMessage(ChatColor.GREEN + "Refunded "
            + ChatColor.GOLD + playerRefund
            + ChatColor.GREEN + " levels of experience points for "
            + ChatColor.GOLD + Formatter.getFormattedEnchant(itemEnchantment) + " " + Formatter.toRoman(itemPowerLevel));
    }

    public void RemoveEnchantment() {
        Map<Enchantment, Integer> enchantments = enchantedItem.getEnchantments();
        for(Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()){
            Enchantment itemEnchant = enchant.getKey();
            Integer itemPowerLevel = enchant.getValue();
            if(isValidEnchantment(itemEnchant, itemPowerLevel))
            {
                enchantedBookMeta.addStoredEnchant(itemEnchant, itemPowerLevel, true);
                enchantedItem.removeEnchantment(itemEnchant);
                book.setItemMeta(enchantedBookMeta);
                applyEnchantmentRefund(itemEnchant, itemPowerLevel);
            }
            else
            {
                sendInvalidEnchantmentMessage(itemEnchant, itemPowerLevel);
            }
        }
        if(!enchantedBookMeta.hasStoredEnchants()){
            book.setType(Material.BOOK);
        }

    }
}
