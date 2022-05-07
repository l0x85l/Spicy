package com.spicy.events;

import lombok.Getter;
import net.minecraft.util.MovementInput;
import pisi.unitedmeows.eventapi.event.Event;

public class MoveInputEvent extends Event {
     @Getter private final MovementInput movementInput;

    public MoveInputEvent(MovementInput movementInput) {
        this.movementInput = movementInput;
    }



}