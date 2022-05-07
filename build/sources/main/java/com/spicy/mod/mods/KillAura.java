package com.spicy.mod.mods;

import com.spicy.Spicy;
import com.spicy.events.Render3DEvent;
import com.spicy.events.UpdateEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.utils.RotationUtils;
import com.spicy.utils.Timer;
import com.spicy.utils.Util;
import com.spicy.utils.interfaces.IMinecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import pisi.unitedmeows.eventapi.event.listener.Listener;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class KillAura extends Mod {
    private boolean setupTick;
    public List<EntityLivingBase> targets;
    public int index;
    private Timer timer;
    private final ArrayList<hit> hits = new ArrayList<hit>();
    private float lastHealth;
    private EntityLivingBase lastTarget = null;

    public KillAura() {
        super("KillAura", "Auto attacking targets", 0, Category.COMBAT);
        this.targets = new ArrayList<EntityLivingBase>();
        this.timer = new Timer();
    }

    public Listener<UpdateEvent> updateListener = new Listener<UpdateEvent>(event -> {
        switch (event.getTime()) {
            case BEFORE: {
                setSuffix("Switch");
                NoSlowdown noSlowdownModule = (NoSlowdown) Spicy.getINSTANCE().modManager.getMod(NoSlowdown.class);
                this.targets = this.getTargets();
                if (this.index >= this.targets.size()) {
                    this.index = 0;
                }
                if (this.targets.size() > 0) {
                    EntityLivingBase target = this.targets.get(this.index);
                    if (target == null) {
                        this.lastHealth = 20;
                        lastTarget = null;
                        return;
                    }
                    if (this.lastTarget == null || target != this.lastTarget) {
                        this.lastTarget = target;
                        this.lastHealth = target.getHealth() / 2;
                        return;
                    }
                    if (target.getHealth() / 2 != this.lastHealth) {
                        if (target.getHealth() / 2 < this.lastHealth) {
                            this.hits.add(new hit(target.getPosition().add(ThreadLocalRandom.current().nextDouble(-0.5, 0.5), ThreadLocalRandom.current().nextDouble(1, 1.5), ThreadLocalRandom.current().nextDouble(-0.5, 0.5)), this.lastHealth - target.getHealth() / 2));
                        }
                        this.lastHealth = target.getHealth() / 2;
                    }

                        if (getMinecraft().thePlayer.getCurrentEquippedItem() != null && getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                            getMinecraft().playerController.sendUseItem(getMinecraft().thePlayer, getMinecraft().theWorld, getMinecraft().thePlayer.getCurrentEquippedItem());
                            if (!noSlowdownModule.isState()) {
                                getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                        }
                        float[] rotations = RotationUtils.getRotations(target);
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);

                    if (this.setupTick) {
                        if (this.targets.size() > 0 && getMinecraft().thePlayer.isCollidedVertically) {
                            event.setY(event.getY() + 0.07);
                            event.setGround(false);
                        }
                        if (this.timer.delay(300.0f)) {
                            this.incrementIndex();
                            this.timer.reset();
                        }
                    } else {
                        if (this.targets.size() > 0 && getMinecraft().thePlayer.isCollidedVertically) {
                            event.setGround(false);
                            event.setAlwaysSend(true);
                        }
                        if (getMinecraft().thePlayer.fallDistance > 0.0f && getMinecraft().thePlayer.fallDistance < 0.66) {
                            event.setGround(true);
                        }
                    }
                }
                this.setupTick = !this.setupTick;
                break;
            }
            case AFTER: {
                if (!this.setupTick || this.targets.size() <= 0 || this.targets.get(this.index) == null || isValidItem(getMinecraft().thePlayer.getCurrentEquippedItem())) {
                    break;
                }
                EntityLivingBase target = this.targets.get(this.index);
                if (getMinecraft().thePlayer.isBlocking()) {
                    getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                attack(target);
                if (getMinecraft().thePlayer.isBlocking()) {
                    getMinecraft().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(getMinecraft().thePlayer.inventory.getCurrentItem()));
                    break;
                }
                break;
            }
        }
    });

    public Listener<Render3DEvent> render3DListener = new Listener<>(event -> {
        try {
            for (hit h : hits) {
                if (h.isFinished()) {
                    hits.remove(h);
                } else {
                    h.onRender();
                }
            }
        } catch (Exception e) {
        }
    });


    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void incrementIndex() {
        ++this.index;
        if (this.index >= this.targets.size()) {
            this.index = 0;
        }
    }

    private boolean isValidItem(final ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() instanceof ItemPotion;
    }

    public void attack(final EntityLivingBase entity) {
        this.attackTarget(entity);
    }

    public void attackTarget(final EntityLivingBase entity) {
        getMinecraft().thePlayer.swingItem();
        final float sharpLevel = EnchantmentHelper.getModifierForCreature(getMinecraft().thePlayer.getHeldItem(), entity.getCreatureAttribute());
        final boolean vanillaCrit = getMinecraft().thePlayer.fallDistance > 0.0f && !getMinecraft().thePlayer.onGround && !getMinecraft().thePlayer.isOnLadder() && !getMinecraft().thePlayer.isInWater() && !getMinecraft().thePlayer.isPotionActive(Potion.blindness) && getMinecraft().thePlayer.ridingEntity == null;
        getMinecraft().getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (vanillaCrit) {
            getMinecraft().thePlayer.onCriticalHit(entity);
        }
        if (sharpLevel > 0.0f) {
            getMinecraft().thePlayer.onEnchantmentCritical(entity);
        }
    }

    public boolean isEntityValid(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (!getMinecraft().thePlayer.isEntityAlive() || !entityLiving.isEntityAlive() || entityLiving.getDistanceToEntity(getMinecraft().thePlayer) > (getMinecraft().thePlayer.canEntityBeSeen(entityLiving) ? 4.7 : 3.0)) {
                return false;
            }
            if (entityLiving.ticksExisted < 10.0) {
                return false;
            }
            if (entityLiving instanceof EntityPlayer) {
                final EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
                return true;
            }
        }
        return false;
    }


    private List<EntityLivingBase> getTargets() {
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Entity entity : Util.loadedEntityList()) {
            if (isEntityValid(entity)) {
                targets.add((EntityLivingBase) entity);
            }
        }
        targets.sort(new Comparator<EntityLivingBase>() {
            @Override
            public int compare(final EntityLivingBase target1, final EntityLivingBase target2) {
                return Math.round(target2.getHealth() / 2 - target1.getHealth() / 2);
            }
        });
        return targets;
    }

    class hit {
        private long startTime = System.currentTimeMillis();
        private final BlockPos pos;
        private final double healthVal;
        private final long maxTime = 1000;

        public hit(BlockPos pos, double healthVal) {
            this.startTime = System.currentTimeMillis();
            this.pos = pos;
            this.healthVal = healthVal;
        }

        public void onRender() {
            final double x = this.pos.getX() + (this.pos.getX() - this.pos.getX()) * ((IMinecraft) getMinecraft()).getTimer().renderPartialTicks - getMinecraft().getRenderManager().viewerPosX + 1.5;
            final double y = this.pos.getY() + (this.pos.getY() - this.pos.getY()) * ((IMinecraft) getMinecraft()).getTimer().renderPartialTicks - getMinecraft().getRenderManager().viewerPosY;
            final double z = this.pos.getZ() + (this.pos.getZ() - this.pos.getZ()) * ((IMinecraft) getMinecraft()).getTimer().renderPartialTicks - getMinecraft().getRenderManager().viewerPosZ;

            final float var10001 = (getMinecraft().gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f;
            final double size = (2.5);
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            getMinecraft().entityRenderer.setupCameraTransform(((IMinecraft) getMinecraft()).getTimer().renderPartialTicks, 0);
            GL11.glTranslated(x, y, z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(getMinecraft().getRenderManager().playerViewX, var10001, 0.0f, 0.0f);
            GL11.glScaled(-0.01666666753590107 * size, -0.01666666753590107 * size, 0.01666666753590107 * size);
            float sizePercentage;
            long timeLeft = (this.startTime + this.maxTime) - System.currentTimeMillis();
            float yPercentage = 0;
            if (timeLeft < 75) {
                sizePercentage = Math.min((float) timeLeft / 75F, 1F);
                yPercentage = Math.min((float) timeLeft / 75F, 1F);
            } else {
                sizePercentage = Math.min((float) (System.currentTimeMillis() - this.startTime) / 300F, 1F);
                yPercentage = Math.min((float) (System.currentTimeMillis() - this.startTime) / 600F, 1F);
            }
            GlStateManager.scale(0.8 * sizePercentage, 0.8 * sizePercentage, 0.8 * sizePercentage);
            Gui.drawRect(-100, -100, 100, 100, new Color(255, 0, 0, 0).getRGB());
            int color = 0;
            if (healthVal > 6) {
                color = Color.green.getRGB();
            } else if (healthVal < 5.99) {
                color = Color.red.getRGB();
            }
            getMinecraft().fontRendererObj.drawStringWithShadow(new DecimalFormat("#.##").format(this.healthVal), 0, (int) -(yPercentage * 1), color);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }

        public boolean isFinished() {
            return System.currentTimeMillis() - this.startTime >= maxTime;
        }
    }
}
