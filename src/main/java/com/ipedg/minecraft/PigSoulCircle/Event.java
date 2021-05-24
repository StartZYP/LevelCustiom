package com.ipedg.minecraft.PigSoulCircle;


import eos.moe.dragoncore.listener.PlayerJoinListener;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.List;

public class Event implements Listener {

    @EventHandler
    public void PlayerItemHeldEvent(PlayerItemHeldEvent event) {
        Utils.ClearPlayerSoulKey(event.getPlayer().getUniqueId());
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item==null){
            return;
        }
        if (item.getItemMeta()==null){
            return;
        }
        if (!item.getItemMeta().hasLore()){
            return;
        }
        List<String> lores = item.getItemMeta().getLore();
//        new HoopHandle(lore,event.getPlayer().getUniqueId());
        Location location = new Location(event.getPlayer().getWorld(), 0, 0, 0);
        List<HoopThread> collect = new ArrayList<>();
        int i= 0;
        for (String lore:
                lores) {
            for (Hoop h:
                    main.confighoop) {
                if (lore.contains(h.getLore())){
                    i+=1;
                    Float aFloat = main.HoopValue.get(i);
                    HoopThread hoopThread = new HoopThread(event.getPlayer().getUniqueId() + "_PigSoulCircle_" + i, h, event.getPlayer().getUniqueId(), location, aFloat, i);

                    collect.add(hoopThread);
                }
            }
        }

        main.SoulCircle.put(event.getPlayer().getUniqueId(),collect);

    }
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event){
        main.PlayerOpenClose.put(event.getPlayer().getUniqueId(), true);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event){
        Utils.ClearPlayerSoulKey(event.getPlayer().getUniqueId());
        main.PlayerOpenClose.remove(event.getPlayer().getUniqueId());
    }


}
