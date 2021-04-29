package cz.larkyy.lchangelogs.handlers;

import cz.larkyy.lchangelogs.LChangeLogs;
import cz.larkyy.lchangelogs.objects.ChangeLogObj;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageHandler {

    private final Map<Integer,ChangeLogObj> changeLogs = new HashMap<>();
    private final Map<Player,ChangeLogObj> editingPlayers = new HashMap<>();

    public void loadChangeLogs() {
        if (getMain().getDataCfg().getConfiguration().getConfigurationSection("changelogs")==null) return;

        for (String str : getDataCfg().getConfiguration().getConfigurationSection("changelogs").getKeys(false)) {
            String title = getDataCfg().getConfiguration().getString("changelogs."+str+".title");
            List<String> changes = getDataCfg().getConfiguration().getStringList("changelogs."+str+".changes");
            Boolean published = getDataCfg().getConfiguration().getBoolean("changelogs."+str+".published");

            ChangeLogObj changeLog = new ChangeLogObj(title,changes,Integer.parseInt(str),published);
            this.changeLogs.put(Integer.parseInt(str),changeLog);
        }
    }

    public void saveChangeLogs() throws IOException {
        getDataCfg().getConfiguration().set("changelogs",null);
        if (changeLogs.isEmpty()) return;

        for (Map.Entry<Integer,ChangeLogObj> pair : changeLogs.entrySet()) {
            ChangeLogObj changeLog = pair.getValue();
            getDataCfg().getConfiguration().set("changelogs."+pair.getKey()+".title",changeLog.getName());
            getDataCfg().getConfiguration().set("changelogs."+pair.getKey()+".changes",changeLog.getChanges());
            getDataCfg().getConfiguration().set("changelogs."+pair.getKey()+".published",changeLog.isPublished());
        }
        getDataCfg().save();
    }

    public Map<Player, ChangeLogObj> getEditingPlayers() {
        return editingPlayers;
    }

    public void addEditingPlayer(Player p, ChangeLogObj changeLog) {
        this.editingPlayers.put(p,changeLog);
    }

    public  void removeEditingPlayer(Player p) {
        this.editingPlayers.remove(p);
    }

    public ChangeLogObj addChangelog(String title,List<String> changes) {

        ChangeLogObj changeLog = new ChangeLogObj(title,changes,getNextEmptyID());

        changeLogs.put(changeLog.getId(),changeLog);

        return changeLog;
    }

    public Map<Integer, ChangeLogObj> getChangeLogs() {
        return changeLogs;
    }

    public void removeChangeLog(ChangeLogObj changeLog) {
        changeLogs.remove(changeLog.getId());
    }

    private int getNextEmptyID() {
        for (int i = 0; i < Integer.MAX_VALUE;i++) {
            if (!changeLogs.containsKey(i)) return i;
        }
        return 1000;
    }

    private static LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }

    private ConfigHandler getDataCfg() {
        return getMain().getDataCfg();
    }
}
