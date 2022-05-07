package com.spicy.events;


import pisi.unitedmeows.eventapi.event.Event;

public class UpdateEvent extends Event
{
    private final Time time;
    private float yaw;
    private float pitch;
    private double y;
    private boolean onground;
    private boolean alwaysSend;

    public UpdateEvent() {
        this.time = Time.AFTER;
    }

    public UpdateEvent(final double y, final float yaw, final float pitch, final boolean ground) {
        this.time = Time.BEFORE;
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.onground = ground;
    }

    public Time getTime() {
        return this.time;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public double getY() {
        return this.y;
    }

    public boolean isOnground() {
        return this.onground;
    }

    public boolean shouldAlwaysSend() {
        return this.alwaysSend;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public void setGround(final boolean ground) {
        this.onground = ground;
    }

    public void setAlwaysSend(final boolean alwaysSend) {
        this.alwaysSend = alwaysSend;
    }
}
