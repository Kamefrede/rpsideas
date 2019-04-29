package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.entity.EntityHailParticle;
import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickConjureHail extends PieceTrick {

    private SpellParam position;
    private SpellParam power;

    public PieceTrickConjureHail(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        double pwr = SpellHelpers.ensurePositiveAndNonzero(this, power);
        if (pwr > 1)
            throw new SpellCompilationException(SpellCompilationExceptions.POWER, x, y);
        meta.addStat(EnumSpellStat.COST, (int) (pwr * 300));
        meta.addStat(EnumSpellStat.POTENCY, (int) (pwr * 15));
    }

    @Override
    public void initParams() {
        super.initParams();
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
        addParam(power = new ParamNumber(SpellParams.GENERIC_NAME_POWER, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double pwr = SpellHelpers.getNumber(this, context, power, 0.5);
        BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, true, false);

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);

        EntityHailParticle entity = new EntityHailParticle(context.caster.world);
        entity.createParticle(context.caster, colorizer, pos, (float) pwr);
        entity.world.spawnEntity(entity);
        return super.execute(context);
    }
}
