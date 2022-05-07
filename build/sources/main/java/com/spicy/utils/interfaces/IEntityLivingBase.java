package com.spicy.utils.interfaces;

import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public interface IEntityLivingBase {

    UUID getSprintingSpeedBoostModifierUUID();
    AttributeModifier getSprintingSpeedBoostModifier();
}
