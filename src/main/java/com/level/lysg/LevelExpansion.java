package com.level.lysg;
import org.bukkit.Bukkit;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;


public class LevelExpansion extends PlaceholderExpansion {

    /*
       The identifier, shouldn't contain any _ or %
        */
    public String getIdentifier() {
        return "LevelExpansion";
    }

    public String getPlugin() {
        return null;
    }


    /*
     The author of the Placeholder
     This cannot be null
     */
    public String getAuthor() {
        return "Banbeucmas";
    }

    /*
     Same with #getAuthor() but for versioon
     This cannot be null
     */

    public String getVersion() {
        return "SomeMagicalVersion";
    }

    /*
    Use this method to setup placeholders
    This is somewhat similar to EZPlaceholderhook
     */
    public String onPlaceholderRequest(Player player, String identifier) {


        /*
        Check if the player is online,
        You should do this before doing anything regarding players
         */
        if(player == null){
            return "";
        }

        /*
        %tutorial_name%
        Returns the player name
         */
        if(identifier.equalsIgnoreCase("level")){
            return String.valueOf(Main.getLevel(player));
        }
        return null;
    }
}