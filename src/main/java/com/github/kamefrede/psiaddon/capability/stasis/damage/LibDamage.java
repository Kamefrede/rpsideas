package com.github.kamefrede.psiaddon.capability.stasis.damage;

import com.github.kamefrede.psiaddon.capability.stasis.time.IStasisTime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import static com.github.kamefrede.psiaddon.capability.stasis.damage.StasisDamageStorage.CAPABILITY_STASIS_DAMAGE;
import static com.github.kamefrede.psiaddon.capability.stasis.time.StasisTimeStorage.CAPABILITY_STASIS_TIME;

public class LibDamage {

    public static IStasisDamage addDamageFromEvent(EntityLiving entityLiving, float damage) {
        IStasisDamage statisDamage = entityLiving.getCapability(CAPABILITY_STASIS_DAMAGE, null);
        if (statisDamage != null){
            statisDamage.addDamageReceived(damage);
            System.out.println("added" + " " + damage);
        }
        return  null;
    }
    public static IStasisDamage getHandler(Entity entity) {

        if (entity.hasCapability(CAPABILITY_STASIS_DAMAGE, null))
            return entity.getCapability(CAPABILITY_STASIS_DAMAGE, null);
        System.out.println("no capability");
        return null;
    }

    public static IStasisDamage requireStasisDamage(EntityLiving living) {
        IStasisDamage instance = living.getCapability(CAPABILITY_STASIS_DAMAGE, null);
        if (instance == null) {
            throw new IllegalStateException(String.format(
                    "Missing Capability (%s): %s - \"%s\"",
                    CAPABILITY_STASIS_DAMAGE.getName(),
                    living.getClass().getName(),
                    living
            ));
        }
        return instance;
    }



}
