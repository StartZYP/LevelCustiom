package com.level.lysg;

import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;
import java.io.*;
import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.configuration.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;

public class Main extends JavaPlugin implements Listener
{
    public static Plugin plugin;
    public static HashMap<String, Integer> level_dj;
    public static HashMap<String, Integer> level_xp;
    public static HashMap<Integer, Integer> level_yq;

    static {
        Main.level_dj = new HashMap<String, Integer>();
        Main.level_xp = new HashMap<String, Integer>();
        Main.level_yq = new HashMap<Integer, Integer>();
    }

    public void onEnable() {
        plugin = this;
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new LevelExpansion().register();
        }

        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this);
        final File file = new File("./plugins/Level", "Cmg.yml");
        if (!file.exists()) {
            this.saveResource("Cmg.yml", true);
        }
        for (int n = 1; n <= getCmg().getInt("level.max"); ++n) {
            Main.level_yq.put(n, getCmg().getInt("level.need." + n));
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListeneres here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            throw new RuntimeException("Could not find PlaceholderAPI!! Plugin can not work without it!");
        }
    }

    public static YamlConfiguration getCmg() {
        final File file = new File("./plugins/Level", "Cmg.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        return yml;
    }

    public static void takeExp(final Player p, final int amount) {
        final File file = new File("./plugins/Level/data", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        Main.level_xp.put(p.getName(), (int)(yml.getDouble("exp") - amount));
        yml.set("exp", (Object)(yml.getDouble("exp") - amount));
        try {
            yml.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setExp(final Player p, final double s) {
        final File file = new File("./plugins/Level/data", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        Main.level_xp.put(p.getName(), (int)(yml.getInt("exp") - s));
        yml.set("exp", (Object)s);
        try {
            yml.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLevel(final Player p, final int amount) {
        final File file = new File("./plugins/Level/data", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        Main.level_dj.put(p.getName(), amount);
        yml.set("level", (Object)amount);
        try {
            yml.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLevel(final Player p, final int amount) {
        final File file = new File("./plugins/Level/data", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        if (yml.getInt("level") + amount == getCmg().getInt("level.max")) {
            yml.set("level", (Object)getCmg().getInt("level.max"));
            try {
                yml.save(file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        Main.level_dj.put(p.getName(), yml.getInt("level") + amount);
        yml.set("level", (Object)(yml.getInt("level") + amount));
        try {
            yml.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addExp(final Player p, final double amount) {
        final File file = new File("./plugins/Level/data", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        Main.level_xp.put(p.getName(), (int)(yml.getInt("exp") + amount));
        if (yml.getInt("level") == getCmg().getInt("level.max")) {
            p.sendMessage(getCmg().getString("level.maxmessage").replace("&", "§"));
            return;
        }
        yml.set("exp", (Object)(yml.getInt("exp") + amount));
        try {
            yml.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        p.sendMessage(getCmg().getString("giveexp").replace("&", "§").replace("%a%", new StringBuilder().append(amount).toString()));
        if (getExp(p) == getNext(p)) {
            setExp(p, 0.0);
            addLevel(p, 1);
            p.sendTitle(getCmg().getString("givelevels"), getCmg().getString("givelevelx"));
        }
        else if (getExp(p) > getNext(p)) {
            final double xp = getExp(p);
            final double next = getNext(p);
            final double s = xp - next;
            setExp(p, s);
            addLevel(p, 1);
            p.sendTitle(getCmg().getString("givelevels").replace("&", "§").replace("%a%", new StringBuilder().append(getLevel(p)).toString()), getCmg().getString("givelevelx").replace("&", "§").replace("%a%", new StringBuilder().append(getLevel(p)).toString()));
        }
    }

    public static double getExp(final Player p) {
        final File file = new File("./plugins/Level/data", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        return yml.getDouble("exp");
    }

    public static int getLevel(final Player p) {
        final File file = new File("./plugins/Level/data", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        return yml.getInt("level");
    }

    public static double getJc(final Player p) {
        final File file = new File("./plugins/Level/data", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        return yml.getDouble("jc");
    }

    public static double getNext(final Player p) {
        final double n = Main.level_yq.get(getLevel(p));
        return n;
    }

    @EventHandler
    public void onj(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final File file = new File("./plugins/Level/data", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            yml.set("exp", (Object)0);
            yml.set("level", (Object)1);
            yml.set("jc", (Object)0);
            try {
                yml.save(file);
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        Main.level_dj.put(p.getName(), yml.getInt("level"));
        Main.level_xp.put(p.getName(), (int)yml.getDouble("exp"));
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("level")) {
            if (args.length == 0) {
                if (sender.isOp()) {
                    sender.sendMessage("level exp [player] [amount]");
                    sender.sendMessage("level level [player] [amount]");
                    sender.sendMessage("level setlevel [player] [amount]");
                    sender.sendMessage("level see");
                }
            }
            else {
                if (args[0].equals("exp")) {
                    final String name = args[1];
                    final int type = Integer.parseInt(args[2]) - 1;
                    final Player p = (Player)sender;
                    addExp(p, type);
                }
                if (args[0].equals("level")) {
                    final String name = args[1];
                    final int type = Integer.parseInt(args[2]) - 1;
                    final Player p = (Player)sender;
                    Main.level_dj.put(p.getName(), getLevel(p) + type);
                    addLevel(p, type);
                    p.sendTitle(getCmg().getString("givelevels").replace("&", "§").replace("%a%", new StringBuilder().append(getLevel(p)).toString()), getCmg().getString("givelevelx").replace("&", "§").replace("%a%", new StringBuilder().append(getLevel(p)).toString()));
                }
                if (args[0].equals("setlevel")) {
                    final String name = args[1];
                    final int type = Integer.parseInt(args[2]) - 1;
                    final Player p = (Player)sender;
                    setLevel(p, type);
                }
                if (args[0].equals("see")) {
                    final Player p2 = (Player)sender;
                    final double yq = Main.level_yq.get(getLevel(p2));
                    p2.sendMessage("§7§m一一一一一一一一一一一一一一一");
                    p2.sendMessage("§c等级: §f" + getLevel(p2));
                    p2.sendMessage("§c经验: §f" + getExp(p2) + "/" + yq);
                    p2.sendMessage("§e经验加成: §f" + getJc(p2));
                    p2.sendMessage("§7§m一一一一一一一一一一一一一一一");
                }
            }
        }
        return true;
    }

    @EventHandler
    public void oned(final EntityDeathEvent e) {
        final Entity en = (Entity)e.getEntity();
        if (en.getCustomName() != null) {
            if (e.getEntity().getKiller() instanceof Player) {
                final Player p = e.getEntity().getKiller();
                final ConfigurationSection name = getCmg().getConfigurationSection("KillMob");
                final Set<String> x = (Set<String>)name.getKeys(false);
                if (e.getEntity().getType() ==EntityType.PLAYER){
                    System.out.println("调试机制触发杀玩家");
                    int exp = getCmg().getInt("PlayerExp");
                    addExp(p, exp);
                }else {
                    System.out.println("调试机制触发杀怪物"+en.getCustomName());
                    for (final String n : x) {
                        if (n.replace("&", "§").equals(en.getCustomName())) {
                            final int exp = getCmg().getInt("KillMob." + n + ".exp");
                            addExp(p, exp);
                        }
                    }
                }

            }
            else if (e.getEntity().getKiller() instanceof Arrow) {
                final Arrow arrow = (Arrow)e.getEntity().getKiller();
                if (arrow.getShooter() instanceof Player) {
                    final ConfigurationSection name = getCmg().getConfigurationSection("KillMob");
                    final Set<String> x = (Set<String>)name.getKeys(false);
                    for (final String n : x) {
                        if (n.replace("&", "§").equals(en.getCustomName())) {
                            final Player p2 = (Player)arrow.getShooter();
                            final int exp2 = getCmg().getInt("KillMob." + n + ".exp");
                            addExp(p2, exp2);
                        }
                    }
                }
            }
        }
    }






}
