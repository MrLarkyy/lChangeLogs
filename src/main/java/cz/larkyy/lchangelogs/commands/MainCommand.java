package cz.larkyy.lchangelogs.commands;

import cz.larkyy.lchangelogs.LChangeLogs;
import cz.larkyy.lchangelogs.handlers.MessageHandler;
import cz.larkyy.lchangelogs.handlers.PermissionHandler;
import cz.larkyy.lchangelogs.inventories.ChangeLogsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            MessageHandler.ONLY_PLAYER.sendMsg(sender);
            return false;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            openInv(p);

            return false;
        }

        switch (args[0]) {
            case "create":
                new CreateCommand(p,args);
                break;
            case "reload":
                if (sender instanceof Player && !PermissionHandler.RELOAD.has((Player)sender,true)) {
                    return false;
                }
                getMain().loadCfgs();
                MessageHandler.RELOAD.sendMsg(sender);
                break;
            default:
                openInv(p);
                break;
        }

        return false;

    }
    private LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }

    private void openInv(Player p) {
        p.openInventory(new ChangeLogsInventory(0,p).getInventory());

        int value = getMain().getDatabase().getData(p.getUniqueId());
        int size = getMain().getStorageHandler().getChangeLogs().size();

        if (value < size) {
            getMain().getDatabase().setData(p.getUniqueId());
        }
    }
}
