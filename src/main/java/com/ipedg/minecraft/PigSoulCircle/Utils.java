package com.ipedg.minecraft.PigSoulCircle;

import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Utils {


    public static void ClearPlayerSoulKey(UUID playeruuid){
        if (!main.SoulCircle.containsKey(playeruuid)){
            return;
        }
        List<HoopThread> hoops = main.SoulCircle.get(playeruuid);
//        System.out.println(hoops.size()+"清理");
        for (Player p:
                Bukkit.getOnlinePlayers()) {
//            for (int i = 0; i < 20; i++) {
//                PacketSender.removePlayerWorldTexture(p,playeruuid+"_PigSoulCircle_"+i);
//            }
            for (HoopThread key:
                    hoops) {
                key.cancel();
                PacketSender.removePlayerWorldTexture(p, key.key);
            }
        }

    }
}
