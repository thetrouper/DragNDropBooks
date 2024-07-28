package com.vexx.dragNDropBooks.Enchants;

import com.vexx.dragNDropBooks.DragNDropBooks;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class Enchanter {

    private Boolean cost_settings_enabled;
    private int player_level_cost_per_enchant_level;
    private double refund_rate;
    private ItemStack item;
    private ItemStack enchantedBook;
    private EnchantmentStorageMeta enchantedBookMeta;
    private ItemMeta itemMeta;


    public Enchanter(DragNDropBooks main, ItemStack enchantedBook, ItemStack item){
        this.cost_settings_enabled = main.getConfig().getBoolean("cost_settings.enabled");
        this.player_level_cost_per_enchant_level = main.getConfig().getInt("cost_settings.player_level_cost_per_enchant_level");
        this.refund_rate = main.getConfig().getDouble("cost_settings.refund_settings.refund_rate");
        this.item = item;
        this.enchantedBook = enchantedBook;
        this.enchantedBookMeta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
        this.itemMeta = item.getItemMeta();

    }

    public Boolean AreValidItemStacks(){
        if(item == null || enchantedBook == null)
            return false;
        if(enchantedBook.getType() != Material.ENCHANTED_BOOK)
            return false;
        if(item.getType().isAir())
            return false;
        if(item.getAmount() != 1 || enchantedBook.getAmount() != 1)
            return false;
        return true;
    }
}
