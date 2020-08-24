package com.kamefrede.rpsideas.util;

import net.minecraft.util.math.BlockPos;

public class SilencedPosition {

    private int time;
    private long timestamp;
    private BlockPos position;
    private float volume;
    private int radius;
    private int dimension;

    public SilencedPosition(int time, long timestamp, BlockPos position, float volume, int radius, int dimension) {
        this.time = time;
        this.timestamp = timestamp;
        this.position = position;
        this.volume = volume;
        this.radius = radius;
        this.dimension = dimension;
    }

    public BlockPos getPosition() {
        return position;
    }

    public float getVolume() {
        return volume;
    }

    public int getDimension() {
        return dimension;
    }

    public int getRadius() {
        return radius;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void renew(long timestamp, int time, float volume, int radius) {
        this.timestamp = timestamp;
        this.time = time;
        this.volume = volume;
        this.radius = radius;
    }

    public int getTime() {
        return time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long expiryDate() {
        return (timestamp + (long) time);
    }
}
