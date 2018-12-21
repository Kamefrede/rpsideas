package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.RPSItem;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.client.core.handler.ClientTickHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemBioticSensor extends RPSItem implements IExosuitSensor, IItemColorProvider {
    public static final String EVENT_BIOTIC = RPSIdeas.MODID + ".event.nearby_entities";
    private static final double RANGE = 10d;
    private static final double RANGE_SQ = RANGE * RANGE;
    private static final AxisAlignedBB RANGE_AABB = new AxisAlignedBB(-RANGE, -RANGE, -RANGE, RANGE, RANGE, RANGE);
    private static final Map<EntityPlayer, List<EntityLivingBase>> triggeredBioticsRemote = new HashMap<>();
    private static final Map<EntityPlayer, List<EntityLivingBase>> triggeredBioticsNonRemote = new HashMap<>();

    public ItemBioticSensor() {
        super(RPSItemNames.BIOTIC_SENSOR);
        setMaxStackSize(1);
    }

    private static Map<EntityPlayer, List<EntityLivingBase>> getBiotics(World world) {
        return world.isRemote ? triggeredBioticsRemote : triggeredBioticsNonRemote;
    }

    @SubscribeEvent
    public static void entityTick(LivingEvent.LivingUpdateEvent e) {
        if (!(e.getEntityLiving() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) e.getEntityLiving();
        World world = player.world;

        Map<EntityPlayer, List<EntityLivingBase>> triggeredBiotics = getBiotics(world);
        List<EntityLivingBase> triggered = triggeredBiotics.computeIfAbsent(player, k -> new ArrayList<>());

        List<EntityLivingBase> nearbyUntriggeredEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, RANGE_AABB, ent -> {
            if (ent == player || ent == null) return false;
            if (triggered.contains(ent)) return false;
            return SpellHelpers.distanceSquared(player, ent) <= RANGE_SQ;
        });

        for (EntityLivingBase ent : nearbyUntriggeredEntities)
            PsiArmorEvent.post(new PsiArmorEvent(player, EVENT_BIOTIC, 0d, ent));

        triggered.clear();
        triggered.addAll(nearbyUntriggeredEntities);
    }

    @Override
    public String getEventType(ItemStack itemStack) {
        return EVENT_BIOTIC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColor(ItemStack stack) {
        byte add = (byte) Math.max(Math.sin(ClientTickHandler.ticksInGame * 0.1d) * 96, 0);
        return (add << 16) | (add << 8) | add;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColor() {
        return (stack, tintIndex) -> {
            if (tintIndex == 1)
                return getColor(stack);
            else return -1;
        };
    }
}
