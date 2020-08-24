package xyz.kamefrede.rpsideas.spells.enabler.styles;

import net.minecraft.util.text.TextFormatting;

import java.util.Locale;

/**
 * @author WireSegal
 * Created at 11:35 PM on 2/25/19.
 */
public enum EnumAssemblyStyle {
    INFILTRATION(TextFormatting.DARK_AQUA), GRENADIER(TextFormatting.DARK_RED), SNIPER(TextFormatting.AQUA);

    public final String translationKey = "rpsideas.style." + name().toLowerCase(Locale.ROOT);
    public final TextFormatting color;

    EnumAssemblyStyle(TextFormatting color) {
        this.color = color;
    }
}
