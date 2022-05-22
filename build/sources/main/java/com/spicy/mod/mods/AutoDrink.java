package com.spicy.mod.mods;

import com.spicy.Spicy;
import com.spicy.events.UpdateEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.setting.$;
import com.spicy.setting.interfaces.Setting;
import com.spicy.utils.RotationUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;
import pisi.unitedmeows.eventapi.event.listener.Listener;

public class AutoDrink extends Mod {
    public boolean drinking = false;
    @Setting(label = "health")
    public $<Double> health = new $<>(5.5, 1.0, 10.0);

    public AutoDrink() {
        super("AutoDrink", "Auto drink healing potion", 0, Category.PLAYER);
    }

    private boolean swapped;

    @Setting(label = "Mode")
    public $<AutoDrink.Mode> mode = new $<>(AutoDrink.Mode.MANUAL);


    public enum Mode {
        MANUAL, AUTO
    }

    /**
     * W.I.P not stable
     */
    public Listener<UpdateEvent> updateListener = new Listener<>(event -> {
        KillAura aura = (KillAura) Spicy.getINSTANCE().modManager.getMod(KillAura.class);
        switch (mode.val()) {
            case AUTO: {
                setSuffix("Auto " + health.val());
                if (getMinecraft().thePlayer.getHealth() / 2 < health.val()) {
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
                break;
            }
            case MANUAL: {
                setSuffix("Manual " + health.val());
                if (getMinecraft().thePlayer.getHealth() / 2 < health.val()) {
                    getPotion();
                    KeyBinding.setKeyBindState(getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), true);
                    drinking = true;
                    aura.updateListener.pause();
                    swapped = true;
                } else {
                    if (swapped) {
                        drinking = false;
                        KeyBinding.setKeyBindState(getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), false);
                        getSword();
                        aura.updateListener.resume();
                        swapped = false;
                    }
                }
                break;
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
                for (PotionEffect effect : is.getEffects(stack)) {
                    if (effect.getPotionID() == Potion.heal.id) {
                        newItem = slot;
                    }
                }
            }
        }
        if (newItem > -1) {
            getMinecraft().thePlayer.inventory.currentItem = newItem;
        }
    }
}