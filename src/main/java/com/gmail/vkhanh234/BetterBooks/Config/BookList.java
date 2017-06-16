/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package com.gmail.vkhanh234.BetterBooks.Config;

import com.gmail.vkhanh234.BetterBooks.BetterBooks;
import com.gmail.vkhanh234.BetterBooks.Config.BookConfig;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;

public class BookList {
    HashMap<String, BookConfig> books = new HashMap();
    HashMap<String, BookConfig> booksName = new HashMap();

    public BookList() {
        File folder = new File(BetterBooks.getPlugin().getDataFolder(), "books");
        if (!folder.exists()) {
            folder.mkdir();
            File exampleFile = new File(folder, "example.yml");
            //YamlConfiguration example = YamlConfiguration.loadConfiguration((InputStream)BetterBooks.getPlugin().getResource("example.yml"));
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
            BookConfig bc = new BookConfig(name);
            this.books.put(name.substring(0, name.length() - 4), bc);
            this.booksName.put(bc.getName(), bc);
        }
    }

    public BookConfig getBookConfig(String s) {
        if (this.books.containsKey(s)) {
            return this.books.get(s);
        }
        return null;
    }

    public BookConfig getBookConfigByName(String s) {
        if (this.booksName.containsKey(s)) {
            return this.booksName.get(s);
        }
        return null;
    }

    public Set<String> getList() {
        return this.books.keySet();
    }
}

