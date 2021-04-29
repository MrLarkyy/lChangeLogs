package cz.larkyy.lchangelogs.listeners;

import cz.larkyy.lchangelogs.LChangeLogs;
import cz.larkyy.lchangelogs.handlers.MessageHandler;
import cz.larkyy.lchangelogs.inventories.EditInventory;
import cz.larkyy.lchangelogs.objects.ChangeLogObj;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (getMain().getStorageHandler().getEditingPlayers().containsKey(p)) {
            e.setCancelled(true);

            ChangeLogObj changeLog = getMain().getStorageHandler().getEditingPlayers().get(p);

            getMain().getStorageHandler().removeEditingPlayer(p);

            if (e.getMessage().equalsIgnoreCase("cancel")) {
                MessageHandler.CANCELLED.sendMsg(p);
                openInv(p,new EditInventory(0,changeLog).getInventory());
                return;
            }

            changeLog.addChange(e.getMessage());
            openInv(p,new EditInventory(0,changeLog).getInventory());
            MessageHandler.CHANGE_CREATED.sendMsg(p);
        }
    }

    private LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }

    private void openInv(Player p, Inventory inv) {

        new BukkitRunnable() {
            @Override
            public void run() {
                p.openInventory(inv);
            }
        }.runTask(getMain());

    }
}
