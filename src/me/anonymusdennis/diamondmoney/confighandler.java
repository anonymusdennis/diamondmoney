package me.anonymusdennis.diamondmoney;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class confighandler {
    private main plugin = main.instance;
    // Files & File Configs Here
    public FileConfiguration playerspaidcfg;
    public File playerspaidfile;
    // --------------------------

    public void setup()
    {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        playerspaidfile = new File(plugin.getDataFolder(), "playeroverpaid.yml");
        if (!playerspaidfile.exists()) {
            try {
                playerspaidfile.createNewFile();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The players.yml file has been created");
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + "Could not create the players.yml file");
            }
        }
        playerspaidcfg = YamlConfiguration.loadConfiguration(playerspaidfile);
    }
    public void savePlayers() {
        try {
            playerspaidcfg.save(playerspaidfile);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The players.yml file has been saved");

        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the players.yml file");
        }
    }
    public void importPlayerlist() {
    }
    public static void savePlayerlist() {


    }
}
