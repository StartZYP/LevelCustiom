//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package bc.dgad.listener;

import bc.dgad.eclass.CConfig;
import bc.dgad.eclass.Clang;
import bc.dgad.eclass.GetInventory;
import bc.dgad.eclass.PlayerInvLevel;
import bc.dgad.main.Dgad;
//import com.bekvon.bukkit.residence.Residence;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MListener implements Listener {
    public MListener() {
    }

    @EventHandler
    public void onPlayerClickInvByMouse(InventoryClickEvent e) throws IOException {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player)e.getWhoClicked();
            String fs = File.separator;
            File pfi = new File(Dgad.FilePath + fs + "Players", p.getName() + ".yml");
            FileConfiguration pf = YamlConfiguration.loadConfiguration(pfi);
            if (e.getInventory().getTitle().equalsIgnoreCase(CConfig.guititle)) {
                int slot = e.getRawSlot();
                if (slot < 54) {
                    e.setCancelled(true);
                    int level;
                    int playerlevel;
                    if (slot == CConfig.bu_down) {
                        level = 1;

                        for(playerlevel = 0; playerlevel < Dgad.playerinvlevel.size(); ++playerlevel) {
                            if (((PlayerInvLevel)Dgad.playerinvlevel.get(playerlevel)).getPlayer().equals(p)) {
                                level = ((PlayerInvLevel)Dgad.playerinvlevel.get(playerlevel)).getlevel() + 1;
                                ((PlayerInvLevel)Dgad.playerinvlevel.get(playerlevel)).setlevel(level);
                            }
                        }

                        p.closeInventory();
                        p.openInventory(GetInventory.getInvByPlayer(GetInventory.getInvByYml(Dgad.gui), Dgad.award, Dgad.aitem, p, level, Dgad.vipaward, Dgad.vipaitem));
                        p.updateInventory();
                    } else if (slot == CConfig.bu_up) {
                        level = 1;

                        for(playerlevel = 0; playerlevel < Dgad.playerinvlevel.size(); ++playerlevel) {
                            if (((PlayerInvLevel)Dgad.playerinvlevel.get(playerlevel)).getPlayer().equals(p) && ((PlayerInvLevel)Dgad.playerinvlevel.get(playerlevel)).getlevel() > 1) {
                                level = ((PlayerInvLevel)Dgad.playerinvlevel.get(playerlevel)).getlevel() - 1;
                                ((PlayerInvLevel)Dgad.playerinvlevel.get(playerlevel)).setlevel(level);
                            }
                        }

                        p.closeInventory();
                        p.openInventory(GetInventory.getInvByPlayer(GetInventory.getInvByYml(Dgad.gui), Dgad.award, Dgad.aitem, p, level, Dgad.vipaward, Dgad.vipaitem));
                        p.updateInventory();
                    } else if (slot == CConfig.bu_get) {
                        boolean iget = false;
                        playerlevel = pf.getInt("Level");
                        int io = 0;
                        String reason = "§4无奖励可领取";

                        for(int i = 1; i <= CConfig.maxaward; ++i) {
                            List cmds;
                            String s;
                            Iterator var14;
                            if (Dgad.award.get("AwardLevel." + i + ".Point") != null && Dgad.award.getBoolean("AwardLevel." + i + ".Open") && pf.getInt("Point") >= Dgad.award.getInt("AwardLevel." + i + ".Point") && !pf.getString("Got").contains(i + ",")) {
                                pf.set("Got", pf.getString("Got") + i + ",");
                                if (playerlevel < i) {
                                    playerlevel = i;
                                }

                                iget = true;
                                ++io;
                                cmds = Dgad.award.getStringList("AwardLevel." + i + ".Command");
                                var14 = cmds.iterator();

                                while(var14.hasNext()) {
                                    s = (String)var14.next();
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("%player%", p.getName()));
                                }
                            }

                            if (pf.getBoolean("Vip") && Dgad.vipaward.get("AwardLevel." + i + ".Point") != null && Dgad.vipaward.getBoolean("AwardLevel." + i + ".Open") && pf.getInt("Point") >= Dgad.vipaward.getInt("AwardLevel." + i + ".Point") && !pf.getString("VipGot").contains(i + ",")) {
                                pf.set("VipGot", pf.getString("VipGot") + i + ",");
                                if (playerlevel < i) {
                                    playerlevel = i;
                                }

                                iget = true;
                                ++io;
                                cmds = Dgad.vipaward.getStringList("AwardLevel." + i + ".Command");
                                var14 = cmds.iterator();

                                while(var14.hasNext()) {
                                    s = (String)var14.next();
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("%player%", p.getName()));
                                }
                            }
                        }

                        pf.set("Level", playerlevel);
                        pf.save(pfi);
                        if (iget) {
                            p.sendMessage(Clang.title + Clang.award_get.replaceAll("%this_get%", String.valueOf(io)).replaceAll("%count_get%", String.valueOf(pf.getString("Got").split(",").length - 1 + (pf.getString("VipGot").split(",").length - 1))));
                        } else {
                            p.sendMessage(Clang.title + Clang.award_fail.replaceAll("%reason%", reason));
                        }
                    }

                    p.updateInventory();
                }
            }
        }
    }




    @EventHandler
    public void PlayerJoinGame(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String fs = File.separator;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        File pfi = new File(Dgad.FilePath + fs + "Players", p.getName() + ".yml");
        FileConfiguration pf = YamlConfiguration.loadConfiguration(pfi);
        File fileParents = pfi.getParentFile();
        if (!fileParents.exists()) {
            fileParents.mkdirs();
        }

        if (!pfi.exists()) {
            try {
                pfi.createNewFile();
                pf = YamlConfiguration.loadConfiguration(pfi);
                pf.set("Vip", false);
                pf.set("Got", "0,");
                pf.set("VipGot", "0,");
                pf.set("Point", 0);
                pf.set("Level", 0);
                pf.set("Damage", 0);
                pf.set("D", "xxxx-xx-xx");
                pf.set("DP", 0);
                pf.save(pfi);
            } catch (IOException var11) {
                p.sendMessage(Clang.title + Clang.plu_error);
                System.out.println(Clang.title + "§4错误,MListener- PlayerDataFile Create Error:");
                System.out.println("Error: " + var11.getMessage());
            }
        }

        if (!pf.getString("D").equalsIgnoreCase(df.format(new Date()))) {
            pf.set("D", df.format(new Date()));
            pf.set("DP", 0);

            try {
                pf.save(pfi);
            } catch (IOException var10) {
                p.sendMessage(Clang.title + Clang.plu_error);
                System.out.println(Clang.title + "§4错误,MListener- PlayerDataFile Save-D Error:");
                System.out.println("Error: " + var10.getMessage());
            }
        }

        if (pf.getString("Clear") == null) {
            pf.set("Clear", "xxx");
        }

        if (Dgad.config.getString("Clear") == null) {
            Dgad.config.set("Clear", "xxx");
        }

        if (!pf.getString("Clear").equalsIgnoreCase(Dgad.config.getString("Clear"))) {
            pf.set("Vip", false);
            pf.set("Got", "0,");
            pf.set("VipGot", "0,");
            pf.set("Point", 0);
            pf.set("Level", 0);
            pf.set("Damage", 0);
            pf.set("D", "xxxx-xx-xx");
            pf.set("Clear", df.format(new Date()));
            pf.set("DP", 0);

            try {
                pf.save(pfi);
                p.sendMessage(Clang.title + Clang.clear);
            } catch (IOException var9) {
                p.sendMessage(Clang.title + Clang.plu_error);
                System.out.println(Clang.title + "§4错误,MListener- PlayerDataFile Save-D Error:");
                System.out.println("Error: " + var9.getMessage());
            }
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void PlayerDamagedEvent(EntityDamageByEntityEvent e) {
        CraftEntity es = (CraftEntity)e.getEntity();

        if (es.getHandle().getBukkitEntity().getName() != null) {
            String name = es.getHandle().getBukkitEntity().getName();

            for(int i = 0; i < CConfig.blacknpclist.size(); ++i) {
                if (name.contains((CharSequence)CConfig.blacknpclist.get(i))) {
                    return;
                }
            }
        }

        Player p;
//        if (CConfig.on_res) {
//            try {
//                if (!(e.getDamager() instanceof Player)) {
//                    return;
//                }
//
//                p = (Player)e.getDamager();
//                if (Residence.getResidenceManager().getByLoc(p.getLocation()) != null && !CConfig.reslist.contains(Residence.getResidenceManager().getByLoc(p.getLocation()).getName())) {
//                    return;
//                }
//            } catch (Exception var12) {
//                if (CConfig.log_res) {
//                    System.out.println("Res领地插件未找到，取消支持:" + var12.getMessage());
//                }
//            }
//        }

        if (CConfig.perpoint != -1) {
            if (e.getDamager() instanceof Player) {
                p = (Player)e.getDamager();
                String fs = File.separator;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                File pfi = new File(Dgad.FilePath + fs + "Players", p.getName() + ".yml");
                FileConfiguration pf = YamlConfiguration.loadConfiguration(pfi);
                if (pf.getString("D").equalsIgnoreCase(df.format(new Date())) && pf.getInt("DP") >= CConfig.day_point) {
                    return;
                }

                if (!pf.getString("D").equalsIgnoreCase(df.format(new Date()))) {
                    pf.set("D", df.format(new Date()));
                    pf.set("DP", 0);

                    try {
                        pf.save(pfi);
                    } catch (IOException var11) {
                        var11.printStackTrace();
                    }
                }

                DecimalFormat dfs = new DecimalFormat("#.00");
                if (Double.parseDouble(pf.getString("Damage")) + e.getDamage() >= (double)CConfig.perpoint) {
                    pf.set("Point", pf.getInt("Point") + 1);
                    pf.set("DP", pf.getInt("DP") + 1);
                    pf.set("D", df.format(new Date()));
                    pf.set("Damage", 0);
                    p.sendMessage(Clang.title + Clang.award_up.replaceAll("%point%", pf.getString("Point")));
                } else {
                    pf.set("Damage", dfs.format(Double.parseDouble(pf.getString("Damage")) + e.getDamage()));
                }

                try {
                    pf.save(pfi);
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }

        }
    }

    @EventHandler
    public void PlayerFishing(PlayerFishEvent e) {
        if (CConfig.perfish != -1) {
            if (e.getCaught() instanceof Fish) {
                if (e.getCaught() != null) {
                    Player p = e.getPlayer();
                    String fs = File.separator;
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    File pfi = new File(Dgad.FilePath + fs + "Players", p.getName() + ".yml");
                    FileConfiguration pf = YamlConfiguration.loadConfiguration(pfi);
                    if (pf.getString("D").equalsIgnoreCase(df.format(new Date())) && pf.getInt("DP") >= CConfig.day_point) {
                        return;
                    }

                    if (!pf.getString("D").equalsIgnoreCase(df.format(new Date()))) {
                        pf.set("D", df.format(new Date()));
                        pf.set("DP", 0);

                        try {
                            pf.save(pfi);
                        } catch (IOException var9) {
                            var9.printStackTrace();
                        }
                    }

                    if (pf.getInt("Fish") + 1 >= CConfig.perfish) {
                        pf.set("Point", pf.getInt("Point") + 1);
                        pf.set("DP", pf.getInt("DP") + 1);
                        pf.set("D", df.format(new Date()));
                        pf.set("Fish", 0);
                        p.sendMessage(Clang.title + Clang.award_up.replaceAll("%point%", pf.getString("Point")));
                    } else {
                        pf.set("Fish", pf.getDouble("Fish") + 1.0D);
                    }

                    try {
                        pf.save(pfi);
                    } catch (IOException var8) {
                        var8.printStackTrace();
                    }
                }

            }
        }
    }
}
