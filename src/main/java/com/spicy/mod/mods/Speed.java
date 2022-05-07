package com.spicy.mod.mods;

import com.spicy.events.MoveEvent;
import com.spicy.events.UpdateEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.setting.$;
import com.spicy.setting.interfaces.Setting;
import com.spicy.utils.RotationUtils;
import com.spicy.utils.interfaces.IAxisAlignedBB;
import com.spicy.utils.interfaces.IMinecraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import pisi.unitedmeows.eventapi.event.Event;
import pisi.unitedmeows.eventapi.event.listener.Listener;


public class Speed extends Mod {
    public int ticks;
    private int turnTicks;
    private float prevYaw;
    private boolean turnCancel;
    private boolean prevStrafing;

    @Setting(label = "Mode")
    public $<Mode> mode = new $<>(Mode.OLDNCP);

    public Speed() {
        super("Speed", "increases player speed", 0, Category.MOVEMENT);
    }

    public enum Mode {
        OLDNCP, FANTASY
    }

    public Listener<UpdateEvent> updateListener = new Listener<UpdateEvent>(event -> {
        if (event.getTime() == Event.Time.BEFORE) {
            switch (mode.val()) {
                case OLDNCP: {
                    setSuffix("OldNCP");
                    if (!getMinecraft().thePlayer.onGround) {
                        this.ticks = -3;
                    }
                    ((IMinecraft) getMinecraft()).getTimer().timerSpeed = 1.0f;
                    if (this.checks()) {
                        double speed = 2.52;
                        if (this.isStrafing() != this.prevStrafing) {
                            this.turnTicks = 1;
                            this.turnCancel = true;
                        }
                        if (getMinecraft().thePlayer.ticksExisted % 3 == 0) {
                            ((IMinecraft) getMinecraft()).getTimer().timerSpeed = 1.3f;
                        }
                        if (this.turnTicks <= 0) {
                            this.turnCancel = false;
                        }
                        if (this.ticks == 1) {
                            if (!this.turnCancel && !this.isTurning() && !this.isColliding(getMinecraft().thePlayer.getEntityBoundingBox().offset(getMinecraft().thePlayer.motionX * speed, 0.0, getMinecraft().thePlayer.motionZ * speed))) {
                                ((IAxisAlignedBB) getMinecraft().thePlayer.getEntityBoundingBox()).offsetAndUpdate(getMinecraft().thePlayer.motionX * speed, 0.0, getMinecraft().thePlayer.motionZ * speed);
                            }
                            event.setY(getMinecraft().thePlayer.posY + 1.0E-4);
                        } else if (this.ticks >= 2) {
                            speed = 0.63;
                            if (!this.turnCancel && !this.isTurning() && !this.isColliding(getMinecraft().thePlayer.getEntityBoundingBox().offset(getMinecraft().thePlayer.motionX * speed, 0.0, getMinecraft().thePlayer.motionZ * speed))) {
                                ((IAxisAlignedBB) getMinecraft().thePlayer.getEntityBoundingBox()).offsetAndUpdate(getMinecraft().thePlayer.motionX * speed, 0.0, getMinecraft().thePlayer.motionZ * speed);
                            }
                            this.ticks = 0;
                        }
                    }
                    this.prevYaw = getMinecraft().thePlayer.rotationYaw;
                    ++this.ticks;
                    --this.turnTicks;
                    this.prevStrafing = this.isStrafing();
                    break;
                }
            }
        }
    }).weight(Event.Weight.SLAVE);

    public Listener<MoveEvent> moveListener = new Listener<>(event -> {
        if (getMinecraft().thePlayer.moveStrafing != 0.0f && getMinecraft().thePlayer.moveForward != 0.0f) {
            event.setX(event.getX() * 0.95);
            event.setZ(event.getZ() * 0.95);
        }
    });

    @Override
    public void onDisable() {
        ((IMinecraft) getMinecraft()).getTimer().timerSpeed = 1.0f;
        super.onDisable();
    }

    private boolean isColliding(final AxisAlignedBB bb) {
        boolean colliding = false;
        for (final AxisAlignedBB boundingBox : getMinecraft().theWorld.getCollidingBoundingBoxes(getMinecraft().thePlayer, bb)) {
            colliding = true;
        }
        if (this.getBlock(bb.offset(0.0, -0.1, 0.0)) instanceof BlockAir) {
            colliding = true;
        }
        return colliding;
    }

    private boolean isTurning() {
        return RotationUtils.getDistanceBetweenAngles(getMinecraft().thePlayer.rotationYaw, this.prevYaw) > 5.0f;
    }

    public Block getBlock(final AxisAlignedBB bb) {
        final int y = (int) bb.minY;
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
                final Block block = getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null) {
                    return block;
                }
            }
        }
        return null;
    }

    private boolean isStrafing() {
        final MovementInput movementInput = getMinecraft().thePlayer.movementInput;
        return movementInput.moveStrafe != 0.0f && getMinecraft().thePlayer.moveForward != 0.0f;
    }

    public boolean checks() {
        return !getMinecraft().thePlayer.isSneaking() && !getMinecraft().thePlayer.isCollidedHorizontally && (getMinecraft().thePlayer.isCollidedHorizontally || getMinecraft().thePlayer.moveForward != 0.0f || getMinecraft().thePlayer.moveStrafing != 0.0f) && !getMinecraft().gameSettings.keyBindJump.isPressed();
    }

    public double[] calculate(final double speed) {
        double forward = getMinecraft().thePlayer.movementInput.moveForward;
        double strafe = getMinecraft().thePlayer.movementInput.moveStrafe;
        float yaw = getMinecraft().thePlayer.rotationYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        final double xSpeed = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
        final double zSpeed = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        return new double[]{xSpeed, zSpeed};
    }
}
