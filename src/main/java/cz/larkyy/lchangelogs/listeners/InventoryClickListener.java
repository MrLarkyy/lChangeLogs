package cz.larkyy.lchangelogs.listeners;

import cz.larkyy.lchangelogs.LChangeLogs;
import cz.larkyy.lchangelogs.handlers.MessageHandler;
import cz.larkyy.lchangelogs.inventories.ChangeLogsInventory;
import cz.larkyy.lchangelogs.inventories.EditInventory;
import cz.larkyy.lchangelogs.objects.ChangeLogObj;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof EditInventory) {
            e.setCancelled(true);

            EditInventory editInventory = (EditInventory) holder;
            ChangeLogObj changelog = editInventory.getChangeLog();

            ItemStack is = e.getCurrentItem();
            if (is==null) return;
            if (is.getItemMeta()==null) return;
            String lname = is.getItemMeta().getLocalizedName();

            switch (lname) {
                case "publish":
                    changelog.setPublished(true);
                    MessageHandler.PUBLISH_ANNOUNCE.broadcast();
                    p.closeInventory();
                    return;
                case "back":
                    p.openInventory(new ChangeLogsInventory(0,p).getInventory());
                    return;
                case "delete":
                    getMain().getStorageHandler().removeChangeLog(changelog);
                    MessageHandler.CL_REMOVED.sendMsg(p);
                    p.openInventory(new ChangeLogsInventory(0,p).getInventory());
                    getMain().getDatabase().decreaseUpdate();
                    return;
                case "add":
                    getMain().getStorageHandler().addEditingPlayer(p,changelog);
                    p.closeInventory();
                    return;
                default:
                    if (lname.contains("Change")) {
                        int id = Integer.parseInt(lname.split(" ")[1]);
                        MessageHandler.CHANGE_REMOVED.sendMsg(p);
                        changelog.removeChange(id);
                        p.openInventory(new EditInventory(0,changelog).getInventory());
                        return;
                    }
            }
        }
        if (holder instanceof ChangeLogsInventory) {
            e.setCancelled(true);

            ChangeLogsInventory inv = (ChangeLogsInventory) holder;

            ItemStack is = e.getCurrentItem();
            if (is==null) return;
            if (is.getItemMeta()==null) return;
            String lname = is.getItemMeta().getLocalizedName();

            switch (lname) {
                case "close":
                    break;
                case "next-page":
                    break;
                case "prev-page":
                    break;
                default:
                    if (lname.contains("Changelog")) {
                        if (!p.hasPermission("changelogs.admin")) {
                            return;
                        }

                        int id = Integer.parseInt(lname.split(" ")[1]);
                        ChangeLogObj changelog = getMain().getStorageHandler().getChangeLogs().get(id);

                        p.openInventory(new EditInventory(0,changelog).getInventory());
                        return;
                    }
            }
        }
    }

    private LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }
}
