package cz.larkyy.lchangelogs.inventories;

import cz.larkyy.lchangelogs.LChangeLogs;
import cz.larkyy.lchangelogs.handlers.ConfigHandler;
import cz.larkyy.lchangelogs.objects.ChangeLogObj;
import cz.larkyy.lchangelogs.utils.GuiUtils;
import cz.larkyy.lchangelogs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EditInventory implements InventoryHolder {

    private Inventory gui;
    private int page;
    private ChangeLogObj changeLog;

    public EditInventory(int page,ChangeLogObj changeLog) {
        this.page = page;
        this.changeLog = changeLog;
    }

    public ChangeLogObj getChangeLog() {
        return changeLog;
    }

    @Override
    public @NotNull Inventory getInventory() {
        gui = Bukkit.createInventory(this,45,"Changelog Editor");
        solveItems();
        return gui;
    }
    private void solveItems() {
        String path = "inventories.edit.";

        if (getCfg().getConfiguration().getConfigurationSection(path+".items")==null)
            return;
        for (String str : getCfg().getConfiguration().getConfigurationSection(path+".items").getKeys(false)) {

            String itempath = path+".items."+str;
            switch (str) {
                case "changes":
                    loadChangesItems(itempath);
                    break;
                case "publish":
                    loadItem(itempath,"publish");
                    break;
                case "add":
                    loadItem(itempath,"add");
                    break;
                case "back":
                    loadItem(itempath,"back");
                    break;
                case "delete":
                    loadItem(itempath,"delete");
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

        int calculation = 0;
        for (String change : changeLog.getChanges()) {
            changesItems.add(getUtils().mkItem(
                    //MATERIAL
                    Material.valueOf(getCfg().getConfiguration().getString(itempath+".material", "PAPER")),
                    //NAME
                    Utils.format(getCfg().getConfiguration().getString(itempath+".name", "&e%change%").replace("%change%", change)),
                    //LOCALIZEDNAME
                    "Change " + calculation,
                    //LORE
                    getUtils().formatList(replaceLore(getCfg().getConfiguration().getStringList(itempath+".lore"),change)),
                    //TEXTURE
                    getCfg().getConfiguration().getString(itempath+".texture", null)
            ));
            calculation++;
        }
        loadChanges(itempath,changesItems);
    }

    private List<String> replaceLore(List<String> lore,String change) {
        List<String> newLore = new ArrayList<>();
        for (String str : lore) {
            if (str.contains("%change%")) {
                    String newLine = str.replace("%change%", change);
                    newLore.add(newLine);
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
