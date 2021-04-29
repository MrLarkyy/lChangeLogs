package cz.larkyy.lchangelogs.commands;

import cz.larkyy.lchangelogs.LChangeLogs;
import cz.larkyy.lchangelogs.handlers.MessageHandler;
import cz.larkyy.lchangelogs.handlers.PermissionHandler;
import cz.larkyy.lchangelogs.inventories.EditInventory;
import cz.larkyy.lchangelogs.objects.ChangeLogObj;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CreateCommand {

    public CreateCommand(Player p,String[] args) {

        if (!PermissionHandler.CREATE.has(p,true)) {
            return;
        }

        if (args.length < 2) {
            p.sendMessage(MessageHandler.SYNTAX.buildSyntaxStr("create","version"));
            return;
        }

        p.sendMessage(MessageHandler.CL_CREATED.toString().replace("%title%",args[1]));

        ChangeLogObj changeLog = getMain().getStorageHandler().addChangelog(args[1],new ArrayList<>());
        p.openInventory(new EditInventory(0,changeLog).getInventory());

    }
    private LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }
}
