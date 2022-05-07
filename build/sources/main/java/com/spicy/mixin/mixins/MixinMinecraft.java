package com.spicy.mixin.mixins;

import com.spicy.Spicy;
import com.spicy.events.KeyEvent;
import com.spicy.events.TickEvent;
import com.spicy.utils.interfaces.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft implements IMinecraft {
    @Shadow @Final
    private Timer timer;

    @Shadow
    public GuiScreen currentScreen;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void minecraftConstructor(GameConfiguration gameConfig, CallbackInfo ci) {
        new Spicy();
    }


    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
    private void startGame(CallbackInfo ci) {
        Spicy.getINSTANCE().start();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void onShutdown(CallbackInfo ci) {
        Spicy.getINSTANCE().stop();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
    private void onKey(CallbackInfo ci) {
        if (Keyboard.getEventKeyState() && currentScreen == null)
            Spicy.getINSTANCE().eventSystem.fire(new KeyEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        Spicy.getINSTANCE().eventSystem.fire(new TickEvent());
    }

    @Shadow
    private int rightClickDelayTimer;

    @Override
    public int getRightClickDelayTimer() {
        return rightClickDelayTimer;
    }

    @Override
    public void setRightClickDelayTimer(int rightClickDelayTimer) {
        this.rightClickDelayTimer = rightClickDelayTimer;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Shadow @Final @Mutable
    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }
}
