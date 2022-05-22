package com.spicy.irc;

import com.spicy.Spicy;
import org.jibble.pircbot.PircBot;

import static net.minecraft.client.Minecraft.getMinecraft;


public class Myself extends PircBot {

    public static final String START_PREFIX = "$";

    public Myself() {
        this.setName(getMinecraft().getSession().getUsername() + "_neko");
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith(START_PREFIX)) {
            final String full = message.substring(START_PREFIX.length());

            if (full.contains("getinfo")) {
                Spicy.getINSTANCE().updateLook();
                return;
            }

            String username = full.split("::")[0];
            String hat = full.split("::")[1];
            String bandana = full.split("::")[2];
            String wing = full.split("::")[3];
            String halo = full.split("::")[4];

            Spicy.getINSTANCE().getUser(username).setHat(hat);
            Spicy.getINSTANCE().getUser(username).setBandana(bandana);
            Spicy.getINSTANCE().getUser(username).setWing(wing);
            Spicy.getINSTANCE().getUser(username).setHalo(halo);
        }

    }
}