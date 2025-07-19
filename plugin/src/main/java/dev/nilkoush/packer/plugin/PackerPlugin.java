package dev.nilkoush.packer.plugin;

import dev.nilkoush.packer.plugin.commands.PackerCommand;
import dev.nilkoush.thelibrary.paper.TheLibrary;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class PackerPlugin extends JavaPlugin {

    private static PackerPlugin instance;

    @Override
    public void onLoad() {
        TheLibrary.onLoad(this);
    }

    @Override
    public void onEnable() {
        TheLibrary.onEnable();
        TheLibrary.setGlobalReplacements(Map.of(
                "prefix", "<primary>Packer <light>•",
                "primary", "<#12ADF5>",
                "secondary", "<#5ec5f5>",
                "light", "<#a9ddf5>"
        ));
        Bukkit.getCommandMap().register("packer", new PackerCommand());
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
            saveResource("items/items.yml", false);
            new File(getDataFolder(), "pack").mkdirs();
        }
    }

    @Override
    public void onDisable() {
        TheLibrary.onDisable();
    }

    public static PackerPlugin getInstance() {
        if (instance == null) {
            instance = JavaPlugin.getPlugin(PackerPlugin.class);
        }
        return instance;
    }
}