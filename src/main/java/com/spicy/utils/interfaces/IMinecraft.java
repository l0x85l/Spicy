package com.spicy.utils.interfaces;

import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public interface IMinecraft {
    Timer getTimer();
    void setSession(Session session);

    int getRightClickDelayTimer();
    void setRightClickDelayTimer(int rightClickDelayTimer);
}