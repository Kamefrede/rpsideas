package com.kamefrede.rpsideas.spells.operator.block;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
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

        BlockPos pos = SpellHelpers.getBlockPos(this, context, position);
        TileEntity sign = context.caster.world.getTileEntity(pos);
        if (sign instanceof TileEntitySign) {
            StringBuilder s = new StringBuilder();
            for (ITextComponent component : ((TileEntitySign) sign).signText)
                if (!component.getUnformattedText().isEmpty())
                    s.append(component.getFormattedText());
            return s.toString();


        }
        return "";

    }

    @Override
    public Class<?> getEvaluationType() {
        return String.class;
    }
}
