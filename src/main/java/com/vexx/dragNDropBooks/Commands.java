package com.vexx.dragNDropBooks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    com.vexx.dragNDropBooks.main main;

    public Commands(com.vexx.dragNDropBooks.main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player player) {
            if (args[0].equals("enable")  && args[1].equals("cost")) {
                main.getConfig().addDefault("cost_settings.enabled", true);
                main.saveDefaultConfig();
                player.sendMessage(ChatColor.GREEN + "costs have been enabled.");
            }
        }

        if (commandSender instanceof Player player) {
            if (args[0].equals("disable")  && args[1].equals("cost")) {
                main.getConfig().addDefault("cost_settings.enabled", false);
                main.saveDefaultConfig();
                player.sendMessage(ChatColor.RED + "costs have been disabled.");
            }
        }
        return false;
    }

//    @Override
//    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
//        if (commandSender instanceof Player player) {
//            if (args.length == 2) {
//                switch (args[0]) {
//                    case "enable":
//                        switch (args[1]) {
//                            case "cost":
//                                main.getConfig().set("true", "cost_settings.enabled");
//                                main.saveConfig();
//                        }
//                    case "disable":
//                        switch (args[1]) {
//                            case "cost":
//                                main.getConfig().set("false", "cost_settings.enabled");
//                                main.saveConfig();
//                        }
//                }
//            }
//        }
//        return false;
//    }
}
