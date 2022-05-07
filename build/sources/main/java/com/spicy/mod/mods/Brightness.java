package com.spicy.mod.mods;

import com.spicy.events.TickEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import pisi.unitedmeows.eventapi.event.listener.Listener;


public class Brightness extends Mod {


    public Brightness() {
        super("Brightness", "Infinity night vision", 0, Category.RENDER);
    }

    public Listener<TickEvent> tickListener = new Listener<TickEvent>(event -> {
        getMinecraft().thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
    }).filter(f -> getMinecraft().thePlayer != null);
}
