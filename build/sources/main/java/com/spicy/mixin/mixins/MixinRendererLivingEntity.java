package com.spicy.mixin.mixins;

import com.spicy.Spicy;
import com.spicy.mod.mods.Nametags;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {

    public MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "renderName", at = @At("HEAD"), cancellable = true)
    public void onRenderName(T entity, double x, double y, double z, CallbackInfo callbackInfo) {
        if (entity instanceof EntityPlayer) {
            try {
                if (Spicy.getINSTANCE().modManager.getMod(Nametags.class).isState())
                    callbackInfo.cancel();
            } catch (Exception ignored) {
            }
        }
    }
}