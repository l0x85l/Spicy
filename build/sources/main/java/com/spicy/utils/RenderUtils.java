package com.spicy.utils;

import com.spicy.utils.interfaces.IMinecraft;
import com.spicy.utils.interfaces.IShaderGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtils {
    protected static ShaderGroup blurShader;
    protected static int lastScale;
    protected static int lastScaleWidth;
    protected static int lastScaleHeight;
    protected static Framebuffer buffer;
    protected static final Timer timer = new Timer();
    protected static final ResourceLocation shader = new ResourceLocation("spicy/blur.json");

    protected static Minecraft mc = Minecraft.getMinecraft();

    public static void blurArea(int x, int y, int width, int height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
                || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        if (OpenGlHelper.isFramebufferEnabled()) {

            if (timer.delay(1000)) {
                buffer.framebufferClear();
                timer.reset();
            }

            GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - height * factor), width * factor,
                    (height) * factor);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            setShaderConfigs(intensity, blurWidth, blurHeight);
            buffer.bindFramebuffer(true);
            blurShader.loadShaderGroup(((IMinecraft)mc).getTimer().renderPartialTicks);

            mc.getFramebuffer().bindFramebuffer(true);

            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO,
                    GL11.GL_ONE);
            buffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
            GlStateManager.disableBlend();
            GL11.glScalef(factor, factor, 0);

        }
    }


    public static void drawImage(float x, float y, final int width, final int height, final ResourceLocation image, Color color) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawCircleImage(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        float xRadius = width / 2f;
        float yRadius = height / 2f;
        float uRadius = (((u + (float) uWidth) * f) - (u * f)) / 2f;
        float vRadius = (((v + (float) vHeight) * f1) - (v * f1)) / 2f;
        for(int i = 0; i <= 360; i+=10) {
            double xPosOffset = Math.sin(i * Math.PI / 180.0D);
            double yPosOffset = Math.cos(i * Math.PI / 180.0D);
            worldrenderer.pos(x + xRadius + xPosOffset * xRadius, y + yRadius + yPosOffset * yRadius, 0)
                    .tex(u * f + uRadius + xPosOffset * uRadius, v * f1 + vRadius + yPosOffset * vRadius).endVertex();
        }
        tessellator.draw();
    }

    public static void initFboAndShader() {
        try {
            if (buffer != null) {
                buffer.deleteFramebuffer();
            }
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            buffer = ((IShaderGroup) blurShader).getListFramebuffers().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setShaderConfigs(float intensity, float blurWidth, float blurHeight) {
        ((IShaderGroup) blurShader).getListShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        ((IShaderGroup) blurShader).getListShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);

        ((IShaderGroup) blurShader).getListShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        ((IShaderGroup) blurShader).getListShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }

    public static void drawBlurryRect(float x, float y, float x1, float y1, int intensity, int size) {
        drawRect(
                (int) x,
                (int) y,
                (int) x1,
                (int) y1, new Color(50, 50, 50, 22).getRGB());
        blurArea(
                (int) x,
                (int) y,
                (int) x1 - (int) x,
                (int) y1 - (int) y,
                intensity, size, size);
    }

    public static void drawRect(float startX, float startY, float endX, float endY, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(startX, endY, 0.0D).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(endX, endY, 0.0D).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(endX, startY, 0.0D).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(startX, startY, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedRect(double x, double y, double x1, double y1, double radius, int color) {
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        setColor(color);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        int i;
        for (i = 0; i <= 90; i += 3)
            GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        GL11.glPopAttrib();
    }

    public static void setColor(int color) {
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        GL11.glColor4f(r, g, b, a);
    }


    public static void drawGradientRect(float minX, float minY, float maxX, float maxY, int par5, int par6)
    {
        int zLevel = 0;

        GL11.glPushMatrix();
        final float f = (par5 >> 24 & 255) / 255.0F;
        final float f1 = (par5 >> 16 & 255) / 255.0F;
        final float f2 = (par5 >> 8 & 255) / 255.0F;
        final float f3 = (par5 & 255) / 255.0F;
        final float f4 = (par6 >> 24 & 255) / 255.0F;
        final float f5 = (par6 >> 16 & 255) / 255.0F;
        final float f6 = (par6 >> 8 & 255) / 255.0F;
        final float f7 = (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        //GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(maxX, minY, zLevel).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(minX, minY, zLevel).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(minX, maxY, zLevel).color(f5, f6, f7, f4).endVertex();
        worldRenderer.pos(maxX, maxY, zLevel).color(f5, f6, f7, f4).endVertex();
        Tessellator.getInstance().draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    public static double animate(double target, double current, double speed) {
        boolean larger = target > current;
        if (speed < 0.0D) {
            speed = 0.0D;
        } else if (speed > 1.0D) {
            speed = 1.0D;
        }

        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1D) {
            factor = 0.1D;
        }

        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }

        return current;
    }

}
