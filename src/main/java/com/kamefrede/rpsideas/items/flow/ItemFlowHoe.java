package com.kamefrede.rpsideas.items.flow;

import com.kamefrede.rpsideas.items.ItemPsimetalHoe;
import com.kamefrede.rpsideas.util.helpers.IFlowColorAcceptor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.util.Calendar;

/**
 * @author WireSegal
 * Created at 10:59 AM on 12/16/18.
 */
@Mod.EventBusSubscriber
public class ItemFlowHoe extends ItemPsimetalHoe implements IFlowColorAcceptor {
    private static boolean xmas;

    public ItemFlowHoe(String name) {
        super(name);
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            Calendar date = e.world.getCurrentDate();
            xmas = (date.get(Calendar.MONTH) == Calendar.DECEMBER && date.get(Calendar.DATE) >= 25) ||
                    (date.get(Calendar.MONTH) == Calendar.JANUARY && date.get(Calendar.DATE) <= 5);
        }
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        if (xmas)
            return super.getTranslationKey(stack) + "_xmas";
        return super.getTranslationKey(stack);
    }
}
