
package com.gmail.vkhanh234.BetterBooks;

import com.gmail.vkhanh234.BetterBooks.Config.BookConfig;
import com.gmail.vkhanh234.BetterBooks.Config.BookList;
import com.gmail.vkhanh234.BetterBooks.Config.FirstJoinList;
import com.gmail.vkhanh234.BetterBooks.Config.TemplateList;
import com.gmail.vkhanh234.BetterBooks.KUtils;
import com.gmail.vkhanh234.BetterBooks.NMS.Nms;


import me.clip.placeholderapi.*;

import com.gmail.vkhanh234.BetterBooks.OnJoinListener;
import com.gmail.vkhanh234.BetterBooks.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterBooks
extends JavaPlugin {
    public static FileConfiguration fc;
    private static BetterBooks plugin;
    private BookList bookList;
    private FirstJoinList firstJoinList;
    private TemplateList templateList;
    private Nms nms;
    boolean placeHolder = false;

    public void onEnable() {
        plugin = this;
        this.loadConfiguration();
        this.initConfig();
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerListener(), (Plugin)this);
        
        
        if (fc.getBoolean("onFirstJoin.enable")) {
            this.getServer().getPluginManager().registerEvents((Listener)new OnJoinListener(), (Plugin)this);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeHolder = true;
        }
    }

    private void initConfig() {
        fc = this.getConfig();
        this.templateList = new TemplateList();
        this.bookList = new BookList();
        this.firstJoinList = new FirstJoinList();
    }

    public void onDisable() {
    	this.setEnabled(false);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1) {
            if (!this.hasPermission(sender, "command." + args[0])) {
                sender.sendMessage(BetterBooks.getMessage("noPermission"));
                return true;
            }
            if (args[0].equals("reload")) {
                this.reloadConfig();
                this.initConfig();
                sender.sendMessage(BetterBooks.getMessage("reload"));
            } else if (args[0].equals("give")) {
                if (args.length == 3) {
                    Player p = Bukkit.getPlayer((String)args[2]);
                    if (p == null) {
                        sender.sendMessage(BetterBooks.getMessage("noPlayer"));
                        return true;
                    }
                    BookConfig bc = this.bookList.getBookConfig(args[1]);
                    if (bc == null) {
                        sender.sendMessage(BetterBooks.getMessage("wrongBook"));
                        return true;
                    }
                    p.getInventory().addItem(new ItemStack[]{bc.getBook(p)});
                    sender.sendMessage(BetterBooks.getMessage("sendBook").replace("{name}", args[1]).replace("{player}", args[2]));
                    p.sendMessage(BetterBooks.getMessage("receiveBook").replace("{name}", args[1]));
                } else {
                    this.showHelp(sender);
                }
            } else if (args[0].equals("convert") && args.length == 2) {
                if (sender instanceof Player) {
                    Player p = (Player)sender;
                    ItemStack book = p.getItemInHand();
                    if (book.getType().equals((Object)Material.WRITTEN_BOOK)) {
                        if (BookConfig.createBook(p.getItemInHand(), args[1])) {
                            sender.sendMessage(BetterBooks.getMessage("successConvert"));
                            this.bookList = new BookList();
                        } else {
                            sender.sendMessage(BetterBooks.getMessage("bookExist"));
                        }
                    } else {
                        sender.sendMessage(BetterBooks.getMessage("notBook"));
                    }
                } else {
                    sender.sendMessage(BetterBooks.getMessage("onlyPlayer"));
                }
            } else if (args[0].equals("get")) {
                if (sender instanceof Player) {
                    if (args.length == 2) {
                        BookConfig bc = this.bookList.getBookConfig(args[1]);
                        if (bc == null) {
                            sender.sendMessage(BetterBooks.getMessage("wrongBook"));
                            return true;
                        }
                        ((Player)sender).getInventory().addItem(new ItemStack[]{bc.getBook((Player)sender)});
                        sender.sendMessage(BetterBooks.getMessage("receiveBook").replace("{name}", args[1]));
                    }
                } else {
                    sender.sendMessage(BetterBooks.getMessage("onlyPlayer"));
                }
            } else if (args[0].equals("list")) {
                String l = (Object)ChatColor.AQUA + "";
                for (String s : this.bookList.getList()) {
                    l = l + s + " ";
                }
                sender.sendMessage(l);
            } else {
                this.showHelp(sender);
            }
        } else {
            this.showHelp(sender);
        }
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(BetterBooks.getMessage("help"));
        if (this.hasPermission(sender, "command.reload")) {
            sender.sendMessage((Object)ChatColor.GREEN + "/bb reload" + (Object)ChatColor.WHITE + " - Reload config");
        }
        if (this.hasPermission(sender, "command.give")) {
            sender.sendMessage((Object)ChatColor.GREEN + "/bb give <book-id> <player>" + (Object)ChatColor.WHITE + " - Give a book to player");
        }
        if (this.hasPermission(sender, "command.get")) {
            sender.sendMessage((Object)ChatColor.GREEN + "/bb get <book-id>" + (Object)ChatColor.WHITE + " - Get a book");
        }
        if (this.hasPermission(sender, "command.list")) {
            sender.sendMessage((Object)ChatColor.GREEN + "/bb list" + (Object)ChatColor.WHITE + " - List all book id");
        }
        if (this.hasPermission(sender, "command.convert")) {
            sender.sendMessage((Object)ChatColor.GREEN + "/bb convert <id>" + (Object)ChatColor.WHITE + " - Convert the book in your hand to plugin's book");
        }
    }

    public BookList getBookList() {
        return this.bookList;
    }

    public FileConfiguration getFc() {
        return fc;
    }

    private boolean hasPermission(CommandSender sender, String s) {
        if (sender.hasPermission("BetterBooks." + s)) {
            return true;
        }
        return false;
    }

    private void loadConfiguration() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.getConfig().options().copyDefaults(false);
    }

    public static String getMessage(String type) {
        return KUtils.convertColor(fc.getString("Messages." + type));
    }

    public Nms getNms() {
        return this.nms;
    }

    public static String replacePlaceholder(Player p, String s) {
        if (p != null && BetterBooks.plugin.placeHolder) {
            return PlaceholderAPI.setPlaceholders((Player)p, (String)s);
        }
        return s;
    }

    public static ItemStack getBook(Player p, String s) {
        return plugin.getBookList().getBookConfig(s).getBook(p);
    }

    public FirstJoinList getFirstJoinList() {
        return this.firstJoinList;
    }

    public static ItemStack getBookByName(Player p, String s) {
        BookConfig b = plugin.getBookList().getBookConfigByName(s);
        if (b == null) {
            return null;
        }
        return b.getBook(p);
    }

    public static BetterBooks getPlugin() {
        return plugin;
    }

    public TemplateList getTemplateList() {
        return this.templateList;
    }
}

