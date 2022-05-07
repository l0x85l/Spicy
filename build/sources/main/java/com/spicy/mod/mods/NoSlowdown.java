package com.spicy.mod.mods;

import com.spicy.events.MoveInputEvent;
import com.spicy.events.UpdateEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.setting.$;
import com.spicy.setting.interfaces.Setting;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import pisi.unitedmeows.eventapi.event.Event;
import pisi.unitedmeows.eventapi.event.listener.Listener;

public class NoSlowdown extends Mod {
    @Setting(label = "vanilla")
    public $<Boolean> vanilla = new $<>(false);
    public boolean wasOnground;

    public NoSlowdown() {
        super("NoSlowdown", "No using slowdown", 0, Category.MOVEMENT);
    }

    public Listener<UpdateEvent> updateListener = new Listener<UpdateEvent>(event -> {
        if (!this.vanilla.val() && getMinecraft().thePlayer.isBlocking() && (getMinecraft().thePlayer.motionX != 0.0 || getMinecraft().thePlayer.motionZ != 0.0) && wasOnground) {
            if (event.getTime() == Event.Time.BEFORE) {
                getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            else if (event.getTime() == Event.Time.AFTER) {
                getMinecraft().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(getMinecraft().thePlayer.inventory.getCurrentItem()));
            }
        }
        wasOnground = getMinecraft().thePlayer.onGround;
    }).weight(Event.Weight.SLAVE);


    public Listener<MoveInputEvent> moveInputListener = new Listener<>(event -> {
        if (getMinecraft().thePlayer.isUsingItem() && !getMinecraft().thePlayer.isRiding()) {
            event.getMovementInput().moveForward /= 0.2F;
            event.getMovementInput().moveStrafe /= 0.2F;
        }
    });

}