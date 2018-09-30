package com.github.kamefrede.rpsideas.util;

import com.github.kamefrede.rpsideas.util.botania.EnumManaTier;
import com.github.kamefrede.rpsideas.util.botania.IBlasterComponent;
import com.github.kamefrede.rpsideas.util.botania.IManaTrick;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public interface IComponentPiece {
    static
    @Nullable
    ItemStack firstMatchingPiece(ItemStack cad, Predicate<ItemStack> pred) {
        if (!(cad.getItem() instanceof ICAD))
            return null;
        ICAD cadItem = (ICAD) cad.getItem();
        for (EnumCADComponent component : EnumCADComponent.values()) {
            ItemStack compStack = cadItem.getComponentInSlot(cad, component);
            if (compStack != null) {
                if (pred.test(compStack)) return compStack;
            }
        }
        return null;
    }

    static
    @Nullable
    Object execute(SpellPiece piece, SpellContext context) throws SpellRuntimeException {
        if (!(piece instanceof IComponentPiece))
            return null;
        IComponentPiece componentPiece = (IComponentPiece) piece;

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        if (cad.isEmpty() || !(cad.getItem() instanceof ICAD))
            throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
        ICAD item = (ICAD) cad.getItem();

        boolean flag = false;

        for (EnumCADComponent type : EnumCADComponent.values()) {
            ItemStack component = item.getComponentInSlot(cad, type);

            if (component != null && component.getItem() instanceof ICADComponent) {
                ICADComponent compItem = (ICADComponent) component.getItem();
                if (compItem.getCADStatValue(component, EnumCADStat.POTENCY) < 0)
                    return componentPiece.executeIfAllowed(context);



                ITrickEnablerComponent.EnableResult result = componentPiece.acceptsPiece(component, cad, context, piece.spell, piece.x, piece.y);
                if(result == ITrickEnablerComponent.EnableResult.MISSING_REQUIREMENT){
                    flag = true;
                    break;
                } else if(result == ITrickEnablerComponent.EnableResult.SUCCESS){
                    return componentPiece.executeIfAllowed(context);
                } else if(result == ITrickEnablerComponent.EnableResult.NOT_ENABLED){
                    boolean isElven = ItemManaGun.hasClip(cad);
                    EnumManaTier cadTier = isElven ? EnumManaTier.ALFHEIM : EnumManaTier.BASE;

                    if(piece instanceof IManaTrick){
                        IManaTrick manaPiece = (IManaTrick) piece;
                        if(EnumManaTier.allowed(cadTier, manaPiece.tier())) {
                            int manaDrain = manaPiece.manaDrain(context, piece.x, piece.y);
                            ManaItemHandler.requestManaExact(cad, context.caster, manaDrain, true);
                            return componentPiece.executeIfAllowed(context);
                        }

                    } else if (((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.ASSEMBLY).getItem() instanceof IBlasterComponent) {
                        return componentPiece.executeIfAllowed(context);

                        } else{
                        flag= true;
                        break;
                    }
                }
            }
        }

        if (flag)
            throw new SpellRuntimeException("rpsideas.spellerror.trickdisabled");
        throw new SpellRuntimeException("rpsideas.spellerror.tricknotenabled");
    }

    static void addToTooltip(SpellPiece piece, List<String> tooltip) {
        if (!(piece instanceof IComponentPiece))
            return;
        IComponentPiece componentPiece = (IComponentPiece) piece;

        TooltipHelper.addToTooltip(tooltip, TextFormatting.GRAY + "%s", piece.getUnlocalizedDesc());

        for (String obj : componentPiece.requiredObjects())
            TooltipHelper.addToTooltip(tooltip,"rpsideas.spelldesc.requires", obj);


        TooltipHelper.addToTooltip(tooltip, "");
        String eval = piece.getEvaluationTypeString();
        TooltipHelper.addToTooltip(tooltip, "<- " + TextFormatting.GOLD + eval);

        for (SpellParam param : piece.paramSides.keySet()) {
            String pName = I18n.format(param.name);
            String pEval = param.getRequiredTypeString();
            TooltipHelper.addToTooltip(tooltip, (param.canDisable ? "[->] " : " ->  ") + TextFormatting.YELLOW + pName + " [" + pEval + "]");
        }

    }

    Object executeIfAllowed(SpellContext context) throws SpellRuntimeException;

    String[] requiredObjects();

    default ITrickEnablerComponent.EnableResult acceptsPiece(ItemStack component, ItemStack cad, SpellContext context, Spell spell, int x, int y) {
        return ITrickEnablerComponent.EnableResult.NOT_ENABLED;
    }
}
