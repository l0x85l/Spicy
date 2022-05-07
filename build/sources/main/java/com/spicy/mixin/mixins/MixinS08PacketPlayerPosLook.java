package com.spicy.mixin.mixins;

import com.spicy.utils.interfaces.IS08PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(S08PacketPlayerPosLook.class)
public abstract class MixinS08PacketPlayerPosLook implements IS08PacketPlayerPosLook {
    @Shadow
    private double x;

    @Shadow
    private double y;

    @Shadow
    private double z;

    @Shadow
    private float yaw;

    @Shadow
    private float pitch;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}