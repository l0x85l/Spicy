package com.spicy.cmd.impl;

import com.spicy.Spicy;
import com.spicy.cmd.CommandExecutor;
import com.spicy.cosmetic.CosmeticTextures;
import com.spicy.utils.PrintUtils;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class Cosmetic implements CommandExecutor {

    @Override
    public void execute(List<String> args) {
        if (args.size() < 1) {
            PrintUtils.error(new ChatComponentText(".cosmetic <name>"));
            PrintUtils.info(new ChatComponentText("type .cosmetic list"));
            return;
        }

        if (args.get(0).contains("list")) {
            PrintUtils.info(new ChatComponentText("HATS: wizard"));
            PrintUtils.info(new ChatComponentText("HALO: angel"));
            PrintUtils.info(new ChatComponentText("WINGS: dragon"));
            PrintUtils.info(new ChatComponentText("BANDANAS: nike"));
            return;
        }

        Spicy.getINSTANCE().updateLook();

        if (args.get(0).contains("halo")) {
            CosmeticTextures cosmeticTextures = new CosmeticTextures();
            String haloName = args.get(1);
            Spicy.getINSTANCE().getUser().setHalo(haloName);
            Spicy.getINSTANCE().updateLook();
        } else if (args.get(0).contains("hat")) {
            CosmeticTextures cosmeticTextures = new CosmeticTextures();
            String hatName = args.get(1);
            Spicy.getINSTANCE().getUser().setHat(hatName);
            Spicy.getINSTANCE().updateLook();
        } else if (args.get(0).contains("wing")) {
            CosmeticTextures cosmeticTextures = new CosmeticTextures();
            String wingName = args.get(1);
            Spicy.getINSTANCE().getUser().setWing(wingName);
            Spicy.getINSTANCE().updateLook();
        } else if (args.get(0).contains("bandana")) {
            CosmeticTextures cosmeticTextures = new CosmeticTextures();
            String bandanaName = args.get(1);
            Spicy.getINSTANCE().getUser().setBandana(bandanaName);
            Spicy.getINSTANCE().updateLook();
        }
    }
}