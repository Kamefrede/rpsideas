package com.github.kamefrede.psiaddon.capability.stasis.time;

public class DefaultStasisTimeHandler implements IStasisTime {

    private double time;

    @Override
    public double getStasisTime() {
        return this.time;
    }

    @Override
    public void addStasisTime(double time) {
        this.time += time;
    }

    @Override
    public void remStasisTime(double time) {
        this.time -= time;
    }

    @Override
    public void setStasisTime(double time) {
        this.time = time;
    }

    @Override
    public void entTimeCounter(double time) {
        this.time = this.time - 1;
    }
}
