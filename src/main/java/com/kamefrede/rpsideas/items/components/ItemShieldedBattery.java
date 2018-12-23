package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

import java.util.List;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemShieldedBattery extends ItemComponent {

    public ItemShieldedBattery() {
        super(RPSItemNames.SHIELDED_BATTERY);
    }

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent e) {
        EntityLivingBase living = e.getEntityLiving();
        if (living.world.isRemote) return;
        if (!(living instanceof EntityPlayer)) return;
        if (e.getAmount() < 1) return;

        EntityPlayer player = (EntityPlayer) living;
        ItemStack cad = PsiAPI.getPlayerCAD(player);
        PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);

        if (data != null && !cad.isEmpty()) {
            ICAD icad = (ICAD) cad.getItem();

            ItemStack battery = icad.getComponentInSlot(cad, EnumCADComponent.BATTERY);
            if (!battery.isEmpty() && battery.getItem() instanceof ItemShieldedBattery) {
                int psi = (int) (data.getTotalPsi() * 0.02D * (double) e.getAmount());
                if (psi > 0 && data.availablePsi > 0) {
                    int cd = psi / (data.getRegenPerTick() + 5);
                    if (cd > 50) cd = 50;
                    if (cd < 10) cd = 10;
                    psi = Math.min(psi, data.availablePsi);
                    data.deductPsi(-psi, cd, true);
                    data.save();
                    NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) player);
                }

            }
        }
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.BATTERY;
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.OVERFLOW, 200);
    }

    @Override
    protected void addTooltipTags(List<String> tooltip) {
        addTooltipTag(true, tooltip, RPSIdeas.MODID + ".upsides.soaks_damage");
        addTooltipTag(false, tooltip, RPSIdeas.MODID + ".downsides.locks_regen");
    }
}
