package com.spicy.mod.mods;

import com.spicy.events.PacketReceiveEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.utils.interfaces.IS27PacketExplosion;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import pisi.unitedmeows.eventapi.event.listener.Listener;


public class Velocity extends Mod {


    public Velocity() {
        super("Velocity", "No knockback", 0, Category.COMBAT);
    }

    public Listener<PacketReceiveEvent> packetReceiveListener = new Listener<>(event -> {
        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
            if (getMinecraft().theWorld.getEntityByID(packet.getEntityID()) == getMinecraft().thePlayer) {
                    event.setCanceled(true);
            }
        }
        else if (event.getPacket() instanceof S27PacketExplosion) {
            S27PacketExplosion packet = (S27PacketExplosion) event.getPacket();
            IS27PacketExplosion packetExtension = (IS27PacketExplosion) packet;
            float motionX = packet.func_149149_c();
            float motionY = packet.func_149144_d();
            float motionZ = packet.func_149147_e();
            packetExtension.setVelocityX(motionX);
            packetExtension.setVelocityY(motionY);
            packetExtension.setVelocityZ(motionZ);
        }
    });
}
