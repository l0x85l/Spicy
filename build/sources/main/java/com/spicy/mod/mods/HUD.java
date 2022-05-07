package com.spicy.mod.mods;

import com.spicy.Spicy;
import com.spicy.events.Render2DEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.utils.ColorUtils;
import com.spicy.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pisi.unitedmeows.eventapi.event.listener.Listener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class HUD extends Mod {
    int y;

    public HUD() {
        super("HUD", "Heads up display", 0, Category.RENDER);
    }

    public Listener<Render2DEvent> renderListener = new Listener<>(event -> {

        GL11.glPushMatrix();
        GL11.glColor3f(1, getMinecraft().thePlayer.hurtTime > 1 ? 0 : 1, getMinecraft().thePlayer.hurtTime > 1 ? 0 : 1);
        GL11.glEnable(GL11.GL_BLEND);
        getMinecraft().getTextureManager().bindTexture(new ResourceLocation("spicy/bird.png"));
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 + 90, res.getScaledHeight() - 60, 60, 60, 60, 60, 60, 60);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        if (getMinecraft().gameSettings.showDebugInfo) return;

        int colors = ColorUtils.fadeBetween(-485681, -1337, (System.currentTimeMillis()) % 5000 / (5000 / 2.0F));
        Spicy.getINSTANCE().fontManager.defaultFont.drawStringWithShadow(Spicy.getCLIENT_NAME().charAt(0) + "\u00A7f\u2764" + Spicy.getCLIENT_NAME().substring(1, 5), 5, 5, colors);
        String fps = String.format("%d fps (%d chunk update%s)", Minecraft.getDebugFPS(), RenderChunk.renderChunksUpdated, RenderChunk.renderChunksUpdated != 1 ? "s" : "");
        Spicy.getINSTANCE().fontManager.arrayFont.drawStringWithShadow(fps, 5, 20, -1);
        drawPotionStatus(event.getScaledResolution());


        y = 2;
        for (Mod mod : Spicy.getINSTANCE().modManager.getMods()) {
            double additionY = 12D;
            double offset = (System.currentTimeMillis() + (y / additionY * 100)) % 5000 / (5000 / 2.0F);
            int color = ColorUtils.fadeBetween(-485681, -1337, offset);
            if (mod.isState() && mod.isVisible()) {
                Spicy.getINSTANCE().fontManager.arrayFont.drawStringWithShadow(mod.getAlias() + (mod.getSuffix().isEmpty() ? mod.getSuffix() : "§f<" + mod.getSuffix() + ">"), event.getWidth() - (Spicy.getINSTANCE().fontManager.arrayFont.getWidth(mod.getAlias() + (mod.getSuffix().isEmpty() ? mod.getSuffix() : "§f<" + mod.getSuffix() + ">")) + 1), y, color);
                y += 10;
            }
        }
    });

    private void drawPotionStatus(ScaledResolution sr) {
        List<PotionEffect> potions = new ArrayList<>(getMinecraft().thePlayer.getActivePotionEffects());
        potions.sort(Comparator.comparingDouble(effect -> -Spicy.getINSTANCE().fontManager.arrayFont.getWidth(I18n.format((Potion.potionTypes[effect.getPotionID()]).getName()))));

        float pY = -2;
        for (PotionEffect effect : potions) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = I18n.format(potion.getName());
            String PType = "";
            if (effect.getAmplifier() == 0) {
                name = name + " I";
            } else if (effect.getAmplifier() == 1) {
                name = name + " II";
            } else if (effect.getAmplifier() == 2) {
                name = name + " III";
            } else if (effect.getAmplifier() == 3) {
                name = name + " IV";
            }
            if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
                PType = PType + "\2476 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = PType + "\247c " + Potion.getDurationString(effect);
                Spicy.getINSTANCE().fontManager.defaultFont.drawStringWithShadow("your effect's will end soon!", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, -1);
            } else if (effect.getDuration() > 600) {
                PType = PType + "\2477 " + Potion.getDurationString(effect);
            }

            Spicy.getINSTANCE().fontManager.arrayFont.drawStringWithShadow(name,
                    sr.getScaledWidth() - Spicy.getINSTANCE().fontManager.arrayFont.getWidth(name + PType),
                    sr.getScaledHeight() - 9 + pY, -1);
            GlStateManager.color(1,1,1);
            Spicy.getINSTANCE().fontManager.arrayFont.drawStringWithShadow(PType,
                    sr.getScaledWidth() - Spicy.getINSTANCE().fontManager.arrayFont.getWidth(PType), sr.getScaledHeight() - 9 + pY, -1);
            pY -= 9;
        }
    }
}
