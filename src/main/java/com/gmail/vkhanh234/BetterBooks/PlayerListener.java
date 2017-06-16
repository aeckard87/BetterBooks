/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.gmail.vkhanh234.BetterBooks;

import com.gmail.vkhanh234.BetterBooks.BetterBooks;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener
implements Listener {
    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (!BetterBooks.getPlugin().getFc().getBoolean("renewOnUse")) {
            return;
        }
        if (e.getAction().equals((Object)Action.RIGHT_CLICK_AIR) || e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();
            if (e.getItem() != null && !e.getItem().getType().equals((Object)Material.AIR) && e.getItem().getType().equals((Object)Material.WRITTEN_BOOK) && e.getItem().getItemMeta().hasDisplayName()) {
                ItemStack book = BetterBooks.getBookByName(p, e.getItem().getItemMeta().getDisplayName());
                if (book == null) {
                    return;
                }
                book.setAmount(e.getItem().getAmount());
                p.getInventory().setItemInHand(book);
            }
        }
    }
}

