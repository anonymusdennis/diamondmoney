package me.anonymusdennis.diamondmoney;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
    public Material currency;

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

        instance = this;
        task = new controlloop();
        moneyhook = new Moneyhook();
        config = new confighandler();
        config.setup();
        String curr = config.configfilecfg.getString("currencyitem");
        if(curr == null || curr.isEmpty())
        {
            curr = "DIAMOND";
            config.configfilecfg.set("currencyitem", curr);
        }
        try {
            config.configfilecfg.save(config.configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currency = Material.getMaterial(curr);
        BukkitTask task = new BukkitRunnable() {
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    System.out.println("momenten line 64");
                    moneyhook.updatePlayermoney(player, Moneyhook.getMoney(player));
                    System.out.println(Moneyhook.getMoney(player));
                });
            }
        }.runTaskTimer(this,0,5);
        MyPluginListener pl = new MyPluginListener(this);
        getLogger().info("Enabled! All");
        Bukkit.getPluginManager().registerEvents(pl, this);
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

        if (cmd.getName().equalsIgnoreCase("payout")) {
            config.setup();
            UUID uid = player.getUniqueId();
            int amount = config.playerspaidcfg.getInt(uid.toString());
            config.playerspaidcfg.set(uid.toString(), 0);
            controlloop.removeItems(player.getInventory(), main.instance.currency, -1 * amount, player, true);

        }
        if (cmd.getName().equalsIgnoreCase("money") || cmd.getName().equalsIgnoreCase("balance")) {
            player.sendMessage(ChatColor.GREEN + "You have on your account '" + ChatColor.RED + config.playerspaidcfg.getInt(player.getUniqueId().toString()) + ChatColor.GREEN + "' "+ main.instance.currency.toString() + ". You can collect the "+ main.instance.currency.toString() + " with the command /payout");
            player.sendMessage(ChatColor.GOLD + "You have '" + ChatColor.RED + Moneyhook.getMoney(player) + ChatColor.GOLD + "' "+ main.instance.currency.toString() + " in your Inventory.");

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("payout") || cmd.getName().equalsIgnoreCase("money") || cmd.getName().equalsIgnoreCase("balance")) {
            ArrayList<String> entityTypes = new ArrayList<String>();
            return entityTypes;

        }

        return null;
    }


}

