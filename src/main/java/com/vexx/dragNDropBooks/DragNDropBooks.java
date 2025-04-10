package com.vexx.dragNDropBooks;

import com.vexx.dragNDropBooks.Enchanting.Disenchanter;
import com.vexx.dragNDropBooks.Enchanting.Enchanter;
import com.vexx.dragNDropBooks.Utilities.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class DragNDropBooks extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        PluginCommand mainCommand = getCommand("dragndropbooks");
        if (mainCommand != null) mainCommand.setExecutor(new Commands(this));
    }

    @EventHandler
    public void onEnchantedBookUse(InventoryClickEvent e) {
        ItemStack enchantedBook = e.getCursor();
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        if (enchantedBook == null
          || item == null
          || enchantedBook.getType().isAir()
          || item.getType().isAir()
          || enchantedBook.getType() != Material.ENCHANTED_BOOK) return;
        //System.out.println("enchantedBook: " + enchantedBook);
        //System.out.println("item: " + item);
        Enchanter enchanter = new Enchanter(player, enchantedBook, item, new ConfigManager(this));
        if (!enchanter.isValidItemStacks()) return;
        enchanter.applyEnchantment();
        e.setCancelled(true);
        e.setCurrentItem(enchanter.getItem());
    }

    @EventHandler
    public void onEnchantedItemUse(InventoryClickEvent e) {
        ItemStack book = e.getCurrentItem();
        ItemStack enchantedItem = e.getCursor();
        Player player = (Player) e.getWhoClicked();
        if (book == null 
                || enchantedItem == null
                || book.getType().isAir()
                || enchantedItem.getType().isAir()
                || (book.getType() != Material.ENCHANTED_BOOK && book.getType() != Material.BOOK)
                || enchantedItem.getType() == Material.BOOK
                || enchantedItem.getType() == Material.ENCHANTED_BOOK) return;
        //System.out.println("book: " + book);
        //System.out.println("enchantedItem: " + enchantedItem);
        if (!getConfig().getBoolean("disenchant_settings.enabled")) return;
        Disenchanter disenchanter = new Disenchanter(player, book, enchantedItem, new ConfigManager(this));
        if(!disenchanter.isValidItemStacks()) return;
        disenchanter.RemoveEnchantment();
        e.setCancelled(true);
        e.setCurrentItem(disenchanter.getEnchantedBook());
    }
}