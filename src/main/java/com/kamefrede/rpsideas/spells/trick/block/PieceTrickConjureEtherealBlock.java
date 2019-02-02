package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.blocks.RPSBlocks;
import com.kamefrede.rpsideas.tiles.TileEthereal;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickConjureEtherealBlock extends PieceTrick {

    private SpellParam position;
    private SpellParam time;

    public PieceTrickConjureEtherealBlock(Spell spell) {
        super(spell);
    }

    public static void placeBlock(EntityPlayer player, World world, BlockPos pos, boolean conjure) {
        if (!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos))
            return;

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.isReplaceable(world, pos) && conjure)
            world.setBlockState(pos, RPSBlocks.conjuredEthereal.getDefaultState());

    }

    public static void setColorAndTime(SpellContext context, Double timeVal, BlockPos pos, IBlockState state) {
        context.caster.world.setBlockState(pos, state);
        TileEthereal tile = (TileEthereal) context.caster.world.getTileEntity(pos);

        if (tile != null) {
            if (timeVal != null && timeVal.intValue() > 0) {
                tile.time = timeVal.intValue();
            }

            ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
            if (cad != null)
                tile.colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
        }
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        addStats(meta);
    }

    public void addStats(SpellMetadata meta) {
        meta.addStat(EnumSpellStat.POTENCY, 15);
        meta.addStat(EnumSpellStat.COST, 20);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, false);
        Double timeVal = this.getParamValue(context, time);

        if (!context.caster.world.isBlockModifiable(context.caster, pos))
            return null;

        IBlockState state = context.caster.world.getBlockState(pos);
        if (state.getBlock() != RPSBlocks.conjuredEthereal) {
            placeBlock(context.caster, context.caster.world, pos, true);

            state = context.caster.world.getBlockState(pos);

            if (state.getBlock() == RPSBlocks.conjuredEthereal)
                setColorAndTime(context, timeVal, pos, state);
        }

        return null;
    }


}
