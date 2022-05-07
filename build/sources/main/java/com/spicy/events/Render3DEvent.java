package com.spicy.events;

import pisi.unitedmeows.eventapi.event.Event;

public class Render3DEvent extends Event
{
    float partialTicks;
    
    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
