/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.FireworkEffect$Builder
 *  org.bukkit.FireworkEffect$Type
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Firework
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.FireworkMeta
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.gmail.vkhanh234.BetterBooks;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class KUtils {
    public static String addSpace(String s) {
        return s.replace("-", " ");
    }

    public static int getRandom(String level) {
        if (level.contains("-")) {
            String[] spl = level.split("-");
            return KUtils.randomNumber(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]));
        }
        return Integer.parseInt(level);
    }

    public static boolean isArmor() {
        return false;
    }

    public static boolean isChestplate(Material m) {
        if (m.toString().contains("_CHESTPLATE")) {
            return true;
        }
        return false;
    }

    public static boolean isLeggings(Material m) {
        if (m.toString().contains("_LEGGINGS")) {
            return true;
        }
        return false;
    }

    public static boolean isHelmet(Material m) {
        if (m.toString().contains("_HELMET")) {
            return true;
        }
        return false;
    }

    public static boolean isBoots(Material m) {
        if (m.toString().contains("_BOOTS")) {
            return true;
        }
        return false;
    }

    public static boolean isArmor(Material m) {
        if (KUtils.isHelmet(m)) {
            return true;
        }
        if (KUtils.isChestplate(m)) {
            return true;
        }
        if (KUtils.isLeggings(m)) {
            return true;
        }
        if (KUtils.isBoots(m)) {
            return true;
        }
        return false;
    }

    public static boolean isSword(Material m) {
        if (m.toString().contains("_SWORD")) {
            return true;
        }
        return false;
    }

    public static boolean isShovel(Material m) {
        if (m.toString().contains("_SPADE")) {
            return true;
        }
        return false;
    }

    public static boolean isPickaxe(Material m) {
        if (m.toString().contains("_PICKAXE")) {
            return true;
        }
        return false;
    }

    public static boolean isAxe(Material m) {
        if (m.toString().contains("_AXE")) {
            return true;
        }
        return false;
    }

    public static boolean isHoe(Material m) {
        if (m.toString().contains("_HOE")) {
            return true;
        }
        return false;
    }

    public static boolean isTools(Material m) {
        if (KUtils.isHoe(m)) {
            return true;
        }
        if (KUtils.isAxe(m)) {
            return true;
        }
        if (KUtils.isPickaxe(m)) {
            return true;
        }
        if (KUtils.isShovel(m)) {
            return true;
        }
        return false;
    }

    public static boolean isBow(Material m) {
        if (m.equals((Object)Material.BOW)) {
            return true;
        }
        return false;
    }

    public static int randomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static boolean hasPermmision(Player p, String perm) {
        if (p.hasPermission(perm)) {
            return true;
        }
        if (p.isOp()) {
            return true;
        }
        return false;
    }

    public static boolean isSuccess(int percent) {
        int g = KUtils.randomNumber(1, 100);
        if (g <= percent) {
            return true;
        }
        return false;
    }

    public static void removeItem(ItemStack item, Player p) {
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            item.setType(Material.CLAY_BRICK);
            ItemMeta me = item.getItemMeta();
            me.setDisplayName("Destroyed");
            item.setItemMeta(me);
            p.getInventory().remove(item);
        }
    }

    public static String backColor(String name) {
        return name.replace("\u00a7", "&");
    }

    public static String convertColor(String name) {
        return name.replace("&", "\u00a7");
    }

    public static void spawnFirework(Player p) {
        Firework fw = (Firework)p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        Random r = new Random();
        int rt = r.nextInt(4) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) {
            type = FireworkEffect.Type.BALL;
        }
        if (rt == 2) {
            type = FireworkEffect.Type.BALL_LARGE;
        }
        if (rt == 3) {
            type = FireworkEffect.Type.BURST;
        }
        if (rt == 4) {
            type = FireworkEffect.Type.CREEPER;
        }
        if (rt == 5) {
            type = FireworkEffect.Type.STAR;
        }
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = KUtils.getColor(r1i);
        Color c2 = KUtils.getColor(r2i);
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
        fwm.addEffect(effect);
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);
    }

    public static String RomanNumerals(int Int) {
        LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();
        roman_numerals.put("M", 1000);
        roman_numerals.put("CM", 900);
        roman_numerals.put("D", 500);
        roman_numerals.put("CD", 400);
        roman_numerals.put("C", 100);
        roman_numerals.put("XC", 90);
        roman_numerals.put("L", 50);
        roman_numerals.put("XL", 40);
        roman_numerals.put("X", 10);
        roman_numerals.put("IX", 9);
        roman_numerals.put("V", 5);
        roman_numerals.put("IV", 4);
        roman_numerals.put("I", 1);
        String res = "";
        for (Map.Entry entry : roman_numerals.entrySet()) {
            int matches = Int / (Integer)entry.getValue();
            res = res + KUtils.repeat((String)entry.getKey(), matches);
            Int %= ((Integer)entry.getValue()).intValue();
        }
        return res;
    }

    public static String repeat(String s, int n) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String convertColorSecure(String s) {
        return s.replaceAll("&([0-9a-fk-or])", "\ufffd$1");
    }

    public static Color getColor(int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }
        return c;
    }
}

