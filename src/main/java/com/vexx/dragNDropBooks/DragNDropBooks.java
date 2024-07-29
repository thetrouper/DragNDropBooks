package com.vexx.dragNDropBooks;

import com.vexx.dragNDropBooks.Enchants.Disenchanter;
import com.vexx.dragNDropBooks.Enchants.Enchanter;
import com.vexx.dragNDropBooks.Utilities.ConfigManager;
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
        Player player = (Player) e.getWhoClicked();
        if(enchantedBook == null || item == null) return;
        Enchanter enchanter = new Enchanter(player, enchantedBook, item, new ConfigManager(this));
        if(!enchanter.isValidItemStacks()) return;
        enchanter.applyEnchantment();
        e.setCancelled(true);
        e.setCurrentItem(enchanter.getItem());
    }

    @EventHandler
    public void onEnchantedItemUse(InventoryClickEvent e) {
        ItemStack book = e.getCurrentItem();
        ItemStack enchantedItem = e.getCursor();
        Player player = (Player) e.getWhoClicked();
        if(book == null || enchantedItem == null) return;
        if(!(enchantedItem.getItemMeta() instanceof EnchantmentStorageMeta)) return;
        Disenchanter disenchanter = new Disenchanter(player, book, enchantedItem, new ConfigManager(this));
        if(!disenchanter.isValidItemStacks()) return;
        disenchanter.RemoveEnchantment();
        e.setCancelled(true);
        e.setCurrentItem(disenchanter.getEnchantedBook());
    }
}