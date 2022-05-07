package com.spicy.mod.mods;

import com.spicy.events.UpdateEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import pisi.unitedmeows.eventapi.event.listener.Listener;

public class FastUse extends Mod {


    public FastUse() {
        super("FastUse", "Fast stuff using", 0, Category.PLAYER);
    }

    public Listener<UpdateEvent> updateListener = new Listener<>(event -> {
        if (getMinecraft().thePlayer.getItemInUseDuration() >= 16 && this.isValidItem(getMinecraft().thePlayer.getCurrentEquippedItem())) {
            for (int loop = 0; loop < 22; ++loop) {
                getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(getMinecraft().thePlayer.onGround));
            }
            getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(getMinecraft().thePlayer.getCurrentEquippedItem()));
            getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.UP));
            getMinecraft().thePlayer.stopUsingItem();
        }
    });

    private boolean isValidItem(final ItemStack itemStack) {
        return itemStack != null && (itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBucketMilk || itemStack.getItem() instanceof ItemFood) && getMinecraft().thePlayer.isUsingItem();
    }
}