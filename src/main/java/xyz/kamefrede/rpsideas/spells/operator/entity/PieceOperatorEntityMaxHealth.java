package xyz.kamefrede.rpsideas.spells.operator.entity;

import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.EntityLivingBase;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorEntityMaxHealth extends PieceOperator {
    private SpellParam entity;

    public PieceOperatorEntityMaxHealth(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(entity = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityLivingBase e = SpellHelpers.ensureNonnullAndLivingEntity(this, context, entity);

        return e.getMaxHealth() * 1.0;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
