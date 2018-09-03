package com.github.kamefrede.psiaddon.spells.trick;

import com.github.kamefrede.psiaddon.blocks.ConjuredEtherealBlock;
import com.github.kamefrede.psiaddon.tiles.TileEthereal;
import com.github.kamefrede.psiaddon.util.LibBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import static com.github.kamefrede.psiaddon.spells.trick.PieceTrickConjureEtherealBlock.placeBlock;

public class PieceTrickConjureEtherealBlockSequence extends PieceTrick {

    SpellParam position;
    SpellParam target;
    SpellParam maxBlocks;
    SpellParam time;

    public PieceTrickConjureEtherealBlockSequence(Spell spell){
        super(spell);
    }

    @Override
    public void initParams(){
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
        addParam(maxBlocks = new ParamVector(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException{
        super.addToMetadata(meta);

        Double maxBlocksVal = this.<Double>getParamEvaluation(maxBlocks);
        if(maxBlocksVal == null || maxBlocksVal < 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

        meta.addStat(EnumSpellStat.COST, (int) (maxBlocksVal * 20));
        meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 15));

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Vector3 targetVal = this.<Vector3>getParamValue(context, target);
        Double maxBlocksVal = this.<Double>getParamValue(context, maxBlocks);
        Double timeVal = this.<Double>getParamValue(context, time);
        int maxBlocksInt = maxBlocksVal.intValue();

        if(positionVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        int len = (int) targetVal.mag();
        Vector3 targetNorm = targetVal.copy().normalize();
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);

        for(int i = 0; i < Math.min(len, maxBlocksInt); i++) {
            Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));

            if(!context.isInRadius(blockVec))
                throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

            BlockPos pos = new BlockPos(blockVec.x, blockVec.y, blockVec.z);
            if(!context.caster.getEntityWorld().isBlockModifiable(context.caster, pos))
                continue;

            IBlockState state = context.caster.getEntityWorld().getBlockState(pos);

            if(state.getBlock() != LibBlocks.conjuredEtherealBlock) {
                placeBlock(context.caster, context.caster.getEntityWorld(), pos, context.getTargetSlot(), false, true);
                state = context.caster.getEntityWorld().getBlockState(pos);

                if(!context.caster.getEntityWorld().isRemote && state.getBlock() == LibBlocks.conjuredEtherealBlock) {
                    context.caster.getEntityWorld().setBlockState(pos, messWithState(state));
                    TileEthereal tile = (TileEthereal) context.caster.getEntityWorld().getTileEntity(pos);

                    if(timeVal != null && timeVal.intValue() > 0) {
                        int val = timeVal.intValue();
                        tile.time = val;
                    }

                    if(cad != null)
                        tile.colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
                }
            }
        }

        return null;
    }

    public IBlockState messWithState(IBlockState state) {
        return state.withProperty(ConjuredEtherealBlock.SOLID, true);
    }
}
