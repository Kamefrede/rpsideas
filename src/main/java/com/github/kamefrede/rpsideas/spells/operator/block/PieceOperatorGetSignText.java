package com.github.kamefrede.rpsideas.spells.operator.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.block.state.IBlockState;
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

    SpellParam position;

    public PieceOperatorGetSignText(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false ));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.world.isRemote) return null;
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        if(position == null ) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if(!context.isInRadius(positionVal)) throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
        IBlockState state = context.caster.world.getBlockState(pos);
        Block block = state.getBlock();
        if(block instanceof BlockSign && context.caster.world.getTileEntity(pos) instanceof TileEntitySign){
            System.out.println("here");
            TileEntitySign sign = (TileEntitySign)context.caster.world.getTileEntity(pos);
            StringBuilder s = new StringBuilder();
            for (int ii=0; ii<4; ii++){
                assert sign != null;
                if(sign.signText[ii].getFormattedText().isEmpty() ){
                } else if(!(sign.signText[ii].getFormattedText().isEmpty()) && ii == 3 ) {
                    s.append("\n").append(sign.signText[ii].getFormattedText());
                } else{
                    s.append("\n").append(sign.signText[ii].getFormattedText());
                }
            }
            return s;



        }
        return false;

    }

    @Override
    public Class<?> getEvaluationType() {
        return String.class;
    }
}
