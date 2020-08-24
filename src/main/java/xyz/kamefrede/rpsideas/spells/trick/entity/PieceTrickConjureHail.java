package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.entity.EntityHailParticle;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickConjureHail extends PieceTrick {

    private SpellParam position;
    private SpellParam power;
    private static final float powerMultiplier = 5f;

    public PieceTrickConjureHail(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        double pwr = SpellHelpers.ensurePositiveAndNonzero(this, power);
        pwr /= powerMultiplier;
        meta.addStat(EnumSpellStat.COST, (int) (Math.max(1, pwr) * pwr * 150));
        meta.addStat(EnumSpellStat.POTENCY, (int) Math.ceil(Math.max(1,pwr) * pwr * 20));
    }

    @Override
    public void initParams() {
        super.initParams();
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
        addParam(power = new ParamNumber(SpellParams.GENERIC_NAME_POWER, SpellParam.BLUE, false, true));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double pwr = SpellHelpers.getNumber(this, context, power, 0.5);
        pwr /= powerMultiplier;
        Vector3 pos = SpellHelpers.checkPos(this, context, position, true, true, false);

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);

        EntityHailParticle entity = new EntityHailParticle(context.caster.world);
        entity.createParticle(context.caster, colorizer, pos, (float) pwr);
        entity.world.spawnEntity(entity);
        return null;
    }
}
