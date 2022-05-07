package com.spicy.events;

    import net.minecraft.network.*;
import pisi.unitedmeows.eventapi.event.Event;

public class PacketReceiveEvent extends Event
{
    private Packet packet;
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public PacketReceiveEvent(final Packet packet) {
        this.packet = packet;
    }
}
