package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSmeltBlockSequence extends PieceTrick {

    private SpellParam position;
    private SpellParam direction;
    private SpellParam maxBlocks;

    public PieceTrickSmeltBlockSequence(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(direction = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
        addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        double maxBlocksVal = SpellHelpers.ensurePositiveAndNonzero(this, maxBlocks);

        meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 20));
        meta.addStat(EnumSpellStat.COST, (int) (maxBlocksVal * 80));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote)
            return null;

        Vector3 positionVal = this.getParamValue(context, position);
        Vector3 targetVal = this.getParamValue(context, direction);
        double maxBlocksVal = this.getParamValue(context, maxBlocks);

        if (positionVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);


        int len = (int) targetVal.mag();
        Vector3 targetNorm = targetVal.copy().normalize();
        for (int i = 0; i < Math.min(len, maxBlocksVal); i++) {
            Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));

            if (!context.isInRadius(blockVec))
                throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

            BlockPos pos = new BlockPos(blockVec.x, blockVec.y, blockVec.z);
            if (!context.caster.world.isBlockModifiable(context.caster, pos))
                continue;

            IBlockState state = context.caster.world.getBlockState(pos);
            Block block = state.getBlock();
            int meta = block.getMetaFromState(state);
            ItemStack stack = new ItemStack(block, 1, meta);
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
            if (!result.isEmpty()) {
                Item item = result.getItem();
                Block block1 = Block.getBlockFromItem(item);
                if (block1 != Blocks.AIR) {
                    context.caster.world.setBlockState(pos, block1.getStateFromMeta(result.getMetadata()));
                    state = context.caster.world.getBlockState(pos);
                    context.caster.world.playEvent(2001, pos, Block.getStateId(state));
                }
            }

        }
        return null;

    }
}
