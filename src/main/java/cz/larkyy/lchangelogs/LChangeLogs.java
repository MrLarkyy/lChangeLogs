package cz.larkyy.lchangelogs;

import cz.larkyy.lchangelogs.commands.MainCommand;
import cz.larkyy.lchangelogs.handlers.ConfigHandler;
import cz.larkyy.lchangelogs.handlers.DatabaseHandler;
import cz.larkyy.lchangelogs.handlers.StorageHandler;
import cz.larkyy.lchangelogs.listeners.MainListener;
import cz.larkyy.lchangelogs.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class LChangeLogs extends JavaPlugin {

    private static LChangeLogs main;
    private Utils utils;
    private final ConfigHandler dataCfg = new ConfigHandler(this,"data.yml");
    private final ConfigHandler cfg = new ConfigHandler(this,"config.yml");
    private final DatabaseHandler database = new DatabaseHandler(this);
    private StorageHandler storageHandler;

    @Override
    public void onEnable() {
        main = this;
        utils = new Utils();
        storageHandler = new StorageHandler();

        loadCfgs();
        storageHandler.loadChangeLogs();

        getCommand("lchangelogs").setExecutor(new MainCommand());
        new MainListener();
    }

    @Override
    public void onDisable() {
        try {
            storageHandler.saveChangeLogs();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadCfgs() {
        dataCfg.load();
        cfg.load();
    }

    public static LChangeLogs getMain() {
        return main;
    }

    public Utils getUtils() {
        return utils;
    }

    public ConfigHandler getCfg() {
        return cfg;
    }

    public ConfigHandler getDataCfg() {
        return dataCfg;
    }

    public DatabaseHandler getDatabase() {
        return database;
    }

    public StorageHandler getStorageHandler() {
        return storageHandler;
    }
}
