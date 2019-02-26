package com.kamefrede.rpsideas.spells.enabler.styles;

import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;

import java.util.List;

public interface IStyledPiece {

    default void addToTooltip(List<String> tooltip) {
        if (!(this instanceof SpellPiece))
            return;
        SpellPiece piece = (SpellPiece) this;

        tooltip.add(TextFormatting.GRAY + TooltipHelper.local(piece.getUnlocalizedDesc()));

        String key = requiresStyle() ? "rpsideas.spelldesc.requires" : "rpsideas.spelldesc.enhanced";


        TooltipHelper.addToTooltip(tooltip, key, style().color + TooltipHelper.local(style().translationKey));


        tooltip.add("");
        String eval = piece.getEvaluationTypeString();
        tooltip.add("<- " + TextFormatting.GOLD + eval);

        for (SpellParam param : piece.paramSides.keySet()) {
            String pName = I18n.format(param.name);
            String pEval = param.getRequiredTypeString();
            tooltip.add((param.canDisable ? "[->] " : " ->  ") + TextFormatting.YELLOW + pName + " [" + pEval + "]");
        }
    }

    default boolean hasStyle(SpellContext context) {
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        return !cad.isEmpty() && cad.getItem() instanceof IStyledAssembly &&
                ((IStyledAssembly) cad.getItem()).enablesStyle(cad, style());
    }

    EnumAssemblyStyle style();

    default boolean requiresStyle() {
        return false;
    }

}
