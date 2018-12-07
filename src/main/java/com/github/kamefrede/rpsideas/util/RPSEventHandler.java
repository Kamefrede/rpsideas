package com.github.kamefrede.rpsideas.util;

import com.github.kamefrede.rpsideas.items.ItemPsimetalRod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RPSEventHandler {

    public static class Handler{
        @SubscribeEvent
        public void fishEvent(ItemFishedEvent event){
            EntityPlayer player = event.getEntityPlayer();
            ItemStack stack = player.inventory.getCurrentItem();
            Vec3d pos = new Vec3d(event.getHookEntity().posX, event.getHookEntity().posY, event.getHookEntity().posZ);
            ItemPsimetalRod.castSpell(player, stack, pos);

        }
    }

}
