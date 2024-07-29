package com.vexx.dragNDropBooks.Utilities;

import com.vexx.dragNDropBooks.DragNDropBooks;

public class Cost {

    public static int CalculateEnchantmentCost(int enchantedBookPowerLevel, int itemPowerLevel, int experienceCostPerLevel){
        return (enchantedBookPowerLevel - itemPowerLevel) * experienceCostPerLevel;
    }

    public static double CalculateEnchantmentRefund(int enchantedItemPowerLevel, int playerLevelCostPerEnchantmentLevel, double refundRate){
        System.out.println("enchantedItemPowerLevel: " + enchantedItemPowerLevel);
        System.out.println("playerLevelCostPerEnchantmentLevel: " + playerLevelCostPerEnchantmentLevel);
        System.out.println("refund: " + enchantedItemPowerLevel * refundRate);
        return enchantedItemPowerLevel * refundRate * playerLevelCostPerEnchantmentLevel;
    }
}
