package com.vexx.dragNDropBooks.Enchants;

import com.vexx.dragNDropBooks.Utilities.ConfigManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
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
        System.out.println("item: " + enchantedItem.getType().toString());
        this.book = book;
        System.out.println("enchantedBook: " + book.getType().toString());
        this.player = player;
        this.config = config;

        book.setType(Material.ENCHANTED_BOOK);
        System.out.println("setting enchantedBook to " + book.getType().toString());
        if(book.getItemMeta() instanceof EnchantmentStorageMeta){
            this.enchantedBookMeta = (EnchantmentStorageMeta) book.getItemMeta();
        }
    }

    public ItemStack getEnchantedBook() {
        return book;
    }

    public boolean isValidItemStacks(){
        if(enchantedItem == null || book == null) return false;
        if(book.getType() != Material.ENCHANTED_BOOK || book.getType() != Material.BOOK) return false;
        if(enchantedItem.getAmount() != validStackSize || book.getAmount() != validStackSize) return false;
        return !enchantedItem.getEnchantments().isEmpty();
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
        }
        if(!enchantedBookMeta.hasStoredEnchants()){
            book.setType(Material.BOOK);
        }

    }
}
