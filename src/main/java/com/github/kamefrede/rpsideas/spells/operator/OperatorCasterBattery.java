package com.github.kamefrede.rpsideas.spells.operator;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class OperatorCasterBattery extends PieceOperator {

    public OperatorCasterBattery(Spell spell){
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.getEntityWorld().isRemote) return null;
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        if (cad != null){
            Double battery =(double) ((ICAD) cad.getItem()).getStatValue(cad, EnumCADStat.OVERFLOW);
            return battery;
        }
        return null;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
