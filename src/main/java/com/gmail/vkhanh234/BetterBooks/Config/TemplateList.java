/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package com.gmail.vkhanh234.BetterBooks.Config;

import com.gmail.vkhanh234.BetterBooks.BetterBooks;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.YamlConfiguration;

public class TemplateList {
    private HashMap<String, String> map = new HashMap();

    public TemplateList() {
        File folder = new File(BetterBooks.getPlugin().getDataFolder(), "templates");
        if (!folder.exists()) {
            folder.mkdir();
            File exampleFile = new File(folder, "stats.yml");
            //YamlConfiguration example = YamlConfiguration.loadConfiguration((InputStream)BetterBooks.getPlugin().getResource("stats.yml"));
            YamlConfiguration example = YamlConfiguration.loadConfiguration(exampleFile);
            try {
                example.save(exampleFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; ++i) {
            String name = listOfFiles[i].getName();
            if (!listOfFiles[i].isFile() || !name.endsWith(".yml")) continue;
            YamlConfiguration temp = YamlConfiguration.loadConfiguration((File)new File(folder, name));
            for (String k : temp.getKeys(false)) {
                List l = temp.getStringList(k);
                this.map.put(k, this.wrapText(l));
            }
        }
    }

    private String wrapText(List<String> l) {
        String s = l.get(0);
        for (int i = 1; i < l.size(); ++i) {
            s = s + "\n" + l.get(i);
        }
        return s;
    }

    public String addTemplate(String page) {
        Pattern pattern = Pattern.compile("\\{(.*?)}");
        Matcher matcher = pattern.matcher(page);
        while (matcher.find()) {
            String temp = matcher.group(0);
            page = page.replace(temp, this.getTempalte(temp.substring(1, temp.length() - 1)));
        }
        return page;
    }

    public String getTempalte(String s) {
        if (this.map.containsKey(s)) {
            return this.map.get(s);
        }
        return "";
    }
}

