package com.github.kamefrede.psiaddon.handler;

import com.github.kamefrede.psiaddon.capability.stasis.damage.IStasisDamage;

import com.github.kamefrede.psiaddon.capability.stasis.time.IStasisTime;
import com.github.kamefrede.psiaddon.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.github.kamefrede.psiaddon.capability.stasis.damage.LibDamage.addDamageFromEvent;
import static com.github.kamefrede.psiaddon.capability.stasis.damage.LibDamage.requireStasisDamage;
import static com.github.kamefrede.psiaddon.capability.stasis.damage.StasisDamageStorage.CAPABILITY_STASIS_DAMAGE;
import static com.github.kamefrede.psiaddon.capability.stasis.time.LibTime.remTimeFromEvent;
import static com.github.kamefrede.psiaddon.capability.stasis.time.LibTime.requireStasisTime;
import static com.github.kamefrede.psiaddon.capability.stasis.time.StasisTimeStorage.CAPABILITY_STASIS_TIME;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class StasisTickHandler {

    @SubscribeEvent
    public static void onGetHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityLiving) {
            EntityLiving entity = ((EntityLiving) event.getEntity());
            requireStasisDamage(entity);
            requireStasisTime(entity);
            if(requireStasisTime(entity).getStasisTime() > 0){
                event.setCanceled(true);
                addDamageFromEvent(entity, event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event){
        if(!(event.getEntity() instanceof EntityPlayer)){
            EntityLiving entity = ((EntityLiving)event.getEntityLiving());
            if(!entity.world.isRemote){
                requireStasisTime(entity);
                double currTime = requireStasisTime(entity).getStasisTime();
                if(currTime > 1){
                    requireStasisTime(entity).remStasisTime(1);
                } else {
                    if(currTime == 1){
                        if(entity.hasCapability(CAPABILITY_STASIS_DAMAGE, null)){
                            entity.setNoAI(false);
                            entity.setNoGravity(false);
                            entity.setVelocity(0,0,0);
                            entity.velocityChanged = false;
                            float damageToDeal = requireStasisDamage(entity).getDamageReceived();
                            requireStasisDamage(entity).resetDamageReceived();
                            requireStasisTime(entity).remStasisTime(1);
                            entity.attackEntityFrom(DamageSource.FALL, damageToDeal);
                        } else {
                            entity.setNoAI(false);
                            entity.setNoGravity(false);
                            entity.setVelocity(0,0,0);
                            entity.velocityChanged = false;
                        }
                    }
                }



            }
        }

    }









    }






