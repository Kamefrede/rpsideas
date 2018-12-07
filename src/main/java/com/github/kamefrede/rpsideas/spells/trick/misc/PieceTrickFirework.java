package com.github.kamefrede.rpsideas.spells.trick.misc;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickFirework extends PieceTrick {

    SpellParam position;
    SpellParam time;

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
        Double timeVal = this.<Double>getParamEvaluation(time);
        if(timeVal == null || timeVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

        meta.addStat(EnumSpellStat.COST, timeVal.intValue() * 10);
        meta.addStat(EnumSpellStat.POTENCY, timeVal.intValue() * 2);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.world.isRemote) return null;
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Double timeVal = this.<Double>getParamValue(context, time);
        int timeR = timeVal.intValue();

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        int color = ((ICAD)cad.getItem()).getSpellColor(cad) | 0xf000000;

        if(positionVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if(!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        ItemStack fireworkStack = generateFirework(color, timeR);

        EntityFireworkRocket rocket = new EntityFireworkRocket(context.caster.world, positionVal.x, positionVal.y, positionVal.z, fireworkStack);
        context.caster.world.spawnEntity(rocket);

        return true;
    }


    //thanks u botania
    public ItemStack generateFirework(int color, int time) {
        ItemStack stack = new ItemStack(Items.FIREWORKS);
        NBTTagCompound explosion = new NBTTagCompound();
        explosion.setIntArray("Colors", new int[] { color });

        int type = 1;
        double rand = Math.random();
        if(rand > 0.25) {
            if(rand > 0.9)
                type = 2;
            else type = 0;
        }

        explosion.setInteger("Type", type);

        if(Math.random() < 0.05)
            if(Math.random() < 0.5)
                explosion.setBoolean("Flicker", true);
            else explosion.setBoolean("Trail", true);

        ItemNBTHelper.setCompound(stack, "Explosion", explosion);

        NBTTagCompound fireworks = new NBTTagCompound();
        fireworks.setInteger("Flight", time);

        NBTTagList explosions = new NBTTagList();
        explosions.appendTag(explosion);
        fireworks.setTag("Explosions", explosions);

        ItemNBTHelper.setCompound(stack, "Fireworks", fireworks);

        return stack;
    }
}
