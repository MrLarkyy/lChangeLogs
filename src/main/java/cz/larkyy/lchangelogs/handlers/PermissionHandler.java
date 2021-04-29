package cz.larkyy.lchangelogs.handlers;

import cz.larkyy.lchangelogs.LChangeLogs;
import org.bukkit.entity.Player;

public enum PermissionHandler {
    OPEN("open","lchangelog.pay"),
    RELOAD("reload","lchangelog.reload"),
    CREATE("create","lchangelog.create"),
    ADMIN("admin","lchangelog.admin");

    private final String path;
    private final String defValue;

    PermissionHandler(String path,String defValue) {
        this.defValue = defValue;
        this.path = "permissions."+path;
    }

    public boolean has(Player p) {
        return (p.hasPermission(LChangeLogs.getMain().getCfg().getString(path,defValue)));
    }

    public boolean has(Player p,boolean msg) {
        final boolean bool = p.hasPermission(LChangeLogs.getMain().getCfg().getString(path,defValue));
        if (msg && !bool)
            MessageHandler.NO_PERMISSION.sendMsg(p);
        return bool;
    }
}
