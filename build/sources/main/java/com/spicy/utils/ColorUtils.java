package com.spicy.utils;

import java.awt.*;

public class ColorUtils {

    public static int fadeBetween(final int startColor, final int endColor, double progress) {
        try {
            if (progress > 1.0D) {
                progress = 1.0D - progress % 1.0D;
            }
            return fadeTo(startColor, endColor, progress);
        } catch (final Exception e) {
            return startColor;
        }
    }

    public static int fadeTo(final int startColor, final int endColor, final double progress) {
        final double invert = 1.0D - progress;
        final int r = (int) ((startColor >> 16 & 0xFF) * invert + (endColor >> 16 & 0xFF) * progress);
        final int g = (int) ((startColor >> 8 & 0xFF) * invert + (endColor >> 8 & 0xFF) * progress);
        final int b = (int) ((startColor & 0xFF) * invert + (endColor & 0xFF) * progress);
        final int a = (int) ((startColor >> 24 & 0xFF) * invert + (endColor >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    public static Color blend(Color color1, Color color2, float ratio) {
        if (ratio < 0)
            return color2;
        if (ratio > 1)
            return color1;
        float ratio2 = (float) 1.0 - ratio;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        return new Color((rgb1[0] * ratio) + (rgb2[0] * ratio2), (rgb1[1] * ratio) + (rgb2[1] * ratio2), (rgb1[2] * ratio) + (rgb2[2] * ratio2));
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions.length == colors.length) {
            int[] indices = getFractionIndices(fractions, progress);
            float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
            Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            Color color = blend(colorRange[0], colorRange[1], 1.0F - weight);
            return color;
        } else {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
    }
    public static int[] getFractionIndices(float[] fractions, float progress) {
        int[] range = new int[2];

        int startPoint;
        for(startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }

        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }

        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0F - r;
        float[] rgb1 = color1.getColorComponents(new float[3]);
        float[] rgb2 = color2.getColorComponents(new float[3]);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0F) {
            red = 0.0F;
        } else if (red > 255.0F) {
            red = 255.0F;
        }

        if (green < 0.0F) {
            green = 0.0F;
        } else if (green > 255.0F) {
            green = 255.0F;
        }

        if (blue < 0.0F) {
            blue = 0.0F;
        } else if (blue > 255.0F) {
            blue = 255.0F;
        }

        Color color3 = null;

        try {
            color3 = new Color(red, green, blue);
        } catch (IllegalArgumentException var13) {
        }

        return color3;
    }

    public static int getColorByHealth(float maxHealth, float health) {
        Color green = new Color(72, 255, 94);
        Color yellow = new Color(255, 250, 57);
        Color red = new Color(255, 35, 40);

        float middleHealth = maxHealth / 2;

        if (health <= middleHealth) {
            return ColorUtils.blend(yellow, red, (health / middleHealth)).getRGB();
        } else if (health <= (middleHealth * 2)) {
            return ColorUtils.blend(green, yellow, ((health - middleHealth) / middleHealth)).getRGB();
        }
        return green.getRGB();
    }
}
