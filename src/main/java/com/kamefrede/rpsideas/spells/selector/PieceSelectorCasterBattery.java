package com.kamefrede.rpsideas.spells.selector;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorCasterBattery extends PieceSelector {// TODO: 12/15/18 look at

    public PieceSelectorCasterBattery(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster == null) return null;
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        if (cad != null) {
            return (double) ((ICAD) cad.getItem()).getStatValue(cad, EnumCADStat.OVERFLOW);
        }
        return 0.0D;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
