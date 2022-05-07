package com.spicy.setting;

import com.spicy.Spicy;
import com.spicy.mod.Mod;
import com.spicy.utils.FileUtils;
import net.minecraft.util.Tuple;

import java.io.File;

public class SSettingManager extends SettingManager {


    public void start() {
       for (Mod mod : Spicy.getINSTANCE().modManager.getMods()) {
            Tuple<Boolean, File> result = FileUtils.getConfigFile(mod.getAlias());
            if (!result.getFirst()) {
                loadSettings(mod, result.getSecond());
            }
       }
    }

    public void stop() {
        for (Mod mod : Spicy.getINSTANCE().modManager.getMods()) {
            Tuple<Boolean, File> result = FileUtils.getConfigFile(mod.getAlias());
            saveSettings(mod, result.getSecond());
        }
    }
}