package com.spicy.mod.mods;

import com.spicy.events.UpdateEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import pisi.unitedmeows.eventapi.event.listener.Listener;

public class AutoArmor extends Mod {


    public AutoArmor() {
        super("AutoArmor", "Auto equip armors", 0, Category.PLAYER);
    }

    public Listener<UpdateEvent> updateListener = new Listener<>(event -> {
        if (getMinecraft().thePlayer != null && (getMinecraft().currentScreen == null || getMinecraft().currentScreen instanceof GuiInventory || !getMinecraft().currentScreen.getClass().getName().contains("inventory"))) {
            int slotID = -1;
            double maxProt = -1.0;
            for (int i = 9; i < 45; ++i) {
                ItemStack stack = getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack != null) {
                    if (this.canEquip(stack)) {
                        double protValue = this.getProtectionValue(stack);
                        if (protValue >= maxProt) {
                            slotID = i;
                            maxProt = protValue;
                        }
                    }
                }
            }
            if (slotID != -1) {
                getMinecraft().playerController.windowClick(getMinecraft().thePlayer.inventoryContainer.windowId, slotID, 0, 1, getMinecraft().thePlayer);
            }
        }
    });


    private boolean canEquip(final ItemStack stack) {
        return (getMinecraft().thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots")) || (getMinecraft().thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings")) || (getMinecraft().thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate")) || (getMinecraft().thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet"));
    }

    private double getProtectionValue(final ItemStack stack) {
        return ((ItemArmor) stack.getItem()).damageReduceAmount + (100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4 * 0.0075;
    }
}