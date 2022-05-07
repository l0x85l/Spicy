package com.spicy.cmd.impl;

import com.spicy.Spicy;
import com.spicy.cmd.CommandExecutor;
import com.spicy.utils.PrintUtils;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class Help implements CommandExecutor {

    @Override
    public void execute(List<String> args) {
        Spicy.getINSTANCE().commandManager.getCommands().forEach(command -> PrintUtils.info(new ChatComponentText(command.getName().toUpperCase() + " - " + command.getDesc())));
    }
}