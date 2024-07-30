package com.vexx.dragNDropBooks.Utilities;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import java.util.StringJoiner;
import java.util.TreeMap;


public class Formatter {

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }
    public static String getFormattedEnchant(Enchantment bookEnchantment) {
        String unformattedEnchantmentName = bookEnchantment.getKey().getKey().toLowerCase();
        if (unformattedEnchantmentName.contains("_")) {
            String[] nameSplit = unformattedEnchantmentName.split("_");
            StringJoiner formattedName = new StringJoiner(" ");
            for (String s : nameSplit) {
                if (s.equals("of")) {
                    formattedName.add(s);
                } else {
                    formattedName.add(s.substring(0, 1).toUpperCase() + s.substring(1));
                }
            }
            return formattedName.toString();
        }
        return unformattedEnchantmentName.substring(0, 1).toUpperCase() + unformattedEnchantmentName.substring(1);
    }

    public static String getFormattedItem(ItemStack item) {
        String name = item.getType().name().toLowerCase();
        String[] nameSplit = name.split("_");
        StringJoiner formattedName = new StringJoiner(" ");
        for (String s : nameSplit) {
            formattedName.add(s.substring(0, 1).toUpperCase() + s.substring(1));
        }
        return formattedName.toString();
    }
}
