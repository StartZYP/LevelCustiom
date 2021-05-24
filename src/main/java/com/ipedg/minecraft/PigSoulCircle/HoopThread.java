package com.ipedg.minecraft.PigSoulCircle;

import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class HoopThread extends BukkitRunnable {

    Hoop hoop;
    UUID playeruuid;
    Location location;
    float Multiply;
    String key;


    HoopThread(String key,Hoop hoop,UUID playeruuid,Location location,double Multiply,long delay){
        this.hoop = hoop;
        this.playeruuid = playeruuid;
        this.location = location;
        this.Multiply = (float) Multiply;
        this.key = key;
        this.runTaskTimer(main.plugin, 20L*delay, 5L);
    }

    float i = 2.3f;
    @Override
    public void run() {
        i-=Config.fps;
        if (i<=0.2){
            this.cancel();
        }
        for (Player p:Bukkit.getOnlinePlayers()){
            if (p.getUniqueId().equals(playeruuid)){
                if (main.PlayerOpenClose.get(playeruuid)){
                    PacketSender.setPlayerWorldTexture(p,key,location,90f,0f,0f,hoop.getTexture(),Multiply,Multiply,1.0f,false,false,playeruuid,false,0,i,0);
                }else {
                    continue;
                }
            }
            PacketSender.setPlayerWorldTexture(p,key,location,90f,0f,0f,hoop.getTexture(),Multiply,Multiply,1.0f,false,false,playeruuid,false,0,i,0);
        }

    }


}
