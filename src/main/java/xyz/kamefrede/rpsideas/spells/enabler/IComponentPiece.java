package xyz.kamefrede.rpsideas.spells.enabler;

import xyz.kamefrede.rpsideas.spells.enabler.ITrickEnablerComponent.EnableResult;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;

import javax.annotation.Nullable;
import java.util.List;

public interface IComponentPiece {

    @Nullable
    default Object execute(SpellContext context) throws SpellRuntimeException {
        if (!(this instanceof SpellPiece))
            return null;

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        if (cad.isEmpty() || !(cad.getItem() instanceof ICAD))
            throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
        ICAD item = (ICAD) cad.getItem();

        boolean disabled = false;

        boolean resourcesMissing = false;

        for (EnumCADComponent type : EnumCADComponent.values()) {
            ItemStack component = item.getComponentInSlot(cad, type);

            if (!component.isEmpty() && component.getItem() instanceof ICADComponent) {
                ICADComponent compItem = (ICADComponent) component.getItem();
                if (compItem.getCADStatValue(component, EnumCADStat.POTENCY) < 0)
                    return executeIfAllowed(context);
                else if (compItem instanceof ITrickEnablerComponent) {
                    EnableResult result = acceptsPiece((ITrickEnablerComponent) compItem, component, cad, context, false);

                    if (result == EnableResult.MISSING_REQUIREMENT)
                        resourcesMissing = true;
                    else if (result == EnableResult.NOT_ENABLED)
                        disabled = true;
                    else
                        return executeIfAllowed(context);
                }
            }
        }

        if (resourcesMissing)
            throw new SpellRuntimeException(noResources());

        if (disabled)
            throw new SpellRuntimeException("rpsideas.spellerror.trickdisabled");

        throw new SpellRuntimeException("rpsideas.spellerror.tricknotenabled");
    }

    default void addToTooltip(List<String> tooltip) {
        if (!(this instanceof SpellPiece))
            return;
        SpellPiece piece = (SpellPiece) this;

        tooltip.add(TextFormatting.GRAY + TooltipHelper.local(piece.getUnlocalizedDesc()));

        for (String obj : requiredObjects())
            TooltipHelper.addToTooltip(tooltip, "rpsideas.spelldesc.requires", TooltipHelper.local(obj));


        tooltip.add("");
        String eval = piece.getEvaluationTypeString();
        tooltip.add("<- " + TextFormatting.GOLD + eval);

        for (SpellParam param : piece.paramSides.keySet()) {
            String pName = I18n.format(param.name);
            String pEval = param.getRequiredTypeString();
            tooltip.add((param.canDisable ? "[->] " : " ->  ") + TextFormatting.YELLOW + pName + " [" + pEval + "]");
        }
    }

    String noResources();

    Object executeIfAllowed(SpellContext context) throws SpellRuntimeException;

    String[] requiredObjects();

    boolean drainResources(ITrickEnablerComponent enabler, ItemStack component, ItemStack cad, SpellContext context, boolean simulate);

    default EnableResult acceptsPiece(ITrickEnablerComponent enabler, ItemStack component, ItemStack cad, SpellContext context, boolean simulate) {
        boolean enables = enabler.enables(cad, component, (SpellPiece) this);
        if (enables)
            return EnableResult.fromBoolean(drainResources(enabler, component, cad, context, simulate));
        return EnableResult.NOT_ENABLED;
    }
}
