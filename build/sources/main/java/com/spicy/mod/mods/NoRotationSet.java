package com.spicy.mod.mods;

import com.spicy.events.PacketReceiveEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.utils.interfaces.IS08PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import pisi.unitedmeows.eventapi.event.listener.Listener;

public class NoRotationSet extends Mod {

    public NoRotationSet() {
        super("NoRotationSet", "No server rotation set", 0, Category.PLAYER);
    }

    public Listener<PacketReceiveEvent> packetReceiveListener = new Listener<PacketReceiveEvent>(event -> {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
            ((IS08PacketPlayerPosLook) event.getPacket()).setYaw(getMinecraft().thePlayer.rotationYaw);
            ((IS08PacketPlayerPosLook) event.getPacket()).setPitch(getMinecraft().thePlayer.rotationPitch);
        }
    }).filter(f -> getMinecraft().thePlayer != null);
}
