package com.kamefrede.rpsideas.items.base;

import com.kamefrede.rpsideas.RPSIdeas;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.teamwizardry.librarianlib.features.kotlin.CommonUtilMethods.times;


public abstract class ItemComponent extends ItemMod implements ICADComponent {
    private Map<EnumCADStat, Integer> stats = new HashMap<>();

    public ItemComponent(String name) {
        super(name);
        setMaxStackSize(1);
        registerStats();
    }

    protected void registerStats() {
        // NO-OP
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            EnumCADComponent componentType = getComponentType(stack);
            TooltipHelper.addToTooltip(tooltip, "psimisc.componentType", TooltipHelper.local(componentType.getName()));

            addTooltipTags(Minecraft.getMinecraft(), world,
                    Minecraft.getMinecraft().gameSettings.keyBindSneak,
                    stack, tooltip, advanced);

            for (EnumCADStat cadStat : EnumCADStat.values()) {
                if (cadStat.getSourceType() == componentType) {
                    int statValue = getCADStatValue(stack, cadStat);
                    String statValueString = statValue == -1 ? "∞" : String.valueOf(statValue);
                    String statName = I18n.format(cadStat.getName());
                    tooltip.add(" " + TextFormatting.AQUA + statName + TextFormatting.GRAY + ": " + statValueString);
                }
            }
        });
    }

    @SideOnly(Side.CLIENT)
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        // NO-OP
    }

    @SideOnly(Side.CLIENT)
    protected final void addTooltipTagSubLineRaw(@Nonnull List<String> tooltip, String prefix, String rawValue) {
        String nameFormatted = I18n.format(prefix);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int length = fontRenderer.getStringWidth(nameFormatted + ":  ");
        int space = fontRenderer.getStringWidth(" ");
        String padding = times(" ", (int) Math.ceil(length * 1.0 / space));

        tooltip.add(padding + TextFormatting.GRAY + rawValue);
    }

    @SideOnly(Side.CLIENT)
    protected final void addTooltipTagRaw(@Nonnull List<String> tooltip, TextFormatting color, String prefix, String rawValue) {
        String nameFormatted = I18n.format(prefix);

        tooltip.add(" " + color + nameFormatted + ": " + TextFormatting.GRAY + rawValue);
    }

    @SideOnly(Side.CLIENT)
    protected final void addTooltipTag(@Nonnull List<String> tooltip, TextFormatting color, String prefix, String key, Object... format) {
        if (format == null) format = new String[0];
        String descriptionFormatted = I18n.format(key, format);

        addTooltipTagRaw(tooltip, color, prefix, descriptionFormatted);
    }

    @SideOnly(Side.CLIENT)
    protected final void addTooltipTagSubLine(@Nonnull List<String> tooltip, String prefix, String key, Object... format) {
        if (format == null) format = new String[0];
        String formatted = I18n.format(key, format);

        addTooltipTagSubLineRaw(tooltip, prefix, formatted);
    }

    @SideOnly(Side.CLIENT)
    protected void addTooltipTag(List<String> tooltip, boolean positiveEffect, String descriptionTranslationKey, Object... descriptionFormatArgs) {
        addTooltipTag(tooltip,
                positiveEffect ? TextFormatting.AQUA : TextFormatting.RED,
                RPSIdeas.MODID + ".cadstat." + (positiveEffect ? "extra" : "downside"),
                descriptionTranslationKey, descriptionFormatArgs);
    }

    protected void addStat(EnumCADStat stat, int value) {
        stats.put(stat, value);
    }

    @Override
    public int getCADStatValue(ItemStack stack, EnumCADStat stat) {
        return stats.getOrDefault(stat, 0);
    }
}
