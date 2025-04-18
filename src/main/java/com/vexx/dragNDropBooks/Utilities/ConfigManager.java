package com.vexx.dragNDropBooks.Utilities;

import com.vexx.dragNDropBooks.DragNDropBooks;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    public boolean cost_settings_enabled ;
    public double refund_rate;
    public final  Map<Enchantment, Integer> enchant_costs = new HashMap<Enchantment, Integer>();

    public ConfigManager(DragNDropBooks main){
        this.cost_settings_enabled = main.getConfig().getBoolean("cost_settings.enabled");
        this.refund_rate = main.getConfig().getDouble("refund_settings.refund_rate");

        enchant_costs.put(Enchantment.PROTECTION,
            main.getConfig().getInt("default_enchant_costs.protection"));
        enchant_costs.put(Enchantment.FIRE_PROTECTION,
            main.getConfig().getInt("default_enchant_costs.fire_protection"));
        enchant_costs.put(Enchantment.FEATHER_FALLING,
            main.getConfig().getInt("default_enchant_costs.feather_falling"));
        enchant_costs.put(Enchantment.BLAST_PROTECTION,
            main.getConfig().getInt("default_enchant_costs.blast_protection"));
        enchant_costs.put(Enchantment.PROJECTILE_PROTECTION,
            main.getConfig().getInt("default_enchant_costs.projectile_protection"));
        enchant_costs.put(Enchantment.THORNS,
            main.getConfig().getInt("default_enchant_costs.thorns"));
        enchant_costs.put(Enchantment.RESPIRATION,
            main.getConfig().getInt("default_enchant_costs.respiration"));
        enchant_costs.put(Enchantment.DEPTH_STRIDER,
            main.getConfig().getInt("default_enchant_costs.depth_strider"));
        enchant_costs.put(Enchantment.AQUA_AFFINITY,
            main.getConfig().getInt("default_enchant_costs.aqua_affinity"));
        enchant_costs.put(Enchantment.SHARPNESS,
            main.getConfig().getInt("default_enchant_costs.sharpness"));
        enchant_costs.put(Enchantment.SMITE,
            main.getConfig().getInt("default_enchant_costs.smite"));
        enchant_costs.put(Enchantment.BANE_OF_ARTHROPODS,
            main.getConfig().getInt("default_enchant_costs.bane_of_arthropods"));
        enchant_costs.put(Enchantment.KNOCKBACK,
            main.getConfig().getInt("default_enchant_costs.knock_back"));
        enchant_costs.put(Enchantment.FIRE_ASPECT,
            main.getConfig().getInt("default_enchant_costs.fire_aspect"));
        enchant_costs.put(Enchantment.LOOTING,
            main.getConfig().getInt("default_enchant_costs.looting"));
        enchant_costs.put(Enchantment.EFFICIENCY,
            main.getConfig().getInt("default_enchant_costs.efficiency"));
        enchant_costs.put(Enchantment.SILK_TOUCH,
            main.getConfig().getInt("default_enchant_costs.silk_touch"));
        enchant_costs.put(Enchantment.UNBREAKING,
            main.getConfig().getInt("default_enchant_costs.unbreaking"));
        enchant_costs.put(Enchantment.FORTUNE,
            main.getConfig().getInt("default_enchant_costs.fortune"));
        enchant_costs.put(Enchantment.POWER,
            main.getConfig().getInt("default_enchant_costs.power"));
        enchant_costs.put(Enchantment.PUNCH,
            main.getConfig().getInt("default_enchant_costs.punch"));
        enchant_costs.put(Enchantment.FLAME,
            main.getConfig().getInt("default_enchant_costs.flame"));
        enchant_costs.put(Enchantment.INFINITY,
            main.getConfig().getInt("default_enchant_costs.infinity"));
        enchant_costs.put(Enchantment.LUCK_OF_THE_SEA,
            main.getConfig().getInt("default_enchant_costs.luck_of_the_sea"));
        enchant_costs.put(Enchantment.LURE,
            main.getConfig().getInt("default_enchant_costs.lure"));
        enchant_costs.put(Enchantment.FROST_WALKER,
            main.getConfig().getInt("default_enchant_costs.frost_walker"));
        enchant_costs.put(Enchantment.MENDING,
            main.getConfig().getInt("default_enchant_costs.mending"));
        enchant_costs.put(Enchantment.IMPALING,
            main.getConfig().getInt("default_enchant_costs.impaling"));
        enchant_costs.put(Enchantment.RIPTIDE,
            main.getConfig().getInt("default_enchant_costs.riptide"));
        enchant_costs.put(Enchantment.MULTISHOT,
            main.getConfig().getInt("default_enchant_costs.multishot"));
        enchant_costs.put(Enchantment.PIERCING,
            main.getConfig().getInt("default_enchant_costs.piercing"));
        enchant_costs.put(Enchantment.QUICK_CHARGE,
            main.getConfig().getInt("default_enchant_costs.quick_charg"));
        enchant_costs.put(Enchantment.SOUL_SPEED,
            main.getConfig().getInt("default_enchant_costs.soul_speed"));
        enchant_costs.put(Enchantment.SWIFT_SNEAK,
            main.getConfig().getInt("default_enchant_costs.swift_sneak"));
        enchant_costs.put(Enchantment.SWEEPING_EDGE,
            main.getConfig().getInt("default_enchant_costs.sweeping_edge"));
        enchant_costs.put(Enchantment.CHANNELING,
                main.getConfig().getInt("default_enchant_costs.channeling"));
        enchant_costs.put(Enchantment.WIND_BURST,
                main.getConfig().getInt("default_enchant_costs.wind_burst"));
    }
}
