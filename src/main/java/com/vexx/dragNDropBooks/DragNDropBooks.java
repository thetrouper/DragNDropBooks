package com.vexx.dragNDropBooks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEnchantedBookUse(InventoryClickEvent e) {
        if(e.getCursor() == null || e.getCursor().getType() != Material.ENCHANTED_BOOK || e.getCursor() == null || e.getCurrentItem() == null) return;
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        ItemStack enchantedBook = e.getCursor();
        ItemMeta itemMeta = item.getItemMeta();
        if(item == null || item.getType().isAir() || enchantedBook.getType().isAir()) return;
        if(enchantedBook.getAmount() > 1){
            player.sendMessage(ChatColor.RED + "Book stack size must be 1.");
            return;
        }
        EnchantmentStorageMeta bookEnchantmentMetaData = (EnchantmentStorageMeta)enchantedBook.getItemMeta();
        if (!bookEnchantmentMetaData.hasStoredEnchants()) return;
        Map<Enchantment, Integer> bookEnchantments = bookEnchantmentMetaData.getStoredEnchants();
        for(Map.Entry<Enchantment, Integer> entry : bookEnchantments.entrySet()){
            Enchantment proposedEnchantment = entry.getKey();
            Integer proposedEnchantmentPowerLevel = entry.getValue();
            if(proposedEnchantment.canEnchantItem(item)) {
                if(itemMeta.hasEnchant(proposedEnchantment)) {
                    if(proposedEnchantmentPowerLevel > itemMeta.getEnchantLevel(proposedEnchantment)) {
                        item.addUnsafeEnchantment(proposedEnchantment, proposedEnchantmentPowerLevel);
                        bookEnchantmentMetaData.removeStoredEnchant(proposedEnchantment);
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Item's current enchant contains higher or equal power level than enchanted book.");
                        continue;
                    }
                }
                else{
                    item.addUnsafeEnchantment(proposedEnchantment, proposedEnchantmentPowerLevel);
                    bookEnchantmentMetaData.removeStoredEnchant(proposedEnchantment);
                }
            }
            else{
                player.sendMessage(ChatColor.RED + "Cannot apply enchant to this item type.");
                continue;
            }
        }
        if(!bookEnchantmentMetaData.hasStoredEnchants()){
            enchantedBook.setType(Material.BOOK);
        }
        enchantedBook.setItemMeta(bookEnchantmentMetaData);
        e.setCancelled(true);
        e.setCurrentItem(item);
    }

    @EventHandler
    public void onEnchantedItemUse(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack book = e.getCurrentItem();
        ItemStack enchantedItem = e.getCursor();
        ItemMeta enchantedItemMetaData = enchantedItem.getItemMeta();
        if(book.getType().isAir())  return;
        if(book.getType() != Material.BOOK && book.getType() != Material.ENCHANTED_BOOK ) return;
        if(book.getAmount() > 1) return;
        if(!enchantedItemMetaData.hasEnchants()) return;
        if(book.getType() == Material.BOOK) book.setType(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta enchantedBookMetaData = (EnchantmentStorageMeta)book.getItemMeta();
        Map<Enchantment, Integer> itemEnchantments = enchantedItem.getEnchantments();
        for(Map.Entry<Enchantment, Integer> entry : itemEnchantments.entrySet()){
            Enchantment proposedEnchantment = entry.getKey();
            Integer proposedEnchantmentPowerLevel = entry.getValue();
            if(enchantedBookMetaData.hasStoredEnchant(proposedEnchantment))
            {
                if (proposedEnchantmentPowerLevel > enchantedBookMetaData.getStoredEnchantLevel(proposedEnchantment))
                {
                    enchantedBookMetaData.addStoredEnchant(proposedEnchantment, proposedEnchantmentPowerLevel, true);
                    enchantedItem.removeEnchantment(proposedEnchantment);
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "Book's current enchant contains higher or equal power level than enchanted item.");
                    continue;
                }
            }
            else
            {
                enchantedBookMetaData.addStoredEnchant(proposedEnchantment, proposedEnchantmentPowerLevel, true);
                enchantedItem.removeEnchantment(proposedEnchantment);
            }
        }
        e.setCancelled(true);
        book.setItemMeta(enchantedBookMetaData);
        e.setCurrentItem(book);
    }
}