/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.gmail.vkhanh234.BetterBooks;

import com.gmail.vkhanh234.BetterBooks.BetterBooks;
import com.gmail.vkhanh234.BetterBooks.Config.FirstJoinList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class OnJoinListener
implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (BetterBooks.getPlugin().getFirstJoinList().checkPlayer(p)) {
            for (String s : BetterBooks.getPlugin().getFc().getStringList("onFirstJoin.books")) {
                p.getInventory().addItem(new ItemStack[]{BetterBooks.getBook(p, s)});
            }
            BetterBooks.getPlugin().getFirstJoinList().addPlayer(p);
        }
    }
}

