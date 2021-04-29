package cz.larkyy.lchangelogs.utils;

import cz.larkyy.lchangelogs.handlers.ConfigHandler;

public class GuiUtils {

    public static boolean isMoreSlots(String itemPath, ConfigHandler config) {
        return config.getConfiguration().getConfigurationSection(itemPath).getKeys(false).contains("slots");
    }
}
