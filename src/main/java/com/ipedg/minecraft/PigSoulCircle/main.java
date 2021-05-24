package com.ipedg.minecraft.PigSoulCircle;

import eos.moe.dragoncore.api.CoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class main extends JavaPlugin {
    public static HashMap<Integer,Float> HoopValue = new HashMap<>();
    public static HashMap<UUID, Boolean>  PlayerOpenClose = new HashMap<>();
    public static HashMap<UUID, List<HoopThread>>  SoulCircle = new HashMap<>();
    public static List<Hoop> confighoop = new ArrayList<>();
    public static FileConfiguration config;


    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(),"config.yml");
        if (!file.exists()){
            saveDefaultConfig();
        }
        plugin =this;
        config = getConfig();
        Bukkit.getServer().getPluginManager().registerEvents(new Event(),this);
        ReloadConfig();
        getLogger().info(
                " __  __                          ____                      ___             \n" +
                        "/\\ \\/\\ \\                        /\\  _`\\    __             /\\_ \\            \n" +
                        "\\ \\ \\_\\ \\    ___     ___   _____\\ \\ \\/\\_\\ /\\_\\  _ __   ___\\//\\ \\      __   \n" +
                        " \\ \\  _  \\  / __`\\  / __`\\/\\ '__`\\ \\ \\/_/_\\/\\ \\/\\`'__\\/'___\\\\ \\ \\   /'__`\\ \n" +
                        "  \\ \\ \\ \\ \\/\\ \\L\\ \\/\\ \\L\\ \\ \\ \\L\\ \\ \\ \\L\\ \\\\ \\ \\ \\ \\//\\ \\__/ \\_\\ \\_/\\  __/ \n" +
                        "   \\ \\_\\ \\_\\ \\____/\\ \\____/\\ \\ ,__/\\ \\____/ \\ \\_\\ \\_\\\\ \\____\\/\\____\\ \\____\\\n" +
                        "    \\/_/\\/_/\\/___/  \\/___/  \\ \\ \\/  \\/___/   \\/_/\\/_/ \\/____/\\/____/\\/____/\n" +
                        "                             \\ \\_\\                                         \n" +
                        "                              \\/_/     猪猪侠在线定制:410120288"
        );


        super.onEnable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player&&args.length==1){
            if (args[0].equalsIgnoreCase("reload")&&sender.isOp()){
                ReloadConfig();
                sender.sendMessage("§c§l重载成功");
            }else if (args[0].equalsIgnoreCase("change")){
                Boolean aBoolean = PlayerOpenClose.get(((Player) sender).getUniqueId());
                if (aBoolean){
                    PlayerOpenClose.put(((Player) sender).getUniqueId(),false);
                    sender.sendMessage("§e§l已关闭自我魂环显示");
                }else {
                    PlayerOpenClose.put(((Player) sender).getUniqueId(),true);
                    sender.sendMessage("§e§l已开启自我魂环显示");
                }
            }
        }

        return super.onCommand(sender, command, label, args);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


    private void ReloadConfig() {
        confighoop.clear();
        reloadConfig();
        Set<String> mines = getConfig().getConfigurationSection("SoulHoop").getKeys(false);
        for (String temp : mines) {
            confighoop.add(new Hoop(getConfig().getString("SoulHoop."+temp+".texture"),getConfig().getString("SoulHoop."+temp+".lore").replace("&","§")));
        }

        Set<String> hoopvalue = getConfig().getConfigurationSection("HoopValue").getKeys(false);
        int i=0;
        for (String temp : hoopvalue) {
            i+=1;
            HoopValue.put(i,Float.valueOf(getConfig().getString("HoopValue."+temp)));
        }


        Config.fps = 2.3d/getConfig().getDouble("fps");
        Config.Positionx = getConfig().getDouble("Position.x");
        Config.Positiony = getConfig().getDouble("Position.y");
        Config.Positionz = getConfig().getDouble("Position.z");
        Config.rotatex = getConfig().getDouble("Position.rotatex.x");
        Config.rotatey = getConfig().getDouble("Position.rotatex.y");
        Config.rotatez = getConfig().getDouble("Position.rotatex.z");


    }
}
