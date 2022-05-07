package com.spicy.cmd.impl;

import com.spicy.Spicy;
import com.spicy.cmd.CommandExecutor;
import com.spicy.mod.Mod;
import com.spicy.utils.PrintUtils;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Bind implements CommandExecutor {

    @Override
    public void execute(List<String> args) {
        if(args.size() < 1) {
            PrintUtils.warning(new ChatComponentText(".bind <mod> <key>"));
            return;
        }


        args.set(1, args.get(1).toUpperCase());

        int key = Keyboard.getKeyIndex(args.get(1));
          for(Mod mod : Spicy.getINSTANCE().modManager.getMods()) {
              if(args.get(0).equalsIgnoreCase(mod.getAlias())) {
                  mod.setKey(Keyboard.getKeyIndex(Keyboard.getKeyName(key)));
                  PrintUtils.warning(new ChatComponentText(args.get(0) + " has bound to " + key));
                  if (args.get(1).equalsIgnoreCase("reset"))
                      mod.setKey(0);
              }
          }
    }
}