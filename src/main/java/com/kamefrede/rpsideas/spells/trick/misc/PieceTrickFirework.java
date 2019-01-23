package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.entity.EntityPsireworkRocket;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.Random;

public class PieceTrickFirework extends PieceTrick {

    private SpellParam position;
    private SpellParam time;

    public PieceTrickFirework(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        double timeVal = SpellHelpers.ensurePositiveOrZero(this, time);

        meta.addStat(EnumSpellStat.COST, (int) (timeVal * 10));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;
        Vector3 positionVal = this.getParamValue(context, position);
        double timeVal = SpellHelpers.getNumber(this, context, time, 0);

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ICAD icad = (ICAD) cad.getItem();
        ItemStack colorizer = icad.getComponentInSlot(cad, EnumCADComponent.DYE);

        if (positionVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        ItemStack fireworkStack = generateFirework((int) timeVal);

        EntityFireworkRocket rocket = new EntityPsireworkRocket(context.caster.world, positionVal.x, positionVal.y, positionVal.z, fireworkStack, colorizer);
        context.caster.world.spawnEntity(rocket);

        return null;
    }

    public ItemStack generateFirework(int time) {
        ItemStack stack = new ItemStack(Items.FIREWORKS);
        Random rdm = new Random();

        NBTTagCompound fireworks = new NBTTagCompound();

        fireworks.setInteger("Flight", Math.max(-2, ((time - 10 - rdm.nextInt(6) - rdm.nextInt(7)) / 10)));
        fireworks.setTag("Explosions", new NBTTagList());

        ItemNBTHelper.setCompound(stack, "Fireworks", fireworks);
        ItemNBTHelper.setInt(stack, "LifeTime", time);

        return stack;
    }
}
