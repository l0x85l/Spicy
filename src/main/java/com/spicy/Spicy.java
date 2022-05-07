package com.spicy;

import com.spicy.cmd.CommandManager;
import com.spicy.font.FontManager;
import com.spicy.mod.ModManager;
import com.spicy.setting.SSettingManager;
import com.spicy.user.User;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import pisi.unitedmeows.eventapi.system.BasicEventSystem;

public class Spicy {

    @Getter private static Spicy INSTANCE;
    @Getter private static final String CLIENT_NAME = "Spicy";
    @Getter private static final double CLIENT_VERSION = 0.4;
    public BasicEventSystem eventSystem;
    public SSettingManager sSettingManager;
    public ModManager modManager;
    public CommandManager commandManager;
    public FontManager fontManager;
    public User user;

    /** discord integration */
    @Getter public String username;
    @Getter public String discriminator;

    public Spicy() {
        INSTANCE = this;
    }


    public void start() {
        String display = CLIENT_NAME + " " + CLIENT_VERSION + " - with mixins!";
        Display.setTitle(display);
        eventSystem = new BasicEventSystem();
        fontManager = new FontManager();
        modManager = new ModManager();
        commandManager = new CommandManager();
        commandManager.loadCommands();
        sSettingManager = new SSettingManager();
        sSettingManager.start();
        user = new User();
        user.put(Minecraft.getMinecraft().getSession().getUsername());
    }

    public void stop() {
        sSettingManager.stop();
    }
}
