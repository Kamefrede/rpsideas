package com.github.kamefrede.rpsideas.entity;

import com.github.kamefrede.rpsideas.Psiam;
import com.github.kamefrede.rpsideas.util.libs.LibEntities;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
    public static void init() {
        int id = 0;

        registerModEntity(EntitySniperProjectile.class, LibEntities.ENTITY_SNIPER_BULLET, id++, Psiam.INSTANCE, 256, 1, true);
    }

    private static void registerModEntity(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(entityName), entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);

    }
}
