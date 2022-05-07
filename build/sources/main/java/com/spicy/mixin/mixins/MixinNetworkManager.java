package com.spicy.mixin.mixins;

import com.spicy.Spicy;
import com.spicy.events.PacketReceiveEvent;
import com.spicy.events.PacketSendEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pisi.unitedmeows.eventapi.event.Event;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "channelRead0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void packetReceived(ChannelHandlerContext p_channelRead0_1_, Packet packet, CallbackInfo ci) {
        PacketReceiveEvent event = new PacketReceiveEvent(packet);
        Spicy.getINSTANCE().eventSystem.fire(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "dispatchPacket", at = @At("HEAD"), cancellable = true)
    private void sendPacketPre(Packet packetIn, GenericFutureListener[] futureListeners, CallbackInfo ci) {
        PacketSendEvent event = new PacketSendEvent(Event.Time.BEFORE, packetIn);
        Spicy.getINSTANCE().eventSystem.fire(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "dispatchPacket", at = @At("RETURN"))
    private void sendPacketPost(Packet packetIn, GenericFutureListener[] futureListeners, CallbackInfo ci) {
        PacketSendEvent event = new PacketSendEvent(Event.Time.AFTER, packetIn);
        Spicy.getINSTANCE().eventSystem.fire(event);
    }
}