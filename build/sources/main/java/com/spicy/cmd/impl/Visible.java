package com.spicy.cmd.impl;

import com.spicy.Spicy;
import com.spicy.cmd.CommandExecutor;
import com.spicy.mod.Mod;
import com.spicy.utils.PrintUtils;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class Visible implements CommandExecutor {

    @Override
    public void execute(List<String> args) {

        if (args.size() < 1) {
            PrintUtils.error(new ChatComponentText(".visible <modname>"));
            return;
        }


        final String modName = args.get(0);

        if (Spicy.getINSTANCE().modManager.getMod(modName) == null) {
            PrintUtils.warning(new ChatComponentText("unknown or undefined mod"));
            return;
        }
        for(Mod mod : Spicy.getINSTANCE().modManager.getMods()) {
            if(modName.equalsIgnoreCase(mod.getAlias()))
                mod.setVisible(!mod.isVisible());
        }
    }
}