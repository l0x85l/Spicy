package com.spicy.setting;

import com.spicy.mod.Mod;
import com.spicy.setting.interfaces.ICustomSetting;
import com.spicy.setting.interfaces.Setting;
import stelix.xfile.SxfDataBlock;
import stelix.xfile.SxfFile;
import stelix.xfile.reader.SxfReader;
import stelix.xfile.writer.SxfWriter;

import java.io.File;
import java.lang.reflect.Field;

public class SettingManager {

    private static final SxfWriter sxfWriter = new SxfWriter();

    public SettingManager() {
        sxfWriter.setWriteType(SxfWriter.WriteType.MULTI_LINE);
    }

    /*
        aura => {
            displayName: "Aura",
            shown: true,
            enabled: true,
            settings => (
                speed: 5,
                range: 12,
                mode: ["SWITCH"]
            )
        }
     */

    public void loadSettings(Mod mod, File file) {
        try {
            if (!file.exists()) {
                return;
            }
            SxfFile settingFile = SxfReader.readFile(file);
            SxfDataBlock modBlock = settingFile.dataBlock(mod.getAlias().toLowerCase());

            mod.setAlias(modBlock.variable("display_name"));
            mod.setVisible(modBlock.variable("shown"));
            mod.setKey(modBlock.variable("key"));

            boolean status = modBlock.variable("enabled");
            if (status != mod.isState()) {
                mod.toggle();
            }

            SxfDataBlock settingsBlock = modBlock.dataBlock("settings");

            for (Field field : mod.getClass().getDeclaredFields()) {
                try {

                    Setting setting = field.getDeclaredAnnotation(Setting.class);
                    if (setting != null) {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        $<?> baseSet = ($<?>) field.get(mod);
                        baseSet.setLabel(setting.label());
                        Object value = baseSet.val();
                        if (value instanceof ICustomSetting) {
                            ((ICustomSetting) value).load(settingsBlock.variable(setting.label()));
                        } else {
                            if (value instanceof Enum) {
                                String name = settingsBlock.variable(setting.label());
                                for (Object obj : ((Enum) value).getDeclaringClass().getEnumConstants()) {
                                    Enum en = (Enum) obj;
                                    if (en.name().equalsIgnoreCase(name)) {
                                        baseSet.setValRaw(en);
                                        break;
                                    }
                                }
                            } else {
                                baseSet.setVal(settingsBlock.variable(setting.label()));
                            }
                        }

                        mod.getSettings().add(baseSet);
                    }

                } catch (Exception ignored) {}

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveSettings(Mod mod, File file) {

        SxfFile settingFile = new SxfFile();
        SxfDataBlock modBlock = new SxfDataBlock();

        modBlock.putVar("display_name", mod.getAlias());
        modBlock.putVar("shown", mod.isVisible());
        modBlock.putVar("enabled", mod.isState());
        modBlock.putVar("key", mod.getKey());

        SxfDataBlock settingsBlock = new SxfDataBlock();


        for (Field field : mod.getClass().getDeclaredFields()) {
            try {

                Setting setting = field.getDeclaredAnnotation(Setting.class);
                if (setting != null) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    Object value = (($<?>) field.get(mod)).val();
                    if (value instanceof ICustomSetting) {
                        settingsBlock.putVar(setting.label(), ((ICustomSetting) value).save());
                    } else {
                        if (value instanceof Enum) {
                            settingsBlock.putVar(setting.label(), ((Enum) value).name());
                        } else {
                            settingsBlock.putVar(setting.label(), value);
                        }
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        modBlock.putBlock("settings", settingsBlock);
        settingFile.put(mod.getAlias().toLowerCase(), modBlock);


        sxfWriter.writeFile(settingFile, file);
    }
}
