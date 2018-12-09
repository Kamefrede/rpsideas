package com.github.kamefrede.rpsideas.util;

import com.github.kamefrede.rpsideas.items.ItemPsimetalRod;
import com.github.kamefrede.rpsideas.items.components.ItemTwinflowBattery;
import com.github.kamefrede.rpsideas.items.components.ItemUnstableBattery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

import java.util.ConcurrentModificationException;

@Mod.EventBusSubscriber
public class RPSEventHandler {

    public static class Handler{

        public  int defaultRegen = 25;
        public final String regenKey = "rpsideasRegen";
        public final String regenBefore = "rpsideasRegenBefore";
        public  int unstableRegen = 10;
        public  int shieldedRegen = 5;
        @SubscribeEvent
        public void fishEvent(ItemFishedEvent event){
            EntityPlayer player = event.getEntityPlayer();
            ItemStack stack = player.inventory.getCurrentItem();
            Vec3d pos = new Vec3d(event.getHookEntity().posX, event.getHookEntity().posY, event.getHookEntity().posZ);
            ItemPsimetalRod.castSpell(player, stack, pos);

        }

        @SubscribeEvent
        public void onPlayerTick(LivingEvent.LivingUpdateEvent ev){
            try{
                if(ev.getEntityLiving() instanceof EntityPlayer){
                    EntityPlayer player = (EntityPlayer) ev.getEntityLiving();
                    ItemStack cad = PsiAPI.getPlayerCAD(player);
                    PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
                    if(data != null && data.getCustomData() != null){
                        Integer currExtraRegen = data.getCustomData().getInteger(regenKey);
                        data.getCustomData().setInteger(regenBefore, data.regen);
                        Integer regenb4 = data.getCustomData().getInteger(regenBefore);
                        if(regenb4 == 0 || regenb4 == null) regenb4 = defaultRegen;
                        Integer regenNow = 0;
                        if(regenb4 == currExtraRegen + defaultRegen){
                            regenNow = defaultRegen;
                        }
                        if(regenb4 != currExtraRegen + defaultRegen){
                            regenNow = regenb4 - currExtraRegen;
                        } else {
                            regenNow = defaultRegen;
                        }


                        currExtraRegen = 0;
                        if(!cad.isEmpty()) {
                            ICAD icad = (ICAD) cad.getItem();
                            ItemStack battery = icad.getComponentInSlot(cad, EnumCADComponent.BATTERY);
                            if(battery.getItem() instanceof ItemTwinflowBattery){
                                currExtraRegen = shieldedRegen;
                            }
                            else if(battery.getItem() instanceof ItemUnstableBattery){
                                currExtraRegen = unstableRegen;
                            }
                        }

                        if(regenNow + currExtraRegen ==  data.regen) return;
                        data.getCustomData().setInteger(regenKey, currExtraRegen);
                        data.regen = Math.max(defaultRegen, regenNow + currExtraRegen);
                        data.save();
                        if(player instanceof EntityPlayerMP){
                            NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP)player);
                        }
                    }
                    }

            } catch (ConcurrentModificationException except){
                //huge meme
            }
        }
    }

}
