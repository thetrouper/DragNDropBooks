package com.vexx.dragNDropBooks.Utilities;

import com.vexx.dragNDropBooks.DragNDropBooks;

public class ConfigManager {
    public boolean cost_settings_enabled ;
    public int player_level_cost_per_enchant_level;
    public double refund_rate;

    public ConfigManager(DragNDropBooks main){
        this.cost_settings_enabled = main.getConfig().getBoolean("cost_settings.enabled");
        this.player_level_cost_per_enchant_level = main.getConfig().getInt("cost_settings.player_level_cost_per_enchant_level");
        this.refund_rate = main.getConfig().getDouble("cost_settings.refund_settings.refund_rate");
    }
}
