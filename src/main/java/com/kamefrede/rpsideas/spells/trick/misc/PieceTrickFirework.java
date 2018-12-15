package com.kamefrede.rpsideas.spells.trick.misc;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.client.core.handler.ClientTickHandler;

import java.awt.*;
import java.util.Random;

import static vazkii.psi.common.item.component.ItemCADColorizer.colorTable;

public class PieceTrickFirework extends PieceTrick {// TODO: 12/15/18 look at

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
        if (timeVal == null || timeVal < 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

        meta.addStat(EnumSpellStat.COST, timeVal.intValue() * 10);
        meta.addStat(EnumSpellStat.POTENCY, timeVal.intValue() * 2);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Double timeVal = this.<Double>getParamValue(context, time);
        int timeR = timeVal.intValue();

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ICAD icad = (ICAD) cad.getItem();
        ItemStack colorizer = icad.getComponentInSlot(cad, EnumCADComponent.DYE);
        int color;
        if (!colorizer.isEmpty()) {
            color = getColor(colorizer);
        } else {
            color = 0xFFFFFF;
        }

        if (positionVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        ItemStack fireworkStack = generateFirework(timeR, color);

        EntityFireworkRocket rocket = new EntityFireworkRocket(context.caster.world, positionVal.x, positionVal.y, positionVal.z, fireworkStack);
        context.caster.world.spawnEntity(rocket);

        return true;
    }

    public int getColor(ItemStack stack) {
        if (stack.getItemDamage() < 16)
            return colorTable[15 - stack.getItemDamage()];

        switch (stack.getItemDamage()) {
            case 16: {
                float time = ClientTickHandler.total;
                return Color.HSBtoRGB(time * 0.005F, 1F, 1F);
            }
            case 17:
                float time = ClientTickHandler.total;
                float w = (float) (Math.sin(time * 0.4) * 0.5 + 0.5) * 0.1F;
                float r = (float) (Math.sin(time * 0.1) * 0.5 + 0.5) * 0.5F + 0.25F + w;
                float g = 0.5F + w;
                float b = 1F;

                return new Color((int) (r * 255), (int) (g * 255), (int) (b * 255)).getRGB();
        }

        return 0xFFFFFF;
    }


    //thanks u botania
    public ItemStack generateFirework(int time, int color) {
        ItemStack stack = new ItemStack(Items.FIREWORKS);
        NBTTagCompound explosion = new NBTTagCompound();
        explosion.setIntArray("Colors", new int[]{color});
        int type = 1;
        double rand = Math.random();
        if (rand > 0.25) {
            if (rand > 0.9)
                type = 2;
            else type = 0;
        }

        explosion.setInteger("Type", type);

        if (Math.random() < 0.05)
            if (Math.random() < 0.5)
                explosion.setBoolean("Flicker", true);
            else explosion.setBoolean("Trail", true);

        ItemNBTHelper.setCompound(stack, "Explosion", explosion);
        Random rdm = new Random();

        NBTTagCompound fireworks = new NBTTagCompound();

        fireworks.setInteger("Flight", (Integer) Math.max(-2, ((time - 10 - rdm.nextInt(6) - rdm.nextInt(7)) / 10)));
        NBTTagList explosions = new NBTTagList();
        explosions.appendTag(explosion);
        fireworks.setTag("Explosions", explosions);

        ItemNBTHelper.setCompound(stack, "Fireworks", fireworks);
        ItemNBTHelper.setInt(stack, "LifeTime", time);

        return stack;
    }
}
