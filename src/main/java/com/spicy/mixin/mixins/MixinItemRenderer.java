package com.spicy.mixin.mixins;

import com.spicy.utils.interfaces.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Shadow
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {}

    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "net.minecraft.client.renderer.ItemRenderer.transformFirstPersonItem(FF)V", ordinal = 2))
    public void injectCustomTransform(ItemRenderer itemRenderer, float equipProgress, float swingProgress) {
        try {
            transformFirstPersonItem(equipProgress, Minecraft.getMinecraft().thePlayer.getSwingProgress(((IMinecraft) Minecraft.getMinecraft()).getTimer().renderPartialTicks));
        } catch (Exception e) {
            transformFirstPersonItem(equipProgress, swingProgress);
        }
    }
}