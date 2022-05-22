package com.spicy.cosmetic;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class CosmeticTextures {
    public ResourceLocation WIZARD_HAT = new ResourceLocation("spicy/cosmetic/hat/wizardhat.png");
    public ResourceLocation NIKE_BANDANA = new ResourceLocation("spicy/cosmetic/bandanas/nike.png");
    public ResourceLocation DRAGON_WING = new ResourceLocation("spicy/cosmetic/wing/dragon.png");
    public ResourceLocation HALO = new ResourceLocation("spicy/cosmetic/halo/halo.png");


    public HashMap<String, ResourceLocation> hats = new HashMap<>();
    public HashMap<String, ResourceLocation> bandanas = new HashMap<>();
    public HashMap<String, ResourceLocation> wings = new HashMap<>();
    public HashMap<String, ResourceLocation> halo = new HashMap<>();

    public CosmeticTextures() {
        hats.put("wizard", WIZARD_HAT);
        halo.put("angel", HALO);
        bandanas.put("nike", NIKE_BANDANA);
        wings.put("dragon", DRAGON_WING);
    }




    public ResourceLocation getHat(String name) { return hats.getOrDefault(name, null); }

    public ResourceLocation getHalo(String name) { return halo.getOrDefault(name, null); }

    public ResourceLocation getWing(String name) { return wings.getOrDefault(name, null); }

    public ResourceLocation getBandana(String name) { return bandanas.getOrDefault(name, null); }


}