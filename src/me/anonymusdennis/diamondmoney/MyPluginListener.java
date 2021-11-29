package me.anonymusdennis.diamondmoney;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.math.BigDecimal;
import java.util.UUID;

public class MyPluginListener implements Listener {
    public static main plugin;

    public MyPluginListener(main  instance) {
        plugin = instance;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onOpen(final InventoryOpenEvent e)
    {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin, new Runnable() {
                    public void run() {main.moneyhook.onInventorChanges(e.getPlayer().getInventory(),null,e.getPlayer());}},2);
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void closeInventory(final InventoryCloseEvent e)
    {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin, new Runnable() {
                    public void run() {main.moneyhook.onInventorChanges(e.getPlayer().getInventory(),null,e.getPlayer());}},2);
    }

    @EventHandler
    public void itemPickup(EntityPickupItemEvent e) {

        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            //Important: The class Player, not a variable. This works because Entity is a subclass of Player
            //Do stuff here
            main.moneyhook.onInventorChanges(((HumanEntity) e.getEntity()).getInventory(),e.getItem().getItemStack(),((HumanEntity) e.getEntity()));
        }


    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMoneyChange(UserBalanceUpdateEvent change) {
        main.task.run(change);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        String pName = player.getName();
        UUID uuid = player.getUniqueId();
        int money = Moneyhook.getMoney(player);
        main.moneyhook.updatePlayermoney(player, money);
        System.out.println("momenten line 92");
        int availmoney = main.config.playerspaidcfg.getInt(String.valueOf(uuid));
        if(availmoney > 0)
            player.sendMessage(ChatColor.GREEN + "On your account there are " + availmoney + " "+ main.instance.currency.toString() + "! Collect them with the Command /payout \n also you might have lost some money if you are buying items in your shop too");
    }
}
