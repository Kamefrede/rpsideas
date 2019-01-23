package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickOpenElytra extends PieceTrick {

    private SpellParam num;

    public PieceTrickOpenElytra(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double value = SpellHelpers.getNumber(this, context, num, 1);
        if (Math.abs(value) < 1.0) {
            Iterable<ItemStack> it = context.caster.getArmorInventoryList();
            for (ItemStack stack : it) {
                if (stack.getItem() instanceof ItemElytra) {
                    context.caster.setFlag(7, true);

                    return null;
                }
            }
        }

        return null;
    }
}
