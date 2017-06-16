/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_10_R1.ItemStack
 *  net.minecraft.server.v1_10_R1.NBTBase
 *  net.minecraft.server.v1_10_R1.NBTTagCompound
 *  net.minecraft.server.v1_10_R1.NBTTagList
 *  net.minecraft.server.v1_10_R1.NBTTagString
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.gmail.vkhanh234.BetterBooks.NMS;

import com.gmail.vkhanh234.BetterBooks.Config.BookConfig;
import com.gmail.vkhanh234.BetterBooks.NMS.Nms;

import mkremins.fanciful.FancyMessage;

import java.util.List;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Nms_v1_12_R1
implements Nms {
    public ItemStack convertBook(BookConfig book, Player p) {
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy((ItemStack)new ItemStack(Material.WRITTEN_BOOK));
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList pages = new NBTTagList();
        for (FancyMessage m : book.getPages(p)) {
            pages.add((NBTBase)new NBTTagString(m.toJSONString()));
        }
        tag.set("pages", (NBTBase)pages);
        stack.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack)stack);
    }

    public void loadBook(ItemStack item) {
    }
}

