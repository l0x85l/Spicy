package com.spicy;

import com.spicy.cmd.CommandManager;
import com.spicy.cosmetic.CosmeticTextures;
import com.spicy.font.FontManager;
import com.spicy.irc.Myself;
import com.spicy.mod.ModManager;
import com.spicy.setting.SSettingManager;
import com.spicy.user.User;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.jibble.pircbot.IrcException;
import org.lwjgl.opengl.Display;
import pisi.unitedmeows.eventapi.system.BasicEventSystem;

import java.io.IOException;
import java.util.HashMap;

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

    public Myself myself;

    public HashMap<String, User> userMap;

    public CosmeticTextures cosmeticTextures;

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
        cosmeticTextures = new CosmeticTextures();
        user = new User(Minecraft.getMinecraft().getSession().getUsername(), "hat", "bandana", "wing", "halo");
        userMap = new HashMap<>();
        userMap.put(Minecraft.getMinecraft().getSession().getUsername(), user);
        myself = new Myself();
        try {
            myself.connect("irc.freenode.net");
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
        myself.setVerbose(true);
        myself.joinChannel("#nekomod0");
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                updateLook();
            } catch (InterruptedException ignored) {
            }
            myself.sendMessage("#nekomod0", Myself.START_PREFIX + "getinfo");
        }).start();
        fontManager = new FontManager();
        modManager = new ModManager();
        commandManager = new CommandManager();
        commandManager.loadCommands();
        sSettingManager = new SSettingManager();
        sSettingManager.start();
    }

    public void stop() {
        sSettingManager.stop();
    }

    public void updateLook() {
        myself.sendMessage("#nekomod0", user.getUpdateText());
    }

    public HashMap<String, User> getUserMap() {
        return userMap;
    }

    public User registerUser(String user) {
        if (userMap.containsKey(user)) {
            return userMap.get(user);
        } else {
            User newUser = new User(user, "hat", "bandana", "wing", "halo");
            userMap.put(user, newUser);
            return newUser;
        }
    }

    public User getUser(final String name) {
        return registerUser(name);
    }

    public User getUser() {
        return user;
    }
}
