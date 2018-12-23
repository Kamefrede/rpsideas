package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.ItemPsimetalRod;
import com.kamefrede.rpsideas.items.base.IRegenerationBattery;
import com.kamefrede.rpsideas.network.MessageCastOffHand;
import com.kamefrede.rpsideas.network.MessageChangeSocketSlot;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;


@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSEventHandler {

    public static final int DEFAULT_REGEN_RATE = 25;
    public static final String REGEN_KEY = "rpsideasRegen";
    public static final String REGEN_BEFORE_KEY = "rpsideasRegenBefore";

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.inventory.getCurrentItem();
        Vec3d pos = new Vec3d(event.getHookEntity().posX, event.getHookEntity().posY, event.getHookEntity().posZ);
        ItemPsimetalRod.castSpell(player, stack, pos);
    }

    @SubscribeEvent
    public static void keyDown(InputEvent.KeyInputEvent ev){
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.currentScreen == null){
            RPSKeybind[] slotbindings = RPSKeybindHandler.keybinds;
            RPSKeybind offhandbinding = RPSKeybindHandler.offhandCast;
            if(offhandbinding.isPressed()){
                RPSPacketHandler.sendToServer(new MessageCastOffHand());
            }
            for(int i = 0; i < slotbindings.length; i++){
                if(slotbindings[i].isPressed()){
                    RPSPacketHandler.sendToServer(new MessageChangeSocketSlot(i));
                }
            }
        }
    }



    @SubscribeEvent
    public static void updateRegenRate(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntityLiving();
            ItemStack cad = PsiAPI.getPlayerCAD(player);
            PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
            if (data != null && data.getCustomData() != null) {

                int extraRegen = data.getCustomData().getInteger(REGEN_KEY);
                data.getCustomData().setInteger(REGEN_BEFORE_KEY, data.regen);

                int regenBefore = data.getCustomData().getInteger(REGEN_BEFORE_KEY);
                if (regenBefore == 0) regenBefore = DEFAULT_REGEN_RATE;

                int regenNow = DEFAULT_REGEN_RATE;
                if (regenBefore != extraRegen + DEFAULT_REGEN_RATE)
                    regenNow = regenBefore - extraRegen;


                extraRegen = 0;
                if (!cad.isEmpty()) {
                    ICAD cadItem = (ICAD) cad.getItem();
                    ItemStack battery = cadItem.getComponentInSlot(cad, EnumCADComponent.BATTERY);
                    if (battery.getItem() instanceof IRegenerationBattery)
                        extraRegen = ((IRegenerationBattery) battery.getItem()).getRegenerationValue(battery);
                }

                if (regenNow + extraRegen == data.regen)
                    return;

                data.getCustomData().setInteger(REGEN_KEY, extraRegen);
                data.regen = Math.max(DEFAULT_REGEN_RATE, regenNow + extraRegen);

                data.save();
                if (player instanceof EntityPlayerMP)
                    NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) player);
            }
        }
    }

}
