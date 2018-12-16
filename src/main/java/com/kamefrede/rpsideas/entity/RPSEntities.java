package com.kamefrede.rpsideas.entity;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.libs.LibEntityNames;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class RPSEntities {
    private static int id = 0;

    public static void init() {
        registerModEntity(EntitySniperProjectile.class, LibEntityNames.ENTITY_SNIPER_BULLET, 256, 1, true);
        registerModEntity(EntityGaussPulse.class, LibEntityNames.ENTITY_GAUSS_PULSE, 256, 10, true);
    }

    private static void registerModEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(entityName), entityClass, entityName, id++, RPSIdeas.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);

    }
}
