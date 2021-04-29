package cz.larkyy.lchangelogs.listeners;

import cz.larkyy.lchangelogs.LChangeLogs;
import cz.larkyy.lchangelogs.handlers.MessageHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        int value = getMain().getDatabase().getData(p.getUniqueId());
        int size = getMain().getStorageHandler().getChangeLogs().size();

        if (value < size) {
            MessageHandler.NOTIFICATION.sendMsg(p);
        }

    }
    private LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }
}
