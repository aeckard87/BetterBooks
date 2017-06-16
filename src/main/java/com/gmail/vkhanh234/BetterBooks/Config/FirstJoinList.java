/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package com.gmail.vkhanh234.BetterBooks.Config;

import com.gmail.vkhanh234.BetterBooks.BetterBooks;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class FirstJoinList {
    private FileConfiguration config;
    private File configFile;
    private List<String> players = new ArrayList<String>();
    private String filename = "playerlist.yml";

    public FirstJoinList() {
        this.configFile = new File(BetterBooks.getPlugin().getDataFolder(), this.filename);
        try {
            this.update();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void update() throws IOException, InvalidConfigurationException {
        if (!this.configFile.exists()) {
            this.configFile.createNewFile();
        }
        this.config = YamlConfiguration.loadConfiguration((File)this.configFile);
        this.players = this.config.getStringList("List");
    }

    public boolean checkPlayer(Player p) {
        return !this.players.contains(p.getName());
    }

    public void addPlayer(Player p) {
        this.players.add(p.getName());
        this.config.set("List", this.players);
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)BetterBooks.getPlugin(), new Runnable(){

            @Override
            public void run() {
                try {
                    FirstJoinList.this.config.save(FirstJoinList.this.configFile);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FileConfiguration get() {
        return this.config;
    }

}

