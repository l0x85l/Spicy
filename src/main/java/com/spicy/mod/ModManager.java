package com.spicy.mod;

import com.spicy.Spicy;
import com.spicy.events.KeyEvent;
import com.spicy.mod.mods.*;
import lombok.Getter;
import pisi.unitedmeows.eventapi.event.listener.Listener;

import java.util.ArrayList;
import java.util.List;

public class ModManager {

     private final List<Mod> mods = new ArrayList<>();


    public ModManager() {
        Spicy.getINSTANCE().eventSystem.subscribe(keyListener, this);
        mods.add(new HUD());
        mods.add(new Speed());
        mods.add(new Sprint());
        mods.add(new FastUse());
        mods.add(new Nametags());
        mods.add(new Velocity());
        mods.add(new KillAura());
        mods.add(new AutoArmor());
        mods.add(new AutoDrink());
        mods.add(new TargetHUD());
        mods.add(new Brightness());
        mods.add(new Scoreboard());
        mods.add(new NoSlowdown());
        mods.add(new NoRotationSet());
    }


    public Mod getMod(final Class<? extends Mod> clazz) {
        if (clazz != null) {
            for (final Mod mod : getMods()) {
                if (mod.getClass() == clazz) {
                    return mod;
                }
            }
        }
        return null;
    }

    public Mod getMod(String modName) {
        if (modName != null) {
            for (Mod mod : getMods()) {
                if (mod.getAlias().equalsIgnoreCase(modName)) {
                    return mod;
                }
            }
        }
        return null;
    }

    public List<Mod> getMods() {
        mods.sort((o1, o2) -> {
            float s2 = Spicy.getINSTANCE().fontManager.arrayFont.getWidth(o1.getAlias() + (o1.getSuffix().isEmpty() ? o1.getSuffix() : "§f<" + o1.getSuffix() + ">"));
            float s1 = Spicy.getINSTANCE().fontManager.arrayFont.getWidth(o2.getAlias() + (o2.getSuffix().isEmpty() ? o2.getSuffix() : "§f<" + o2.getSuffix() + ">"));
            return Float.compare(s1, s2);
        });
        return mods;
    }

    public Listener<KeyEvent> keyListener = new Listener<>(event -> {
        for (Mod mod : mods) if (mod.getKey() == event.getKey()) mod.setState(!mod.isState());
    });
}
