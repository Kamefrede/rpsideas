package com.kamefrede.rpsideas.spells.trick.misc;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSlotMatch extends PieceTrick {

    private SpellParam position;

    public PieceTrickSlotMatch(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;
        Vector3 posVec = this.getParamValue(context, position);

        if (posVec == null || posVec.isZero())
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

        World world = context.caster.world;
        BlockPos pos = new BlockPos(posVec.x, posVec.y, posVec.z);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.isAir(state, world, pos)) return null;
        NonNullList<ItemStack> allInvs = context.caster.inventory.mainInventory;
        allInvs.addAll(context.caster.inventory.offHandInventory);
        for (ItemStack stack : allInvs) {
            if (Item.getItemFromBlock(block) == stack.getItem() && block.damageDropped(state) == stack.getItemDamage()) {
                context.targetSlot = context.caster.inventory.getSlotFor(stack);
                return true;
            }
        }
        return false;
    }
}
