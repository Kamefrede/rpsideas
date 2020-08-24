package xyz.kamefrede.rpsideas.spells.operator.vector;

import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.item.ItemVectorRuler;

public class PieceOperatorVectorRulerOrigin extends PieceOperator {


    public PieceOperatorVectorRulerOrigin(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return getRulerVector(context.caster);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }

    public static Vector3 getRulerVector(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemVectorRuler)
                return new Vector3(NBTHelper.getInt(stack, "srcX", 0), NBTHelper.getInt(stack, "srcY", 0), NBTHelper.getInt(stack, "srcZ", 0));
        }

        return Vector3.zero;
    }

}
