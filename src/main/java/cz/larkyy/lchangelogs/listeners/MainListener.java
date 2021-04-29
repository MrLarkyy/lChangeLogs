package cz.larkyy.lchangelogs.listeners;

import cz.larkyy.lchangelogs.LChangeLogs;

public class MainListener {

    public MainListener() {

        getMain().getServer().getPluginManager().registerEvents(new PlayerJoinListener(),getMain());
        getMain().getServer().getPluginManager().registerEvents(new PlayerChatListener(),getMain());
        getMain().getServer().getPluginManager().registerEvents(new InventoryClickListener(),getMain());

    }


    private static LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }
}
