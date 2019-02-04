package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemKeypad extends ItemMod {
    public ItemKeypad() {
        super(RPSItemNames.KEYPAD);
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            TooltipHelper.addToTooltip(tooltip, getTranslationKey(stack) + ".desc", Minecraft.getMinecraft().gameSettings.keyBindSneak.getDisplayName());
        });
    }
}
