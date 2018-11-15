package com.github.kamefrede.rpsideas.util;

import com.github.kamefrede.rpsideas.items.components.botania.ItemBlasterAssembly;
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
    Object execute(SpellPiece piece, SpellContext context) throws SpellRuntimeException {
        if (!(piece instanceof IComponentPiece))
            return null;
        IComponentPiece componentPiece = (IComponentPiece) piece;

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        if (cad.isEmpty() || !(cad.getItem() instanceof ICAD))
            throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
        ICAD item = (ICAD) cad.getItem();

        boolean flag = false;

        boolean mana = false;



        for (EnumCADComponent type : EnumCADComponent.values()) {
            ItemStack component = item.getComponentInSlot(cad, type);

            if (component != null && component.getItem() instanceof ICADComponent) {
                ICADComponent compItem = (ICADComponent) component.getItem();
                if (compItem.getCADStatValue(component, EnumCADStat.POTENCY) < 0)
                    return componentPiece.executeIfAllowed(context);

                if(compItem instanceof ItemBlasterAssembly && piece instanceof IManaTrick){
                    boolean isElven = ItemManaGun.hasClip(cad);
                    EnumManaTier cadTier = isElven ? EnumManaTier.ALFHEIM : EnumManaTier.BASE;
                    IManaTrick manapiece = (IManaTrick) piece;
                    if(EnumManaTier.allowed(cadTier, manapiece.tier())){
                        int manaDrain = manapiece.manaDrain(context, piece.x, piece.y);
                        if(ManaItemHandler.requestManaExact(cad, context.caster, manaDrain, false)){
                            ManaItemHandler.requestManaExact(cad, context.caster, manaDrain, true);
                            return componentPiece.executeIfAllowed(context);
                        }
                        mana = true;
                        break;
                    }
                    flag = true;
                    break;

                }
            }
        }

        if(mana) throw new SpellRuntimeException("rpsideas.spellerror.nomana");

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
        return ITrickEnablerComponent.EnableResult.SUCCESS;
    }
}
