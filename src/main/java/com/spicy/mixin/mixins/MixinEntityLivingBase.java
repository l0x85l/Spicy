package com.spicy.mixin.mixins;

import com.spicy.utils.interfaces.IEntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase implements IEntityLivingBase {
    @Shadow
    private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    @Shadow
    private static final AttributeModifier sprintingSpeedBoostModifier = (new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);


    @Override
    public UUID getSprintingSpeedBoostModifierUUID() {
        return sprintingSpeedBoostModifierUUID;
    }

    @Override
    public AttributeModifier getSprintingSpeedBoostModifier() {
        return sprintingSpeedBoostModifier;
    }
}
