package com.github.kamefrede.rpsideas.items.components;

import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.helpers.MiscHelpers;
import net.minecraft.item.Item;
import vazkii.psi.api.exosuit.IExosuitSensor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.client.core.handler.ClientTickHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemBioticSensor extends Item implements IExosuitSensor {
    public ItemBioticSensor() {
        setMaxStackSize(1);
    }

    public static final String EVENT_BIOTIC = Reference.MODID + ".event.nearby_entities";
    private static final double RANGE = 10d;
    private static final double RANGE_SQ = RANGE * RANGE;
    private static final AxisAlignedBB RANGE_AABB = new AxisAlignedBB(-RANGE, -RANGE, -RANGE, RANGE, RANGE, RANGE);

    @Override
    public String getEventType(ItemStack itemStack) {
        return EVENT_BIOTIC;
    }

    @Override
    public int getColor(ItemStack stack) {
        byte add = (byte) Math.max(Math.sin(ClientTickHandler.ticksInGame * 0.1d) * 96, 0);
        return (add << 16) | (add << 8) | add;
    }

    //TODO this feels messy
    private static final HashMap<EntityPlayer, List<EntityLivingBase>> triggeredBioticsRemote = new HashMap<>();
    private static final HashMap<EntityPlayer, List<EntityLivingBase>> triggeredBioticsNonRemote = new HashMap<>();

    private static HashMap<EntityPlayer, List<EntityLivingBase>> getBiotics(World world) {
        return world.isRemote ? triggeredBioticsRemote : triggeredBioticsNonRemote;
    }

    @SubscribeEvent
    public static void entityTick(LivingEvent.LivingUpdateEvent e) {
        if(!(e.getEntityLiving() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) e.getEntityLiving();
        World world = player.world;

        HashMap<EntityPlayer, List<EntityLivingBase>> triggeredBiotics = getBiotics(world);
        List<EntityLivingBase> triggered = triggeredBiotics.computeIfAbsent(player, k -> new ArrayList<>());

        //TODO this is changed, debug this item.
        List<EntityLivingBase> nearbyUntriggeredEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, RANGE_AABB, ent -> {
            if(ent == player) return false;
            if(triggered.contains(ent)) return false;
            assert ent != null;
            return MiscHelpers.distanceSquared(player, ent) <= RANGE_SQ;
        });

        for(EntityLivingBase ent : nearbyUntriggeredEntities) {
            PsiArmorEvent.post(new PsiArmorEvent(player, EVENT_BIOTIC, 0d, ent));
        }

        triggered.clear();
        triggered.addAll(nearbyUntriggeredEntities);
    }
}
