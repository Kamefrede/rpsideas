package com.github.kamefrede.psiaddon.capability.stasis.damage;

public interface IStasisDamage {

    float resetDamageReceived();

    float getDamageReceived();

    void addDamageReceived(float damage);

    void setDamageReceived(float damage);



}
