package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.effect.base.PotionPsiChange;
import com.kamefrede.rpsideas.items.base.IRegenerationBattery;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.LibItemNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.List;

/**
 * @author WireSegal
 * Created at 10:46 AM on 12/16/18.
 */
@Mod.EventBusSubscriber
public class ItemTwinflowBattery extends ItemComponent implements IRegenerationBattery {
    private static final int PSI_REGEN_BONUS = 5;

    public ItemTwinflowBattery() {
        super(LibItemNames.TWINFLOW_BATTERY);
    }

    @Override
    public int getRegenerationValue(ItemStack stack) {
        return PSI_REGEN_BONUS;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            EntityPlayer player = e.player;
            if (e.side.isServer()) {
                ItemStack cad = PsiAPI.getPlayerCAD(player);
                if (!cad.isEmpty()) {
                    ICAD item = (ICAD) cad.getItem();
                    ItemStack battery = item.getComponentInSlot(cad, EnumCADComponent.BATTERY);

                    if (!battery.isEmpty() && battery.getItem() instanceof ItemTwinflowBattery) {
                        PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);

                        if (data != null) {
                            if (data.regenCooldown == 0)
                                PotionPsiChange.addPsiToPlayer(player, 5 +
                                        (data.availablePsi != data.totalPsi ? (int) Math.ceil(data.regen / 2.0) : 0), false);

                            int amountToDump = Math.min(data.totalPsi - data.availablePsi, item.getStoredPsi(cad));
                            if (amountToDump > 0) {
                                data.deductPsi(-amountToDump, 0, true);
                                item.consumePsi(cad, amountToDump);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.OVERFLOW, 500);
    }

    @Override
    protected void addTooltipTags(List<String> tooltip) {
        addTooltipTag(true, tooltip, RPSIdeas.MODID + ".upsides.boost_regen", PSI_REGEN_BONUS);
        addTooltipTag(true, tooltip, RPSIdeas.MODID + ".upsides.fills_last");
    }
}
