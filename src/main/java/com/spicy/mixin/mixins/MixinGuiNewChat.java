package com.spicy.mixin.mixins;

import com.spicy.Spicy;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {

    @Redirect(method = "drawChat(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    public int onDrawStringWithShadow(FontRenderer fontRenderer, String string, float x, float y, int color) {
        return Spicy.getINSTANCE().fontManager.arrayFont.drawStringWithShadow(string, x, y, color);
    }
}