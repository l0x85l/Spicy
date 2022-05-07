package com.spicy.font;

import java.awt.*;

public class FontManager {

    public TTFFontRenderer defaultFont;
    public TTFFontRenderer arrayFont;



    public FontManager() {
        defaultFont = new TTFFontRenderer(new Font("Verdana", Font.PLAIN, 20));
        arrayFont = new TTFFontRenderer(new Font("Verdana", Font.PLAIN, 15));
    }
}