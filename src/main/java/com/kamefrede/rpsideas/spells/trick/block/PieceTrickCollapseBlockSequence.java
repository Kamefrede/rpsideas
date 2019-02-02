package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;

public class PieceTrickCollapseBlockSequence extends PieceTrick {

    private SpellParam position;
    private SpellParam target;
    private SpellParam max;

    public PieceTrickCollapseBlockSequence(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
        addParam(max = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        double maxBlocksVal = SpellHelpers.ensurePositiveAndNonzero(this, max);

        meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 80));
        meta.addStat(EnumSpellStat.COST, (int) (maxBlocksVal * 125));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote)
            return null;

        Vector3 positionVal = SpellHelpers.getVector3(this, context, position, true, false);
        Vector3 targetVal = SpellHelpers.getVector3(this, context, target, false, false);
        double maxBlocksVal = SpellHelpers.getNumber(this, context, max, 0);
        int maxBlocksInt = (int) maxBlocksVal;
        ItemStack tool = context.tool;
        if (tool.isEmpty())
            tool = PsiAPI.getPlayerCAD(context.caster);


        int len = (int) targetVal.mag();
        Vector3 targetNorm = targetVal.copy().normalize();
        World world = context.caster.getEntityWorld();

        for (int i = 0; i < Math.min(len, maxBlocksInt); i++) {
            Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));

            SpellHelpers.isBlockPosInRadius(context, blockVec.toBlockPos());


            BlockPos pos = blockVec.toBlockPos();
            BlockPos posDown = pos.down();
            IBlockState state = world.getBlockState(pos);
            IBlockState stateDown = world.getBlockState(posDown);
            Block block = state.getBlock();
            Block blockBelow = stateDown.getBlock();

            if (!world.isBlockModifiable(context.caster, pos))
                continue;

            if (blockBelow.isAir(stateDown, world, posDown) && state.getBlockHardness(world, pos) != -1 &&
                    PieceTrickBreakBlock.canHarvestBlock(block, context.caster, world, pos, tool) &&
                    world.getTileEntity(pos) == null && block.canSilkHarvest(world, pos, state, context.caster)) {

                BlockEvent.BreakEvent event = PieceTrickBreakBlock.createBreakEvent(state, context.caster, world, pos, tool);
                MinecraftForge.EVENT_BUS.post(event);
                if (event.isCanceled())
                    continue;

                if (state.getBlock() == Blocks.LIT_REDSTONE_ORE) {
                    state = Blocks.REDSTONE_ORE.getDefaultState();
                    world.setBlockState(pos, state);
                }

                EntityFallingBlock falling = new EntityFallingBlock(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, state);
                world.spawnEntity(falling);


            }
        }

        return null;

    }
}
