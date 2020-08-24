package xyz.kamefrede.rpsideas.items.components;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.items.base.IRegenerationBattery;
import xyz.kamefrede.rpsideas.items.base.ItemComponent;
import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.RegenPsiEvent;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author WireSegal
 * Created at 10:46 AM on 12/16/18.
 */
@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemTwinflowBattery extends ItemComponent implements IRegenerationBattery {
    private static final int PSI_REGEN_BONUS = 5;

    public ItemTwinflowBattery() {
        super(RPSItemNames.TWINFLOW_BATTERY);
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
                        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
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

    @Override
    public int getRegenerationValue(ItemStack stack) {
        return PSI_REGEN_BONUS;
    }

    @Override
    public void alterRegenerationBehavior(ItemStack stack, RegenPsiEvent event) {
        IRegenerationBattery.super.alterRegenerationBehavior(stack, event);
        event.regenCadFirst(false);
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.OVERFLOW, 500);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        addTooltipTag(tooltip, true, RPSIdeas.MODID + ".upsides.boost_regen", PSI_REGEN_BONUS);
        addTooltipTag(tooltip, true, RPSIdeas.MODID + ".upsides.fills_last");
    }
}
