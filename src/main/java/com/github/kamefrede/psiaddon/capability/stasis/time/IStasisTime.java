package com.github.kamefrede.psiaddon.capability.stasis.time;

public interface IStasisTime {
    double getStasisTime();

    void addStasisTime(double time);

    void setStasisTime(double time);

    void remStasisTime(double time);

    void entTimeCounter(double time);
}
