/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringEscapeUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.BookMeta
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.gmail.vkhanh234.BetterBooks.Config;

import com.gmail.vkhanh234.BetterBooks.BetterBooks;
import com.gmail.vkhanh234.BetterBooks.KUtils;

import mkremins.fanciful.FancyMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class BookConfig {
    private FileConfiguration config;
    private File configFile;
    private String name;
    private String author;
    private String title;
    private List<String> lores = new ArrayList<String>();
    private List<String> plainPages = new ArrayList<String>();

    public BookConfig(String file) {
        this.configFile = new File(BetterBooks.getPlugin().getDataFolder(), "books/" + file);
        this.config = YamlConfiguration.loadConfiguration((File)this.configFile);
        this.load();
    }

    public static boolean createBook(ItemStack item, String s) {
        File configFile = new File(BetterBooks.getPlugin().getDataFolder(), "books/" + s + ".yml");
        if (configFile.exists()) {
            return false;
        }
        try {
            configFile.createNewFile();
            YamlConfiguration config = YamlConfiguration.loadConfiguration((File)configFile);
            BookMeta meta = (BookMeta)item.getItemMeta();
            config.set("Name", (Object)meta.getDisplayName());
            config.set("Author", (Object)meta.getAuthor());
            config.set("Title", (Object)meta.getTitle());
            if (meta.hasLore()) {
                config.set("Lores", (Object)meta.getLore());
            }
            int i = 1;
            Iterator iterator = meta.getPages().iterator();
            while (iterator.hasNext()) {
                String page = (String)iterator.next();
                page = page.replace("\u00a7", "&");
                String[] lines = page.split("\n");
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(lines));
                config.set("Content." + i, list);
                ++i;
            }
            config.save(configFile);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void load() {
        this.name = this.config.contains("Name") ? KUtils.convertColor(this.config.getString("Name")) : null;
        this.author = KUtils.convertColor(this.config.getString("Author"));
        this.title = KUtils.convertColor(this.config.getString("Title"));
        List<String> subLores = this.config.getStringList("Lores");
        for (String s : subLores) {
            this.lores.add(KUtils.convertColor(s));
        }
        int i = 1;
        while (this.config.contains("Content." + i)) {
            String k = "Content." + i;
            List<String> list = this.config.getStringList(k);
            String page = "";
            for (int j = 0; j < list.size(); ++j) {
                String line = (String)list.get(j);
                line = BetterBooks.getPlugin().getTemplateList().addTemplate(line);
                line = line.replace("<br>", "\n");
                line = this.parseColorCode(line);
                page = page + "\n" + line;
            }
            this.plainPages.add(page);
            ++i;
        }
    }

    private String parseColorCode(String page) {
        String[] spl = page.split("&");
        String t = spl[0];
        ArrayList<String> closing = new ArrayList<String>();
        for (int i = 1; i < spl.length; ++i) {
            String key = spl[i].substring(0, 1);
            if (!key.matches("[0-9a-fk-o]")) continue;
            String cl = "";
            if (key.matches("[0-9a-f]")) {
                for (int j = 0; j < closing.size(); ++j) {
                    cl = cl + (String)closing.get(j);
                }
                closing.clear();
            }
            if (key.matches("[0-9]")) {
                key = "cl" + key;
            }
            t = t + cl + "<" + key + ">" + spl[i].substring(1);
            closing.add("</" + key + ">");
        }
        for (int j = 0; j < closing.size(); ++j) {
            t = t + (String)closing.get(j);
        }
        return t;
    }

    public List<FancyMessage> getPages(Player p) {
        ArrayList<FancyMessage> fancyMessages = new ArrayList<mkremins.fanciful.FancyMessage>();
        boolean i = true;
        for (String s : this.plainPages) {
            FancyMessage m = new FancyMessage("");
            m = this.initText(m, BetterBooks.replacePlaceholder(p, s));
            fancyMessages.add(m);
        }
        return fancyMessages;
    }

    public ItemStack getBook(Player p) {
        ItemStack book = BetterBooks.getPlugin().getNms().convertBook(this, p);
        BookMeta meta = (BookMeta)book.getItemMeta();
        if (this.name != null) {
            meta.setDisplayName(this.name);
        }
        meta.setAuthor(this.author);
        meta.setTitle(this.title);
        meta.setLore(this.lores);
        book.setItemMeta((ItemMeta)meta);
        return book;
    }

    private FancyMessage initText(FancyMessage m, String s) {
        Document doc = Jsoup.parse(s);
        doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
        Element e = doc.body();
        Stack<BookObject> format = new Stack<BookObject>();
        this.parseText(m, e, format);
        return m;
    }

    private void parseText(FancyMessage m, Node e, Stack<BookObject> format) {
        List<Node> list = e.childNodes();
        for (Node element : list) {
            format.push(new BookObject(element.nodeName(), element.attributes()));
            if (element.childNodes().size() >= 1) {
                this.parseText(m, element, format);
            } else if (!element.outerHtml().matches("<.*></.*>")) {
                m.then(StringEscapeUtils.unescapeHtml((String)element.outerHtml()));
                for (BookObject b : format) {
                    if (this.applyStyle(m, b.name)) continue;
                    this.applyEvent(m, b);
                }
            }
            format.pop();
        }
    }

    public void applyEvent(FancyMessage m, BookObject b) {
    	String test = b.attr.get("value");
        if (b.name.equals("hover")) {
            switch (b.attr.get("type")) {
                case "text": {
                    m.formattedTooltip(this.initText(new FancyMessage(""), b.attr.get("value")));
                    break;
                }
                case "achievement": {
                    m.achievementTooltip(b.attr.get("value"));
                    break;
                }
             /*   case "item": {
                	m.tooltip(text)
                    //m.itemTooltip(this.parseItem(b.attr.get("value")));
                }*/
            }
        } else if (b.name.equals("click")) {
            switch (b.attr.get("type")) {
                case "suggest": {
                    m.suggest(b.attr.get("value"));
                    break;
                }
                case "command": {
                    m.command(b.attr.get("value"));
                    break;
                }
                case "link": {
                    m.link(b.attr.get("value"));
                    break;
                }
              /*  case "changepage": {
                    m.changePage(b.attr.get("value"));
                }*/
            }
        }
    }

    public ItemStack parseItem(String t) {
        String[] spl;
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> lores = new ArrayList<String>();
        HashMap<Enchantment, Integer> enchant = new HashMap<Enchantment, Integer>();
        for (String s : spl = t.split(" ")) {
            String[] spl2 = s.split("\\:");
            if (spl2[0].equals("id")) {
                item.setType(Material.getMaterial((int)Integer.parseInt(spl2[1])));
                if (spl2.length != 3) continue;
                item.setDurability(Short.valueOf(spl2[2]).shortValue());
                continue;
            }
            if (spl2[0].equals("name")) {
                meta.setDisplayName(KUtils.convertColor(KUtils.addSpace(spl2[1])));
                continue;
            }
            if (spl2[0].equals("lore")) {
                lores.add(KUtils.convertColor(KUtils.addSpace(spl2[1])));
                continue;
            }
            if (spl2[0].equals("enchant")) {
                enchant.put(Enchantment.getByName((String)spl2[1]), Integer.parseInt(spl2[2]));
                continue;
            }
            if (!spl2[0].equals("amount")) continue;
            item.setAmount(Integer.parseInt(spl2[1]));
        }
        if (lores.size() > 0) {
            meta.setLore(lores);
        }
        item.setItemMeta(meta);
        item.addUnsafeEnchantments(enchant);
        return item;
    }

    private boolean applyStyle(FancyMessage m, String s) {
        if (s.matches("[a-f]")) {
            m.color(ChatColor.getByChar((String)s));
            return true;
        }
        if (s.matches("[k-o]")) {
            m.style(ChatColor.getByChar((String)s));
            return true;
        }
        if (s.length() == 3 && s.startsWith("cl")) {
            m.color(ChatColor.getByChar((String)s.substring(2)));
            return true;
        }
        return false;
    }

    public FileConfiguration get() {
        return this.config;
    }

    public String getName() {
        return this.name;
    }

    public class BookObject {
        Attributes attr;
        String name;

        public BookObject(String name, Attributes attr) {
            this.attr = attr;
            this.name = name;
        }
    }

}

