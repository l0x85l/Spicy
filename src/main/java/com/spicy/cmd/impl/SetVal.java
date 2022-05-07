package com.spicy.cmd.impl;

import com.spicy.Spicy;
import com.spicy.cmd.CommandExecutor;
import com.spicy.mod.Mod;
import com.spicy.setting.$;

import java.util.List;

public class SetVal implements CommandExecutor {

    @Override
    public void execute(List<String> args) {
        for (Mod mod : Spicy.getINSTANCE().modManager.getMods()) {
            if (mod.getAlias().equalsIgnoreCase(args.get(0)))
                for ($ settings : mod.getSettings())
                    if (args.get(1).equalsIgnoreCase(settings.label()))
                        settings.load(args.size() >= 3 ? args.get(2) : "");
        }
    }
}