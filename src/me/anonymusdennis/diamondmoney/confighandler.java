package me.anonymusdennis.diamondmoney;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class confighandler {
    private main plugin = main.instance;
    // Files & File Configs Here
    public FileConfiguration playerspaidcfg;
    public File playerspaidfile;
    // --------------------------
    public File configfile;
    public FileConfiguration configfilecfg;
    public void setup()
    {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        playerspaidfile = new File(plugin.getDataFolder(), "playeroverpaid.yml");
        configfile = new File(plugin.getDataFolder(), "config.yml");
        if (!playerspaidfile.exists()) {
            try {
                playerspaidfile.createNewFile();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The players.yml file has been created");
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + "Could not create the players.yml file");
            }
        }
        if (!configfile.exists()) {
            try {
                configfile.createNewFile();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The config.yml file has been created");
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + "Could not create the config.yml file");
            }
        }
        playerspaidcfg = YamlConfiguration.loadConfiguration(playerspaidfile);
        configfilecfg = YamlConfiguration.loadConfiguration(configfile);
    }
    public void savePlayers() {
        try {
            playerspaidcfg.save(playerspaidfile);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The players.yml file has been saved");

        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the players.yml file");
        }
    }
    public static void savePlayerlist() {


    }
}
