package me.anonymusdennis.diamondmoney;

import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

public class controlloop {

    public void run(UserBalanceUpdateEvent change){
        if(!main.recentmoneychangebyplugin) {
            UUID uuid = change.getPlayer().getUniqueId();
            try {
                if (!main.playerMoneylist.get(uuid).equals(change.getNewBalance())) {
                    BigDecimal bg = change.getOldBalance().subtract(change.getNewBalance());
                    System.out.println("Booking $" + bg.intValueExact() * -1 + " on the player " + change.getPlayer().getName() + "'s account");
                    removeItems(change.getPlayer().getInventory(), Material.DIAMOND, bg.intValueExact(), change.getPlayer(),false);
                    main.moneyhook.updatePlayermoney(change.getPlayer(), Moneyhook.getMoney(change.getPlayer()));
                }
            } catch (NullPointerException e) {

            }

            int money = Moneyhook.getMoney(change.getPlayer());
            if (money != change.getNewBalance().intValueExact())
                main.moneyhook.updatePlayermoney(change.getPlayer(), money);
        }
    else {
        main.recentmoneychangebyplugin = false;
        }
    }
    public static void removeItems(Inventory inventory, Material type, int amount,Player player,boolean toInventory) {

        int size = inventory.getSize();
        if (amount >= 0) {
            for (int slot = 0; slot < size; slot++) {
                ItemStack is = inventory.getItem(slot);
                if (is == null) continue;
                if (type == is.getType()) {
                    int newAmount = is.getAmount() - amount;
                    if (newAmount > 0) {
                        is.setAmount(newAmount);
                        break;
                    } else {
                        inventory.clear(slot);
                        amount = -newAmount;
                    }
                    if (amount == 0) break;
                }
            }
        }
        if(amount < 0) {

            amount = amount * -1;
            if(toInventory) {
            for (int slot = 0; slot < size; slot++) {

                ItemStack is = inventory.getItem(slot);
                if (is == null) {
                    if(inventory.firstEmpty() == -1)
                        continue;
                    if (amount >= 64) {

                        is = new ItemStack(Material.DIAMOND, 64);
                        inventory.addItem(is);
                        amount -= 64;
                    } else {
                        is = new ItemStack(Material.DIAMOND, amount);
                        inventory.addItem(is);
                        amount = 0;
                    }

                } else if (is.getType().equals(Material.DIAMOND) && is.getAmount() < 64) {
                    if (amount >= 64 - is.getAmount()) {
                        amount -= 64 - is.getAmount();
                        is.setAmount(64);
                    } else {
                        is.setAmount(is.getAmount() + amount);
                        amount = 0;
                    }
                }
            }
            }

            if (true) {
                int bd = main.config.playerspaidcfg.getInt(player.getUniqueId().toString());
                bd += amount;
                main.config.playerspaidcfg.set(player.getUniqueId().toString(), bd);
                try {
                    main.config.playerspaidcfg.save(main.config.playerspaidfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(main.config.playerspaidcfg.getInt(player.getUniqueId().toString()) + "were saved onto overpaid");
            }
        }
    }
}