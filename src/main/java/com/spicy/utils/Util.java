package com.spicy.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<Entity> loadedEntityList() {
        final List<Entity> loadedList = new ArrayList<Entity>(Minecraft.getMinecraft().theWorld.loadedEntityList);
        loadedList.remove(Minecraft.getMinecraft().thePlayer);
        return loadedList;
    }
}
