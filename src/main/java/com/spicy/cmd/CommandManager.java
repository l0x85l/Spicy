package com.spicy.cmd;


import com.spicy.cmd.impl.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    private final String prefix = ".";

    public void loadCommands() {
        commands.add(new Command("help", "list of all commands", new Help()));
        commands.add(new Command("setval", "set the values of modules", new SetVal()));
        commands.add(new Command("toggle", "toggle the modules", new Toggle()));
        commands.add(new Command("bind", "set bind the modules", new Bind()));
        commands.add(new Command("visible", "set visible the modules", new Visible()));
    }

    public List<String> getArgs(String text) {
        if (!isCommand(text))
            return new ArrayList<>();

        final List<String> args = new ArrayList<>();

        final String[] split = seperatePrefix(text).split(" ");

        int beginIndex = 1;

        for (int i = beginIndex; i < split.length; i++){
            final String arg = split[i];

            if (arg == null)
                continue;

            args.add(arg);
        }

        return args;
    }

    public Command findCommand(String text) {
        final String[] split = seperatePrefix(text).split(" ");

        if (split.length <= 0)
            return null;


        return commands.stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(split[0]))
                .findFirst()
                .orElse(null);
    }

    public String seperatePrefix(String text) {
        if (!text.startsWith(prefix))
            return prefix + text;

        return text.substring(1);
    }

    public boolean isCommand(String text){
        return findCommand(text) != null;
    }

    public List<Command> getCommands() {
        return commands;
    }
}