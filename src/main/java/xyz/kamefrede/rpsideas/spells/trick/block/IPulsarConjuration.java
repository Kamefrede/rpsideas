package xyz.kamefrede.rpsideas.spells.trick.block;

import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;

/**
 * @author WireSegal
 * Created at 2:11 PM on 12/16/18.
 */
public interface IPulsarConjuration {
    default void conjurePulsar(SpellContext context, SpellParam positionParam, SpellParam timeParam) throws SpellRuntimeException {

        SpellPiece piece = (SpellPiece) this;

        BlockPos pos = SpellHelpers.getBlockPos(piece, context, positionParam, true, false);

        int time = (int) SpellHelpers.getNumber(piece, context, timeParam, 0);
        World world = context.caster.world;

        if (SpellHelpers.placeBlock(world, pos, getStateToSet(), false))
            postSet(context, world, pos, time);
    }

    void postSet(SpellContext context, World world, BlockPos pos, int time);

    IBlockState getStateToSet();
}
