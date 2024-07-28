package com.vexx.dragNDropBooks;

import com.vexx.dragNDropBooks.Utilities.Cost;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Map;

public final class DragNDropBooks extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        getCommand("dragndropbooks").setExecutor(new Commands(this));
    }

    @EventHandler
    public void onEnchantedBookUse(InventoryClickEvent e) {
        ItemStack enchantedBook = e.getCursor();
        ItemStack item = e.getCurrentItem();

        if(enchantedBook == null ||
                item == null ||
                enchantedBook.getType() != Material.ENCHANTED_BOOK ||
                item.getType().isAir() ||
                enchantedBook.getType().isAir() ||
                enchantedBook.getAmount() > 1) {
            return;
        }

        EnchantmentStorageMeta bookEnchantmentMetaData = (EnchantmentStorageMeta) enchantedBook.getItemMeta();

        if (!bookEnchantmentMetaData.hasStoredEnchants()) return;

        Player player = (Player) e.getWhoClicked();
        Map<Enchantment, Integer> bookEnchantments = bookEnchantmentMetaData.getStoredEnchants();
        ItemMeta itemMeta = item.getItemMeta();

        for(Map.Entry<Enchantment, Integer> entry : bookEnchantments.entrySet()){
            Enchantment proposedEnchantment = entry.getKey();
            Integer proposedEnchantmentPowerLevel = entry.getValue();

            if(itemMeta.hasEnchant(proposedEnchantment) &&
                    proposedEnchantmentPowerLevel <= itemMeta.getEnchantLevel(proposedEnchantment)) {
                player.sendMessage(ChatColor.RED + "Item's current enchant (" + proposedEnchantment.getKey().getKey()
                        + ", Level " + itemMeta.getEnchantLevel(proposedEnchantment)
                        + ") contains higher or equal power level than enchanted book.");
                continue;
            }

            if(!proposedEnchantment.canEnchantItem(item)) {
                player.sendMessage(ChatColor.RED + "Cannot apply enchant ("
                        + proposedEnchantment.getKey().getKey() + ", Level " + proposedEnchantmentPowerLevel
                        + ") to this item type.");
                continue;
            }
            if(getConfig().getBoolean("cost_settings.enabled")) {
                int playerLevelCostPerEnchantmentLevel = getConfig().getInt("cost_settings.player_level_cost_per_enchant_level");
                int itemEnchantLevel = itemMeta.getEnchantLevel(proposedEnchantment);
                int enchantedBookEnchantLevel = proposedEnchantmentPowerLevel;
                int enchantmentCost = Cost.CalculateEnchantmentCost(enchantedBookEnchantLevel, itemEnchantLevel, playerLevelCostPerEnchantmentLevel);
                int playerLevel = player.getLevel();
                if (playerLevel >= enchantmentCost) {
                     player.setLevel(playerLevel - enchantmentCost);
                     item.addUnsafeEnchantment(proposedEnchantment, proposedEnchantmentPowerLevel);
                     bookEnchantmentMetaData.removeStoredEnchant(proposedEnchantment);
                }
                else {
                    player.sendMessage(ChatColor.RED + proposedEnchantment.getKey().getKey() + " requires an enchant level of " + String.valueOf(enchantmentCost));
                }
            }
            else
            {
                item.addUnsafeEnchantment(proposedEnchantment, proposedEnchantmentPowerLevel);
                bookEnchantmentMetaData.removeStoredEnchant(proposedEnchantment);
            }
        }

        if(!bookEnchantmentMetaData.hasStoredEnchants()) {
            enchantedBook.setType(Material.BOOK);
        }

        enchantedBook.setItemMeta(bookEnchantmentMetaData);
        e.setCancelled(true);
        e.setCurrentItem(item);
    }

    @EventHandler
    public void onEnchantedItemUse(InventoryClickEvent e) {
        ItemStack book = e.getCurrentItem();
        ItemStack enchantedItem = e.getCursor();

        if(book == null ||
                enchantedItem == null ||
                book.getType().isAir() ||
                book.getType() != Material.BOOK && book.getType() != Material.ENCHANTED_BOOK ||
                book.getAmount() > 1) {
            return;
        }

        ItemMeta enchantedItemMetaData = enchantedItem.getItemMeta();
        if(enchantedItemMetaData == null ||
                !enchantedItemMetaData.hasEnchants()) {
            return;
        }

        if(book.getType() == Material.BOOK) {
            book.setType(Material.ENCHANTED_BOOK);
        }

        Player player = (Player) e.getWhoClicked();
        Map<Enchantment, Integer> itemEnchantments = enchantedItem.getEnchantments();
        EnchantmentStorageMeta enchantedBookMetaData = (EnchantmentStorageMeta)book.getItemMeta();

        for(Map.Entry<Enchantment, Integer> entry : itemEnchantments.entrySet()){
            Enchantment proposedEnchantment = entry.getKey();
            Integer proposedEnchantmentPowerLevel = entry.getValue();

            if(enchantedBookMetaData.hasStoredEnchant(proposedEnchantment) &&
                    proposedEnchantmentPowerLevel <= enchantedBookMetaData.getStoredEnchantLevel(proposedEnchantment)) {
                player.sendMessage(ChatColor.RED + "Book's current enchant (" + proposedEnchantment.getKey().getKey()
                        + ", Level " + enchantedBookMetaData.getStoredEnchantLevel(proposedEnchantment)
                        + ") contains higher or equal power level than enchanted item.");
                continue;
            }

            enchantedBookMetaData.addStoredEnchant(proposedEnchantment, proposedEnchantmentPowerLevel, true);
            enchantedItem.removeEnchantment(proposedEnchantment);

            if(getConfig().getBoolean("cost_settings.enabled")){
                int playerLevelCostPerEnchantmentLevel = getConfig().getInt("cost_settings.player_level_cost_per_enchant_level");
                double refundRate = getConfig().getDouble("cost_settings.refund_settings.refund_rate");
                int powerLevelDifference = proposedEnchantmentPowerLevel - enchantedBookMetaData.getStoredEnchantLevel(proposedEnchantment);
                int refund = (int) Math.round(Cost.CalculateEnchantmentRefund(powerLevelDifference, playerLevelCostPerEnchantmentLevel, refundRate));
                player.setLevel(player.getLevel() + refund);
                enchantedBookMetaData.addStoredEnchant(proposedEnchantment, proposedEnchantmentPowerLevel, true);
                enchantedItem.removeEnchantment(proposedEnchantment);
            }
            else{
                enchantedBookMetaData.addStoredEnchant(proposedEnchantment, proposedEnchantmentPowerLevel, true);
                enchantedItem.removeEnchantment(proposedEnchantment);
            }
        }

        e.setCancelled(true);
        book.setItemMeta(enchantedBookMetaData);
        e.setCurrentItem(book);
    }
}