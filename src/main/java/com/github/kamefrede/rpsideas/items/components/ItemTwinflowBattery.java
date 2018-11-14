package com.github.kamefrede.rpsideas.items.components;

import com.github.kamefrede.rpsideas.items.base.ItemComponent;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.helpers.PsiChangeHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.List;

@Mod.EventBusSubscriber
public class ItemTwinflowBattery extends ItemComponent {
    private static final int PSI_REGEN_BONUS = 5;

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.OVERFLOW, 500);
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
                    psi = Math.min(psi, data.availablePsi);
                    data.regenCooldown = 0;
                    data.deductPsi(-psi, 50, true);
                    System.out.println("this worked");
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if(!e.side.isServer()) return;

        EntityPlayer player = e.player;
        ItemStack cad = PsiAPI.getPlayerCAD(player);

        if(!cad.isEmpty()) {
            ICAD icad = (ICAD) cad.getItem();
            ItemStack battery = icad.getComponentInSlot(cad, EnumCADComponent.BATTERY);

            if(!battery.isEmpty() && battery.getItem() instanceof ItemTwinflowBattery) {
                PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);

                if(data.regenCooldown == 0 && data.availablePsi != data.getTotalPsi()) {
                    PsiChangeHelper.changePsi(player, PSI_REGEN_BONUS, true);
                }


            }
        }
    }
}
