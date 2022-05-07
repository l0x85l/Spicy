package com.spicy.events;

import lombok.Getter;
import lombok.Setter;
import pisi.unitedmeows.eventapi.event.Event;

public class KeyEvent extends Event {
    @Getter @Setter private int key;

    public KeyEvent(int key) {
        this.key = key;
    }

}