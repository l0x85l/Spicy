package com.spicy.mixin.mixins;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {
    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @ModifyConstant(method = "attackTargetEntityWithCurrentItem", constant = @Constant(doubleValue = 0.6D))
    private double multiplyMotion(double original) {
        try {
            return 1.0;
        } catch (Exception e) {}
        return original;
    }
}