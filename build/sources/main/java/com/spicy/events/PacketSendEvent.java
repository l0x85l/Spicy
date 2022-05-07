package com.spicy.events;

import net.minecraft.network.*;
import pisi.unitedmeows.eventapi.event.Event;

public class PacketSendEvent extends Event
{
    private Packet packet;
    private Time time;
    
    public PacketSendEvent(final Time state, final Packet packet) {
        this.time = state;
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public Time getTime() {
        return this.time;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public void setTime(final Time state) {
        this.time = state;
    }
}
