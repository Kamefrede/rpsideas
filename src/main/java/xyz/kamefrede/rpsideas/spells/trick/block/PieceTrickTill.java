package xyz.kamefrede.rpsideas.spells.trick.block;

import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickTill extends PieceTrick {

    private SpellParam position;

    public PieceTrickTill(Spell spell) {
        super(spell);
    }

    public static EnumActionResult tillBlock(EntityPlayer player, World world, BlockPos pos) {
        if (!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos)) {
            return EnumActionResult.PASS;
        }
        return Items.IRON_HOE.onItemUse(player, world, pos, EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.COST, 10);
        meta.addStat(EnumSpellStat.POTENCY, 10);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {

        BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, false);

        return tillBlock(context.caster, context.caster.world, pos);

    }


}

