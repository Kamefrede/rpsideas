package com.kamefrede.rpsideas.entity;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.entity.botania.EntityPsiManaBurst;
import com.kamefrede.rpsideas.util.libs.LibEntityNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RPSEntities {
    private static int id = 0;

    public static void init() {
        registerModEntity(EntitySniperProjectile.class, LibEntityNames.ENTITY_SNIPER_BULLET, 256, 1, true);
        registerModEntity(EntityGaussPulse.class, LibEntityNames.ENTITY_GAUSS_PULSE, 256, 10, true);
        registerModEntity(EntityPsireworkRocket.class, LibEntityNames.ENTITY_PSI_FIREWORK, 64, 10, true);
    }

    @SideOnly(Side.CLIENT)
    public static void clientInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityPsireworkRocket.class,
                (manager) -> new RenderSnowball<>(manager, Items.FIREWORKS, Minecraft.getMinecraft().getRenderItem()));
    }

    @Optional.Method(modid = "botania")
    public static void initBotania() {
        registerModEntity(EntityPsiManaBurst.class, LibEntityNames.ENTITY_PSI_BURST, 64, 10, true);
    }

    public static void registerModEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(RPSIdeas.MODID, entityName), entityClass, entityName, id++, RPSIdeas.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
}
