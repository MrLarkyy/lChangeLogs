package cz.larkyy.lchangelogs.inventories;

import cz.larkyy.lchangelogs.LChangeLogs;
import cz.larkyy.lchangelogs.handlers.ConfigHandler;
import cz.larkyy.lchangelogs.objects.ChangeLogObj;
import cz.larkyy.lchangelogs.utils.GuiUtils;
import cz.larkyy.lchangelogs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChangeLogsInventory implements InventoryHolder {

    private Inventory gui;
    private int page;
    private final Player p;
    private final boolean permission;

    public ChangeLogsInventory(int page, Player p) {
        this.page = page;
        this.p = p;
        this.permission = p.hasPermission("changelogs.admin");
    }

    @Override
    public @NotNull Inventory getInventory() {
        gui = Bukkit.createInventory(this,45,"Changelogs");
        solveItems();
        return gui;
    }
    private void solveItems() {
        String path = "inventories.main.";

        if (getCfg().getConfiguration().getConfigurationSection(path+".items")==null)
            return;
        for (String str : getCfg().getConfiguration().getConfigurationSection(path+".items").getKeys(false)) {

            String itempath = path+".items."+str;
            switch (str) {
                case "changelogs":
                    loadChangesItems(itempath);
                    break;
                default:
                    loadItem(itempath,null);
                    break;
            }
        }
    }

    private void loadItem(String itemPath, String localizedName) {
        if (!isMoreSlots(itemPath)) {
            int slot = getCfg().getConfiguration().getInt(itemPath + ".slot");
            if (slot != -1) {
                gui.setItem(slot, mkItem(itemPath, localizedName));
            }

        } else {
            ItemStack is = mkItem(itemPath, localizedName);
            for (int i : getCfg().getConfiguration().getIntegerList(itemPath + ".slots")) {
                gui.setItem(i, is);
            }
        }
    }

    private void loadChangesItems(String itempath) {
        List<ItemStack> changesItems = new ArrayList<>();

        Map<Integer, ChangeLogObj> map = new TreeMap<>(Collections.reverseOrder());
        map.putAll(getMain().getStorageHandler().getChangeLogs());

        for (Map.Entry<Integer,ChangeLogObj> pair : map.entrySet()) {

            ChangeLogObj changeLog = pair.getValue();

            if (changeLog.isPublished()) {
                changesItems.add(getUtils().mkItem(
                        //MATERIAL
                        Material.valueOf(getCfg().getConfiguration().getString(itempath + ".materialPublished", "MAP")),
                        //NAME
                        Utils.format(getCfg().getConfiguration().getString(itempath + ".name", "&e%title% %status%").replace("%title%", changeLog.getName()).replace("%status%","")),
                        //LOCALIZEDNAME
                        "Changelog " + pair.getKey(),
                        //LORE
                        getUtils().formatList(replaceLore(getCfg().getConfiguration().getStringList(itempath + ".lore"), changeLog)),
                        //TEXTURE
                        getCfg().getConfiguration().getString(itempath + ".texture", null)
                ));
            } else if (permission) {
                changesItems.add(getUtils().mkItem(
                        //MATERIAL
                        Material.valueOf(getCfg().getConfiguration().getString(itempath + ".material", "PAPER")),
                        //NAME
                        Utils.format(getCfg().getConfiguration().getString(itempath + ".name", "&e%title% %status%").replace("%title%", changeLog.getName()).replace("%status%","&7(Not published)")),
                        //LOCALIZEDNAME
                        "Changelog " + pair.getKey(),
                        //LORE
                        getUtils().formatList(replaceLore(getCfg().getConfiguration().getStringList(itempath + ".lore"), changeLog)),
                        //TEXTURE
                        getCfg().getConfiguration().getString(itempath + ".texture", null)
                ));
            }
        }
        loadChanges(itempath,changesItems);
    }

    private List<String> replaceLore(List<String> lore,ChangeLogObj changeLog) {
        List<String> newLore = new ArrayList<>();
        for (String str : lore) {
            if (str.contains("%changes%")) {
                for (String strChange : changeLog.getChanges()) {
                    newLore.add("&7- &f"+strChange);
                }
            } else newLore.add(str);
        }

        return newLore;
    }

    private void loadChanges(String itempath, List<ItemStack> changeItems) {
        List<Integer> slots = getCfg().getConfiguration().getIntegerList(itempath+".slots");

        int first = slots.size()*page;
        for (int i : slots) {
            try {
                gui.setItem(i,changeItems.get(first));
            } catch (IndexOutOfBoundsException ex) {
                continue;
            }
            first++;
        }

    }


    private boolean isMoreSlots(String itemPath) {
        return GuiUtils.isMoreSlots(itemPath, getCfg()  );
    }

    private ItemStack mkItem(String itemPath, String localizedName) {
        return getUtils().mkItem(
                //MATERIAL
                Material.valueOf(getCfg().getConfiguration().getString(itemPath + ".material", "STONE")),
                //NAME
                Utils.format(getCfg().getConfiguration().getString(itemPath + ".name", null)),
                //LOCALIZEDNAME
                localizedName,
                //LORE
                getUtils().formatList(getCfg().getConfiguration().getStringList(itemPath + ".lore")),
                //TEXTURE
                getCfg().getConfiguration().getString(itemPath + ".texture", null));
    }

    private LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }

    private Utils getUtils() {
        return getMain().getUtils();
    }

    private ConfigHandler getCfg() {
        return getMain().getCfg();
    }
}
