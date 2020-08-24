package xyz.kamefrede.rpsideas.spells.trick.misc;

import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSlotMatch extends PieceTrick {

    private SpellParam position;

    public PieceTrickSlotMatch(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        BlockPos pos = SpellHelpers.checkPos(this, context, position, true, true, false).toBlockPos();
        IBlockState state = context.caster.world.getBlockState(pos);
        Block block = state.getBlock();
        EntityPlayer player = context.caster;
        int slot = -1;
        for (ItemStack s : player.inventory.mainInventory) {
            if (s.getItem() == Item.getItemFromBlock(block)) {
                slot = getSlotFor(s, player.inventory.mainInventory);
            }

        }
        if (slot == -1) {
            return null;
        }
        context.customTargetSlot = true;
        context.targetSlot = slot;

        return null;
    }

    private int getSlotFor(ItemStack stack, NonNullList<ItemStack> mainInventory) {
        for (int i = 0; i < mainInventory.size(); ++i) {
            if (!mainInventory.get(i).isEmpty() && this.stackEqualExact(stack, mainInventory.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
}
