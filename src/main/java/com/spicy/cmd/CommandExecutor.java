package com.spicy.cmd;

import java.util.List;

public interface CommandExecutor {

    void execute(List<String> args);
}