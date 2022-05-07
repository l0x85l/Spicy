package com.spicy.mixin.mixins;

import com.spicy.Spicy;
import com.spicy.events.MoveInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public abstract class MixinMovementInputFromOptions extends MovementInput {
    @Inject(method = "updatePlayerMoveState()V", at = @At("RETURN"))
    public void onUpdatePlayerMoveState(CallbackInfo callbackInfo) {
        try {
            Spicy.getINSTANCE().eventSystem.fire(new MoveInputEvent(Minecraft.getMinecraft().thePlayer.movementInput));
        } catch (Exception ignored) {}
    }
}