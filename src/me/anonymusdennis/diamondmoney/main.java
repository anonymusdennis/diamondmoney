package me.anonymusdennis.diamondmoney;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.earth2me.essentials.economy.EconomyLayers;
import net.ess3.api.MaxMoneyException;
import net.minecraft.server.TickTask;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class main extends JavaPlugin {
    public static Map playerlist = new HashMap<UUID, BigDecimal>();
    public static Map playerMoneylist = new HashMap<UUID, BigDecimal>();
    public static Map playerMoneyOverflowlist = new HashMap<UUID, BigDecimal>();
    public static main instance;
    public static Moneyhook moneyhook;
    public static confighandler config;
    private static File f;
    public static controlloop task;
    public static boolean recentmoneychangebyplugin = false;

    public void onEnable() {
        saveDefaultConfig();
        File folder = this.getDataFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }
        f = new File(folder, "playeroverpaid.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MyPluginListener pl = new MyPluginListener(this);
        getLogger().info("Enabled! All");
        Bukkit.getPluginManager().registerEvents(pl, this);
        instance = this;
        task = new controlloop();
        moneyhook = new Moneyhook();
        config = new confighandler();
        config.setup();
        BukkitTask task = new BukkitRunnable() {
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    moneyhook.updatePlayermoney(player, Moneyhook.getMoney(player));
                });
            }
        }.runTaskTimer(this,0,5);
        Bukkit.getOnlinePlayers().forEach(player -> {
            int money = main.moneyhook.getMoney(player);
            playerlist.put(player.getUniqueId(), BigDecimal.valueOf(money));
            try {
                playerMoneylist.put(player.getUniqueId(), Economy.getMoneyExact(player.getUniqueId()));
            } catch (UserDoesNotExistException e) {
                e.printStackTrace();
            }

        });

    }


    // These To well logged in the Console Saying "Disabled!" and "Enabled!"
    public void onDisable() {
        getLogger().info("Disabled!");

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("auszahlen")) {
            config.setup();
            UUID uid = player.getUniqueId();
            int amount = config.playerspaidcfg.getInt(uid.toString());
            config.playerspaidcfg.set(uid.toString(), 0);
            controlloop.removeItems(player.getInventory(), Material.DIAMOND, -1 * amount, player, true);

        }
        if (cmd.getName().equalsIgnoreCase("money") || cmd.getName().equalsIgnoreCase("balance")) {
            player.sendMessage(ChatColor.GREEN + "Sie haben in ihrer Bank '" + ChatColor.RED + config.playerspaidcfg.getInt(player.getUniqueId().toString()) + ChatColor.GREEN + "' Diamanten. Buchen sie es jetzt mit /auszahlen ab");
            player.sendMessage(ChatColor.GOLD + "Sie haben in ihrem Inventar '" + ChatColor.RED + Moneyhook.getMoney(player) + ChatColor.GOLD + "' Diamanten.");

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("auszahlen") || cmd.getName().equalsIgnoreCase("money") || cmd.getName().equalsIgnoreCase("balance")) {
            ArrayList<String> entityTypes = new ArrayList<String>();
            return entityTypes;

        }

        return null;
    }

}

