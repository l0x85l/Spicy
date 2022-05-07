package com.spicy.mixin.mixins;

import com.mojang.authlib.GameProfile;
import com.spicy.Spicy;
import com.spicy.cmd.Command;
import com.spicy.cmd.CommandManager;
import com.spicy.events.MoveEvent;
import com.spicy.events.SprintEvent;
import com.spicy.events.UpdateEvent;
import com.spicy.utils.interfaces.IEntityLivingBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer implements IEntityLivingBase {

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile, NetHandlerPlayClient netHandler) {
        super(worldIn, playerProfile);
        sendQueue = netHandler;
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        MoveEvent event = new MoveEvent(x, y, z);
        Spicy.getINSTANCE().eventSystem.fire(event);
        super.moveEntity(event.getX(), event.getY(), event.getZ());
    }



    @Shadow
    public boolean serverSprintState;
    @Shadow
    public final NetHandlerPlayClient sendQueue;
    @Shadow
    private boolean serverSneakState;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    private double lastReportedPosX;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private float lastReportedPitch;
    @Shadow
    private int positionUpdateTicks;


    @Shadow
    protected abstract boolean isCurrentViewEntity();

    @Overwrite
    public void onUpdateWalkingPlayer()
    {
        UpdateEvent eventPre = new UpdateEvent(this.posY, this.rotationYaw, this.rotationPitch, this.onGround);
        Spicy.getINSTANCE().eventSystem.fire(eventPre);
        if(eventPre.isCanceled()) {
            UpdateEvent eventPost = new UpdateEvent();
            Spicy.getINSTANCE().eventSystem.fire(eventPost);
            return;
        }
        boolean flag = this.isSprinting();

        if (flag != this.serverSprintState)
        {
            if (flag)
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = flag;
        }

        boolean flag1 = this.isSneaking();

        if (flag1 != this.serverSneakState)
        {
            if (flag1)
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = flag1;
        }

        if (this.isCurrentViewEntity())
        {
            double d0 = this.posX - this.lastReportedPosX;
            double d1 = eventPre.getY() - this.lastReportedPosY;
            double d2 = this.posZ - this.lastReportedPosZ;
            double d3 = eventPre.getYaw() - this.lastReportedYaw;
            double d4 = eventPre.getPitch() - this.lastReportedPitch;
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0D || d4 != 0.0D;

            if (this.ridingEntity == null)
            {
                if ((flag2 && flag3) || eventPre.shouldAlwaysSend())
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, eventPre.getY(), this.posZ, eventPre.getYaw(), eventPre.getPitch(), eventPre.isOnground()));
                }
                else if (flag2)
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, eventPre.getY(), this.posZ, eventPre.isOnground()));
                }
                else if (flag3)
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(eventPre.getYaw(), eventPre.getPitch(), eventPre.isOnground()));
                }
                else
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(eventPre.isOnground()));
                }
            }
            else
            {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
                flag2 = false;
            }

            ++this.positionUpdateTicks;

            if (flag2)
            {
                this.lastReportedPosX = this.posX;
                this.lastReportedPosY = this.getEntityBoundingBox().minY;
                this.lastReportedPosZ = this.posZ;
                this.positionUpdateTicks = 0;
            }

            if (flag3)
            {
                this.lastReportedYaw = eventPre.getYaw();
                this.lastReportedPitch = eventPre.getPitch();
            }
        }
        Spicy.getINSTANCE().eventSystem.fire(new UpdateEvent());
    }


    @Overwrite
    public void sendChatMessage(String message) {
        if (Spicy.getINSTANCE().commandManager.isCommand(message)) {
            final Command cmd = Spicy.getINSTANCE().commandManager.findCommand(message);

            if (cmd != null && cmd.getExecutor() != null) {
                cmd.getExecutor().execute(Spicy.getINSTANCE().commandManager.getArgs(message));
            }
        } else {
            this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
        }
    }

    @Shadow
    public int sprintingTicksLeft;

    @Overwrite
    public void setSprinting(boolean sprinting) {
        final SprintEvent event = new SprintEvent(sprinting);
        Spicy.getINSTANCE().eventSystem.fire(event);
        sprinting = event.isSprinting();
        super.setSprinting(sprinting);
        final IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (var2.getModifier(getSprintingSpeedBoostModifierUUID()) != null) {
            var2.removeModifier(getSprintingSpeedBoostModifier());
        }
        if (sprinting) {
            var2.applyModifier(getSprintingSpeedBoostModifier());
        }
    }
}
