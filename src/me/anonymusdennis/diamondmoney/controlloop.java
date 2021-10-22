package me.anonymusdennis.diamondmoney;

import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.UUID;

public class controlloop {

    public void run(UserBalanceUpdateEvent change){
        if(!main.recentmoneychangebyplugin) {
            UUID uuid = change.getPlayer().getUniqueId();
            try {
                if (!(((BigDecimal)main.playerMoneylist.get(uuid)).add(BigDecimal.valueOf(main.config.playerspaidcfg.getInt(change.getPlayer().getUniqueId().toString())), new MathContext(34,RoundingMode.HALF_EVEN))).equals(change.getNewBalance())) {
                    BigDecimal bg = change.getOldBalance().subtract(change.getNewBalance(), new MathContext(34,RoundingMode.HALF_EVEN));
                    System.out.println(get_bg_int_value(bg,true));
                    System.out.println("Booking $" + -get_bg_int_value(bg,true) + " on the player " + change.getPlayer().getName() + "'s account");
                    removeItems(change.getPlayer().getInventory(), main.instance.currency, get_bg_int_value(bg,true), change.getPlayer(),false,true);
                    main.moneyhook.updatePlayermoney(change.getPlayer(), Moneyhook.getMoney(change.getPlayer()));
                }
            } catch (NullPointerException e) {

            }

            int money = Moneyhook.getMoney(change.getPlayer());
            if (money != get_bg_int_value(change.getNewBalance(),false))
                main.moneyhook.updatePlayermoney(change.getPlayer(), money);
        }
    else {
        main.recentmoneychangebyplugin = false;
        }
    }
    public static void removeItems(Inventory inventory, Material type, int amount,Player player,boolean toInventory) {
        removeItems(inventory, type, amount, player, toInventory,false);
    }
    public static void removeItems(Inventory inventory, Material type, int amount,Player player,boolean toInventory, boolean fromStorage) {
        System.out.println(amount);
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
            if(amount > 0)
            {
                main.config.playerspaidcfg.set(player.getUniqueId().toString(), main.config.playerspaidcfg.getInt(player.getUniqueId().toString()) - amount);
                System.out.println(main.config.playerspaidcfg.getInt(player.getUniqueId().toString()) + "were saved onto overpaid by functions");

                try {
                    main.config.playerspaidcfg.save(main.config.playerspaidfile);
                } catch (IOException e) {
                    e.printStackTrace();
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

                        is = new ItemStack(main.instance.currency, 64);
                        inventory.addItem(is);
                        amount -= 64;
                    } else {
                        is = new ItemStack(main.instance.currency, amount);
                        inventory.addItem(is);
                        amount = 0;
                    }

                } else if (is.getType().equals(main.instance.currency) && is.getAmount() < 64) {
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
    public int get_bg_int_value(BigDecimal bd, boolean round)
    {
        double bdd = bd.doubleValue();
        System.out.println(bdd);
        int bdi = (int) Math.round(bdd);
        System.out.println(bdi);
        return round ? bdi : bd.intValue();
    }
}