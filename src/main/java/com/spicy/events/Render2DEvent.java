package com.spicy.events;

import lombok.Getter;
import net.minecraft.client.gui.ScaledResolution;
import pisi.unitedmeows.eventapi.event.Event;

public class Render2DEvent extends Event {

    @Getter private final ScaledResolution scaledResolution;

    public Render2DEvent(ScaledResolution _scaledResolution) {
        scaledResolution = _scaledResolution;
    }


    public int getWidth() {
        return scaledResolution.getScaledWidth();
    }

    public int getHeight() {
        return scaledResolution.getScaledHeight();
    }
}