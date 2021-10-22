package me.anonymusdennis.diamondmoney;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import net.ess3.api.MaxMoneyException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

public class Moneyhook {

    public void onInventorChanges(Inventory e, ItemStack itemStack, HumanEntity player) {
        int amount = 0;
        for (ItemStack item : e.getContents()) {
            if (item == null)
                amount += 0;
            else if (item.getType() == main.instance.currency) {
                amount += item.getAmount();
            }
        }
        if (itemStack == null)
            amount += 0;
        else if (itemStack.getType() == main.instance.currency) {
            amount += itemStack.getAmount();
        }
        main.instance.getLogger().info("player has " + amount + main.instance.currency.toString());
        updatePlayermoney((Player) player, amount);

        try {
            Economy.setMoney(player.getUniqueId(), BigDecimal.valueOf(amount));
        } catch (NoLoanPermittedException noLoanPermittedException) {
            noLoanPermittedException.printStackTrace();
        } catch (UserDoesNotExistException userDoesNotExistException) {
            userDoesNotExistException.printStackTrace();
        } catch (MaxMoneyException maxMoneyException) {
            maxMoneyException.printStackTrace();
        }
    }

    public static int getMoney(Player player) {
        Inventory e = player.getInventory();
        int amount = 0;
        for (ItemStack item : e.getContents()) {
            if (item == null)
                amount += 0;
            else if (item.getType() == main.instance.currency) {
                amount += item.getAmount();
            }
        }
        return amount;
    }


    public void updatePlayermoney(Player player, int money) {
        main.playerlist.put(player.getUniqueId(), BigDecimal.valueOf(money));
        main.playerMoneylist.put(player.getUniqueId(), BigDecimal.valueOf(money));
        try {
            main.recentmoneychangebyplugin = true;
            Economy.setMoney(player.getUniqueId(), BigDecimal.valueOf(money + main.config.playerspaidcfg.getInt(player.getUniqueId().toString())));
        } catch (NoLoanPermittedException noLoanPermittedException) {
            main.recentmoneychangebyplugin = false;
            noLoanPermittedException.printStackTrace();
        } catch (UserDoesNotExistException userDoesNotExistException) {
            main.recentmoneychangebyplugin = false;
            userDoesNotExistException.printStackTrace();
        } catch (MaxMoneyException maxMoneyException) {
            maxMoneyException.printStackTrace();
            main.recentmoneychangebyplugin = false;
        }

    }

}
