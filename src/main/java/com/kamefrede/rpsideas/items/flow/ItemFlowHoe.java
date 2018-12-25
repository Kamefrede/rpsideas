package com.kamefrede.rpsideas.items.flow;

import com.kamefrede.rpsideas.items.ItemPsimetalHoe;
import com.kamefrede.rpsideas.util.helpers.IFlowColorAcceptor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Calendar;

/**
 * @author WireSegal
 * Created at 10:59 AM on 12/16/18.
 */
public class ItemFlowHoe extends ItemPsimetalHoe implements IFlowColorAcceptor {
    private static boolean xmas;

    public ItemFlowHoe(String name) {
        super(name);

        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.MONTH) + 1 == 12 &&
                calendar.get(Calendar.DATE) >= 24 &&
                calendar.get(Calendar.DATE) <= 26)
            xmas = true;
    }

    @Nonnull
    @Override
    public String getTranslationKey(@Nonnull ItemStack stack) {
        if (xmas)
            return super.getTranslationKey(stack) + "_xmas";
        return super.getTranslationKey(stack);
    }
}
