package com.spicy.user;

import com.spicy.Spicy;
import com.spicy.irc.Myself;
import com.spicy.utils.AnimatedResourceLocation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

public class User {
    @Getter @Setter private String name;

    @Getter private String hat;
    @Getter private String bandana;
    @Getter private String wing;
    @Getter private String halo;

    private ResourceLocation haloRes;
    private ResourceLocation hatRes;
    private ResourceLocation bandanaRes;
    private ResourceLocation wingRes;

    public User(String _name, String _hat, String _bandana, String _wing, String _halo) {
        name = _name;
        setHat(_hat);
        setBandana(_bandana);
        setWing(_wing);
        setHalo(_halo);
    }


    public void setHalo(String halo) {
        this.halo = halo;
        if (halo.equalsIgnoreCase("reset")) {
            haloRes = null;
            return;
        }
        ResourceLocation haloInstance = Spicy.getINSTANCE().cosmeticTextures.getHalo(halo);
        haloRes = haloInstance;
    }

    public void setHat(String hat) {
        this.hat = hat;
        if (hat.equalsIgnoreCase("reset")) {
            hatRes = null;
            return;
        }
        ResourceLocation hatInstance = Spicy.getINSTANCE().cosmeticTextures.getHat(hat);
        hatRes = hatInstance;
    }

    public void setWing(String wing) {
        this.wing = wing;
        if (wing.equalsIgnoreCase("reset")) {
            wingRes = null;
            return;
        }
        ResourceLocation wingInstance = Spicy.getINSTANCE().cosmeticTextures.getWing(wing);
        wingRes = wingInstance;
    }

    public void setBandana(String bandana) {
        this.bandana = bandana;
        if (bandana.equalsIgnoreCase("reset")) {
            bandanaRes = null;
            return;
        }
        ResourceLocation bandanaInstance = Spicy.getINSTANCE().cosmeticTextures.getBandana(bandana);
        bandanaRes = bandanaInstance;
    }




    public boolean hasHat() { return hat != null && hatRes != null;}
    public boolean hasBandana() {return bandana != null && bandanaRes != null;}
    public boolean hasWing() {return wing != null && wingRes != null;}
    public boolean hasHalo() {return halo != null && haloRes != null;}


    public String getUpdateText() {
        return Myself.START_PREFIX + name + "::" + hat + "::" + bandana + "::" + wing + "::" + halo;
    }

    public boolean canRender(AbstractClientPlayer abstractClientPlayer) {
        return Spicy.getINSTANCE().getUserMap().containsKey(abstractClientPlayer.getNameClear());
    }


}
