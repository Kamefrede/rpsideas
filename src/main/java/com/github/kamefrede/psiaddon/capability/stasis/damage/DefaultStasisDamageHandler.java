package com.github.kamefrede.psiaddon.capability.stasis.damage;

public class DefaultStasisDamageHandler implements IStasisDamage {

    private float damage;

    @Override
    public float resetDamageReceived(){
        return this.damage -= this.damage;
    }


    @Override
    public float getDamageReceived(){
        return this.damage;
    }

    @Override
    public void addDamageReceived(float damage){
        this.damage += damage;
    }

    @Override
    public void setDamageReceived(float damage){
        this.damage = damage;
    }



}
