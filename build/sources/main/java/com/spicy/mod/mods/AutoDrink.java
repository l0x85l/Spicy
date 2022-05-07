package com.spicy.mod.mods;

import com.spicy.Spicy;
import com.spicy.events.UpdateEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.utils.RotationUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Keyboard;
import pisi.unitedmeows.eventapi.event.listener.Listener;

public class AutoDrink extends Mod {
    public boolean drinking = false;
    public float health = 5.5f;

    public AutoDrink() {
        super("AutoDrink", "Auto drink healing potion", 0, Category.PLAYER);
    }

    private boolean swapped;

    /**
     * W.I.P not stable
     */
    public Listener<UpdateEvent> updateListener = new Listener<>(event -> {
        KillAura aura = (KillAura) Spicy.getINSTANCE().modManager.getMod(KillAura.class);
        if (aura.targets != null && aura.targets.size() > aura.index) {
            if (getMinecraft().thePlayer.getHealth() / 2 < health) {
                getPotion();
                KeyBinding.setKeyBindState(getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), true);
                drinking = true;
                if (aura.targets.size() > aura.index) {
                    final float[] rotations = RotationUtils.getRotations(aura.targets.get(aura.index));
                    getMinecraft().thePlayer.rotationYaw = -rotations[0];
                    KeyBinding.setKeyBindState(getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
                }
                aura.updateListener.pause();
                swapped = true;
            } else {
                if (swapped) {
                    drinking = false;
                    KeyBinding.setKeyBindState(getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), false);
                    getSword();
                    aura.updateListener.resume();
                    if (aura.targets.size() > aura.index) {
                        final float[] rotations = RotationUtils.getRotations(aura.targets.get(aura.index));
                        getMinecraft().thePlayer.rotationYaw = rotations[0];
                        KeyBinding.setKeyBindState(getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
                    }
                    swapped = false;
                }
            }
        }

    });


    public void getSword() {
        float item = 1.0f;
        int newItem = -1;
        for (int slot = 0; slot < 9; ++slot) {
            ItemStack var4 = getMinecraft().thePlayer.inventory.mainInventory[slot];
            if (var4 != null && var4.getItem() instanceof ItemSword) {
                ItemSword is = (ItemSword) var4.getItem();
                if (is.getDamageVsEntity() > item) {
                    newItem = slot;
                    item = is.getDamageVsEntity();
                }
            }
        }
        if (newItem > -1) {
            getMinecraft().thePlayer.inventory.currentItem = newItem;
        }
    }

    private void getPotion() {
        int newItem = -1;
        for (int slot = 0; slot < 9; ++slot) {
            ItemStack stack = getMinecraft().thePlayer.inventory.mainInventory[slot];
            if (stack != null && stack.getItem() instanceof ItemPotion) {
                ItemPotion is = (ItemPotion) stack.getItem();
                if (is.getItemStackDisplayName(stack).contains("healing"))
                    newItem = slot;
            }
        }
        if (newItem > -1) {
            getMinecraft().thePlayer.inventory.currentItem = newItem;
        }
    }
}