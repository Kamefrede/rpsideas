package com.github.kamefrede.psiaddon.handler;

import com.github.kamefrede.psiaddon.capability.stasis.damage.IStasisDamage;

import com.github.kamefrede.psiaddon.capability.stasis.time.IStasisTime;
import com.github.kamefrede.psiaddon.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.core.jmx.Server;
import vazkii.arl.util.ItemTickHandler;

import java.util.List;

import static com.github.kamefrede.psiaddon.capability.stasis.damage.LibDamage.addDamageFromEvent;
import static com.github.kamefrede.psiaddon.capability.stasis.damage.LibDamage.requireStasisDamage;
import static com.github.kamefrede.psiaddon.capability.stasis.damage.StasisDamageStorage.CAPABILITY_STASIS_DAMAGE;
import static com.github.kamefrede.psiaddon.capability.stasis.time.LibTime.remTimeFromEvent;
import static com.github.kamefrede.psiaddon.capability.stasis.time.LibTime.requireMiscStasisTime;
import static com.github.kamefrede.psiaddon.capability.stasis.time.LibTime.requireStasisTime;
import static com.github.kamefrede.psiaddon.capability.stasis.time.StasisTimeStorage.CAPABILITY_STASIS_TIME;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class StasisTickHandler {


    @SubscribeEvent
    public static void scheduler(TickEvent.WorldTickEvent event) {
        List<Entity> entList = event.world.loadedEntityList;
        for (Entity ent : entList) {
            if (ent instanceof EntityMinecart || ent instanceof EntityFallingBlock || ent instanceof EntityBoat) {
                requireMiscStasisTime(ent);
                double timeToWait = requireMiscStasisTime(ent).getStasisTime();

                if (event.phase == TickEvent.Phase.START && event.side == Side.SERVER) {
                    if (timeToWait > 1) {
                        requireMiscStasisTime(ent).remStasisTime(1);
                    } else {
                        if (timeToWait == 1) {
                            ent.setNoGravity(false);
                            ent.setVelocity(0, 0, 0);
                            ent.velocityChanged = false;
                            ent.fall(ent.getMaxFallHeight(), 0);
                            requireMiscStasisTime(ent).remStasisTime(1);
                        }

                    }
                }
            }

        }
    }


    @SubscribeEvent
    public static void onGetHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityLiving) {
            EntityLiving entity = ((EntityLiving) event.getEntity());
            requireStasisDamage(entity);
            requireStasisTime(entity);
            if (requireStasisTime(entity).getStasisTime() > 0) {
                event.setCanceled(true);
                addDamageFromEvent(entity, event.getAmount());
            }
        }
    }


    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            if ((event.getEntity() instanceof EntityLiving)) {
                EntityLiving entity = ((EntityLiving) event.getEntityLiving());
                if (!entity.world.isRemote) {
                    requireStasisTime(entity);
                    double currTime = requireStasisTime(entity).getStasisTime();
                    if (currTime > 1) {
                        requireStasisTime(entity).remStasisTime(1);
                    } else {
                        if (currTime == 1) {
                            entity.setNoAI(false);
                            entity.setNoGravity(false);
                            entity.setVelocity(0, 0, 0);
                            entity.velocityChanged = false;
                            requireStasisTime(entity).remStasisTime(1);
                            if (requireStasisDamage(entity).getDamageReceived() > 0) {
                                float damageToDeal = requireStasisDamage(entity).getDamageReceived();
                                requireStasisDamage(entity).resetDamageReceived();
                                entity.knockBack(entity, ((float) (damageToDeal * 0.4)), 0.5, 0.5);
                                entity.attackEntityFrom(DamageSource.MAGIC, damageToDeal);
                            }
                        }
                    }

                }
            }

        }


    }
}






