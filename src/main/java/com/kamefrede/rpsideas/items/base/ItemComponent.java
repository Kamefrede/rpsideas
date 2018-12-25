package com.kamefrede.rpsideas.items.base;

import com.kamefrede.rpsideas.RPSIdeas;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADComponent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class ItemComponent extends ItemMod implements ICADComponent {
    private Map<EnumCADStat, Integer> stats = new HashMap<>();

    public ItemComponent(String name) {
        super(name);
        setMaxStackSize(1);
        registerStats();
    }

    protected void registerStats() {
        //No-op by default
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
        tooltip.add(I18n.format(RPSIdeas.MODID + ".misc.hold", TextFormatting.AQUA + KeyModifier.SHIFT.name() + TextFormatting.RESET));
        if (!GuiScreen.isShiftKeyDown()) return;
        tooltip.remove(1);

        EnumCADComponent componentType = getComponentType(stack);
        String componentName = I18n.format(componentType.getName());
        tooltip.add(TextFormatting.GREEN + I18n.format("rpsideas.componentType") + " " + TextFormatting.GRAY + componentName);

        addTooltipTags(tooltip);

        for (EnumCADStat cadStat : EnumCADStat.values()) {
            if (cadStat.getSourceType() == componentType) {
                int statValue = getCADStatValue(stack, cadStat);
                String statValueString = statValue == -1 ? "∞" : String.valueOf(statValue);
                String statName = I18n.format(cadStat.getName());
                tooltip.add(" " + TextFormatting.AQUA + statName + TextFormatting.GRAY + ": " + statValueString);
            }
        }
    }

    protected void addTooltipTags(List<String> tooltip) {
        //NO-OP
    }

    protected void addTooltipTag(boolean positiveEffect, List<String> tooltip, String descriptionTranslationKey, Object... descriptionFormatArgs) {
        String nameFormatted = I18n.format(RPSIdeas.MODID + ".cadstat." + (positiveEffect ? "extra" : "downside"));

        if (descriptionFormatArgs == null) descriptionFormatArgs = new String[0];
        String descriptionFormatted = I18n.format(descriptionTranslationKey, descriptionFormatArgs);

        TextFormatting color = positiveEffect ? TextFormatting.AQUA : TextFormatting.RED;

        tooltip.add(" " + color + nameFormatted + ": " + TextFormatting.GRAY + descriptionFormatted);
    }

    protected void addStat(EnumCADStat stat, int value) {
        stats.put(stat, value);
    }

    @Override
    public int getCADStatValue(ItemStack stack, EnumCADStat stat) {
        return stats.getOrDefault(stat, 0);
    }
}
