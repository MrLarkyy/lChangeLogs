package cz.larkyy.lchangelogs.handlers;

import cz.larkyy.lchangelogs.LChangeLogs;
import cz.larkyy.lchangelogs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum MessageHandler {

    RELOAD("messages.reload","&6[lChangeLogs] &fPlugin has been reloaded!"),
    CHANGE_REMOVED("messages.changeRemoved","&6[lChangeLogs] &fChange has been removed!"),
    CHANGE_CREATED("messages.changeCreated","&6[lChangeLogs] &fChange has been created!"),
    CL_REMOVED("messages.removed","&6[lChangeLogs] &fChangeLog has been removed!"),
    CL_CREATED("messages.created","&6[lChangeLogs] &fChangeLog has been created! &7(%title%)"),
    CANCELLED("messages.cancel","&6[lChangeLogs] &fAction has been cancelled!"),
    PUBLISH_ANNOUNCE("messages.published",Arrays.asList("Missing Message in the config!")),
    NOTIFICATION("messages.notify",Arrays.asList("Missing Message in the config!")),
    NO_PERMISSION("messages.noPermission","&cNo Permission!"),
    SYNTAX("messages.usage.syntax","&cInvalid Command Usage! &7Usage: &f/lcl %arguments%"),
    USAGE_ARG("messages.usage.argument","<%arg%>"),
    ONLY_PLAYER("messages.onlyPlayer","&cOnly players can use this command!");

    private final String path;
    private String defValue = null;
    private List<String> defValueList = null;

    MessageHandler(String path,String defValue) {
        this.path = path;
        this.defValue = defValue;
    }
    MessageHandler(String path,List<String> defValue) {
        this.path = path;
        this.defValueList = defValue;
    }

    @Override
    public String toString() {
        return Utils.format(getMain().getCfg().getConfiguration().getString(path,defValue));
    }

    public List<String> getList() {
        return getUtils().formatList(getMain().getCfg().getStringList(path,defValueList));
    }

    public String buildSyntaxStr(String subCmd, String arg) {
        return toString().replace("%arguments%", subCmd + " " + USAGE_ARG)
                .replace("%arg%", arg);
    }

    public void sendMsg(CommandSender sender) {
        if (defValueList==null) {
            sender.sendMessage(toString());
            return;
        }
        for (String str : getList()) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                Set<String> keys = getMain().getCfg().getConfiguration().getConfigurationSection("messages.updateMsg").getKeys(false);
                String updateMsg;

                int amount = getMain().getStorageHandler().getChangeLogs().size()-getMain().getDatabase().getData(p.getUniqueId());

                if (keys.contains(String.valueOf(amount))) {
                    updateMsg = getMain().getCfg().getString("messages.updateMsg."+amount+".message","update");
                } else
                    updateMsg = getMain().getCfg().getString("messages.updateMsg.default.message","update");

                p.sendMessage(str.replace("%amount%",amount+"").replace("%update%",updateMsg));
            } else
                sender.sendMessage(str);
        }
    }

    public void broadcast() {
        if (defValueList==null) {
            Bukkit.broadcastMessage(toString());
            return;
        }
        for (String str : getList()) {
            Bukkit.broadcastMessage(str);
        }
    }

    private Utils getUtils() {
        return LChangeLogs.getMain().getUtils();
    }

    private LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }

}
