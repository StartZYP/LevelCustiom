package com.ipedg.minecraft.PigSoulCircle;

import java.util.Objects;

public class Hoop {
    private final String texture;
    private final String lore;

    public Hoop(String texture, String lore) {
        this.texture = texture;
        this.lore = lore;
    }


    public String getTexture() {
        return texture;
    }

    public String getLore() {
        return lore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hoop hoop = (Hoop) o;
        return Objects.equals(texture, hoop.texture) &&
                Objects.equals(lore, hoop.lore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, lore);
    }

    @Override
    public String toString() {
        return "Hoop{" +
                "texture='" + texture + '\'' +
                ", lore='" + lore + '\'' +
                '}';
    }
}
