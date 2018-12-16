package com.kamefrede.rpsideas.spells.operator.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetSignText extends PieceOperator {

    private SpellParam position;

    public PieceOperatorGetSignText(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        if (position == null) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (!context.isInRadius(positionVal)) throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
        TileEntity sign = context.caster.world.getTileEntity(pos);
        if (sign instanceof TileEntitySign) {
            StringBuilder s = new StringBuilder();
            boolean any = false;
            for (ITextComponent component : ((TileEntitySign) sign).signText)
                if (!component.getUnformattedText().isEmpty()) {
                    if (!any) {
                        s.append("\n");
                        any = true;
                    }
                    s.append(component.getFormattedText());
                }
            return s.toString();


        }
        return false;

    }

    @Override
    public Class<?> getEvaluationType() {
        return String.class;
    }
}
