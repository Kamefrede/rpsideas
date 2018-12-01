package com.github.kamefrede.rpsideas.items.components;

import com.github.kamefrede.rpsideas.items.base.ItemComponent;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

import javax.annotation.Nonnull;
import java.util.ConcurrentModificationException;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemTwinflowBattery extends ItemComponent {
    private static final int PSI_REGEN_BONUS = 5;

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.OVERFLOW,500);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.BATTERY;
    }

    @Override
    protected void addTooltipTags(List<String> tooltip) {
        addTooltipTag(true, tooltip, Reference.MODID + ".upsides.boost_regen", PSI_REGEN_BONUS);
        addTooltipTag(true, tooltip, Reference.MODID + ".upsides.fills_last");
    }



    @SubscribeEvent
    public static void onDamage(LivingHurtEvent e) {
        EntityLivingBase living = e.getEntityLiving();
        if (living.world.isRemote) return;
        if (!(living instanceof EntityPlayer)) return;
        if (e.getAmount() < 1) return;

        EntityPlayer player = (EntityPlayer) living;
        ItemStack cad = PsiAPI.getPlayerCAD(player);

        if (!cad.isEmpty()) {
            ICAD icad = (ICAD) cad.getItem();

            ItemStack battery = icad.getComponentInSlot(cad, EnumCADComponent.BATTERY);
            if (!battery.isEmpty() && battery.getItem() instanceof ItemTwinflowBattery) {
                PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
                int psi = (int) (data.getTotalPsi() * 0.02D * (double) e.getAmount());
                if (psi > 0 && data.availablePsi > 0) {
                    int cd = psi /( data.getRegenPerTick() + 5);
                    if(cd > 50) cd = 50;
                    if(cd < 10) cd = 10;
                    psi = Math.min(psi, data.availablePsi);
                    data.deductPsi(-psi, cd, true);
                    data.save();
                    NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP)player);
                }
            }
        }
    }

    @Nonnull
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if(!e.side.isServer()) return;

        EntityPlayer player = e.player;
        ItemStack cad = PsiAPI.getPlayerCAD(player);

        if(!cad.isEmpty()) {
            ICAD icad = (ICAD) cad.getItem();
            ItemStack battery = icad.getComponentInSlot(cad, EnumCADComponent.BATTERY);

            if(!battery.isEmpty() && battery.getItem() instanceof ItemTwinflowBattery) {
                try{
                    PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
                    if(data.regenCooldown < 1 && data.availablePsi != data.getTotalPsi()) {
                        data.availablePsi = Math.min(data.getTotalPsi(), data.availablePsi + PSI_REGEN_BONUS);
                        data.save();
                        NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP)player);
                    }
                } catch (ConcurrentModificationException except){
                    //this is probably one of the hackiest things i've done
                }




            }
        }
    }
}
