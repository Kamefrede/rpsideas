package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.IRegenerationBattery;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemUnstableBattery extends ItemComponent implements IRegenerationBattery {
    private static final int PSI_REGEN_BONUS = 10;

    public ItemUnstableBattery() {
        super(RPSItemNames.UNSTABLE_BATTERY);
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
            if (!battery.isEmpty() && battery.getItem() instanceof ItemUnstableBattery) {
                data.availablePsi = 0;
                data.regenCooldown = 20;
                data.save();
                NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) player);
            }
        }
    }

    @Override
    public int getRegenerationValue(ItemStack stack) {
        return PSI_REGEN_BONUS;
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.OVERFLOW, 800);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.BATTERY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        addTooltipTag(tooltip, true, RPSIdeas.MODID + ".upsides.boost_regen", PSI_REGEN_BONUS);
        addTooltipTag(tooltip, false, RPSIdeas.MODID + ".downsides.on_damage");
    }


}
