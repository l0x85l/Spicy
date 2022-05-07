package com.spicy.mod.mods;

import com.spicy.Spicy;
import com.spicy.events.Render3DEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.utils.ColorUtils;
import com.spicy.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import pisi.unitedmeows.eventapi.event.listener.Listener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Nametags extends Mod {

    public Nametags() {
        super("Nametags", "Shows the player tags", 0, Category.RENDER);
    }

    public Listener<Render3DEvent> renderListener = new Listener<>(event -> {
        List<EntityPlayer> players = new ArrayList<>();

        for (EntityPlayer player : getMinecraft().theWorld.playerEntities) {
            if (player != getMinecraft().getRenderViewEntity() && player.isEntityAlive() && !players.contains(player)) {
                players.add(player);
            }
        }

        // Sort by reverse distance (render close after far.)
        players.sort((p1, p2) -> Double.compare(p2.getDistanceSqToEntity(getMinecraft().getRenderViewEntity()), p1.getDistanceSqToEntity(getMinecraft().getRenderViewEntity())));

        GL11.glPushMatrix();
        GL11.glTranslated(-getMinecraft().getRenderManager().viewerPosX, -getMinecraft().getRenderManager().viewerPosY, -getMinecraft().getRenderManager().viewerPosZ);

        for (EntityPlayer player : players) {
            String tag = player.getDisplayName().getFormattedText();
            double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
            double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
            double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

            posY += player.isSneaking() ? 0.5 : 0.7;

            double scale = Math.max(1.6, getMinecraft().getRenderViewEntity().getDistanceToEntity(player) / 4);

            int colour = 0xFFFFFF;
            if (player.isInvisible()) {
                colour = 0xFFF26A;
            } else if (player.isSneaking()) {
                colour = 0x9E1535;
            }

            double hp = player.getHealth() + player.getAbsorptionAmount();
            double health = Math.ceil(hp) / 2;

            String healthStr = null;

            if (Math.floor(health) == health) {
                healthStr = String.valueOf((int) Math.floor(health));
            } else {
                healthStr = String.valueOf(health);
            }
            int healthCol = ColorUtils.getColorByHealth(player.getMaxHealth(), player.getHealth());

            scale /= 100;
            GL11.glPushMatrix();
            GL11.glTranslated(posX, posY + 1.4, posZ);
            GL11.glNormal3i(0, 1, 0);
            GL11.glRotatef(-getMinecraft().getRenderManager().playerViewY, 0, 1, 0);
            GL11.glRotatef(getMinecraft().getRenderManager().playerViewX, 1, 0, 0);
            GL11.glScaled(-scale, -scale, scale);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            int width;
            if (healthStr != null) {
                width = (int) (Spicy.getINSTANCE().fontManager.arrayFont.getWidth(tag + " " + healthStr) / 2) + 1;
            } else {
                width = (int) (Spicy.getINSTANCE().fontManager.arrayFont.getWidth(tag) / 2) + 1;
            }

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Gui.drawRect(-width - 2, -(getMinecraft().fontRendererObj.FONT_HEIGHT + 1), width + 2, 2, 0x9F0A0A0A);
            Spicy.getINSTANCE().fontManager.arrayFont.drawStringWithShadow(tag, -width, -(getMinecraft().fontRendererObj.FONT_HEIGHT - 1), -1);

            Spicy.getINSTANCE().fontManager.arrayFont.drawStringWithShadow(healthStr, -width + Spicy.getINSTANCE().fontManager.arrayFont.getWidth(tag + " "), -(getMinecraft().fontRendererObj.FONT_HEIGHT - 1), healthCol);

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    });
}
