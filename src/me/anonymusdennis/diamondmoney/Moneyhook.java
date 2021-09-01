package me.anonymusdennis.diamondmoney;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.UUID;

public class Moneyhook {
    public Moneyhook() {

    }

    public void onInventorChanges(Inventory e, ItemStack itemStack, HumanEntity player) {
        int amount = 0;
        for (ItemStack item : e.getContents()) {
            if (item == null)
                amount += 0;
            else if (item.getType() == Material.DIAMOND) {
                amount += item.getAmount();
            }
        }
        if (itemStack == null)
            amount += 0;
        else if (itemStack.getType() == Material.DIAMOND) {
            amount += itemStack.getAmount();
        }
        main.instance.getLogger().info("player has " + amount + "Diamonds");
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
            else if (item.getType() == Material.DIAMOND) {
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
            Economy.setMoney(player.getUniqueId(), BigDecimal.valueOf(money));
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
