package me.anonymusdennis.diamondmoney;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import net.ess3.api.events.UserBalanceUpdateEvent;
import net.minecraft.server.TickTask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        String pName = player.getName();
        UUID uuid = player.getUniqueId();
        BigDecimal aa = (BigDecimal) main.playerlist.get(uuid);
        if(aa == null)
        {
            aa = BigDecimal.valueOf(0);
        }
        BigDecimal bg = BigDecimal.valueOf(0);
        try {
            bg = aa.subtract(Economy.getMoneyExact(uuid));
        } catch (UserDoesNotExistException userDoesNotExistException) {
            userDoesNotExistException.printStackTrace();
        }
        controlloop.removeItems(player.getInventory(), Material.DIAMOND,bg.intValueExact(),player,false);
        int money = Moneyhook.getMoney(player);
        main.moneyhook.updatePlayermoney(player, money);
        int availmoney = main.config.playerspaidcfg.getInt(String.valueOf(uuid));
        if(availmoney > 0)
            player.sendMessage(ChatColor.GREEN + "Du hast in deiner Bank " + availmoney + " Diamanten, hol sie dir jetzt ins inventar mit /auszahlen!");
    }
}
