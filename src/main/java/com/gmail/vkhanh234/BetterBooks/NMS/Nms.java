/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.gmail.vkhanh234.BetterBooks.NMS;

import com.gmail.vkhanh234.BetterBooks.Config.BookConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Nms {
    public ItemStack convertBook(BookConfig var1, Player var2);

    public void loadBook(ItemStack var1);
}

