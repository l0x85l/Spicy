package com.spicy.events;

import pisi.unitedmeows.eventapi.event.Event;

public class SprintEvent extends Event
{
    private boolean sprinting;
    
    public SprintEvent(final boolean sprinting) {
        this.setSprinting(sprinting);
    }
    
    public boolean isSprinting() {
        return this.sprinting;
    }
    
    public void setSprinting(final boolean sprinting) {
        this.sprinting = sprinting;
    }
}
