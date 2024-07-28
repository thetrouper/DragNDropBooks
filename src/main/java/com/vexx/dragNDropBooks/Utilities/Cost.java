package com.vexx.dragNDropBooks.Utilities;

import com.vexx.dragNDropBooks.DragNDropBooks;

public class Cost {


    public static int CalculateEnchantmentCost(int enchantedBookPowerLevel, int itemPowerLevel, int experienceCostPerLevel){
        System.out.println("Set experienceCostPerLevel to " + experienceCostPerLevel);
        return (enchantedBookPowerLevel - itemPowerLevel) * experienceCostPerLevel;
    }

    public static double CalculateEnchantmentRefund(int enchantedItemPowerLevel, double refundRate){
        System.out.println("Set refund rate to " + refundRate);
        return enchantedItemPowerLevel * refundRate;
    }
}
