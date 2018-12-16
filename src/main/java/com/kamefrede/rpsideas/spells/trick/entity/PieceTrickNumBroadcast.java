package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PieceTrickNumBroadcast extends PieceTrick {

    private SpellParam radius;
    private SpellParam channel;
    private SpellParam position;
    private SpellParam signal;

    public PieceTrickNumBroadcast(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(channel = new ParamNumber(SpellParams.GENERIC_NAME_CHANNEL, SpellParam.RED, true, true));
        addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, false, true));
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(signal = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.YELLOW, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        Double radiusVal = this.<Double>getParamEvaluation(radius);
        Double channelVal = this.<Double>getParamEvaluation(channel);

        if (channelVal != null && channelVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
        if (radiusVal == null || radiusVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
        meta.addStat(EnumSpellStat.COST, radiusVal.intValue() * 5);
        meta.addStat(EnumSpellStat.POTENCY, 5);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Double radiusVal = this.<Double>getParamValue(context, radius);
        Double channelVal = this.<Double>getParamValue(context, channel);
        Double signalVal = this.<Double>getParamValue(context, signal);

        if (context.customData.get("rpsideas:BroadcastAny") != null)
            return null;

        context.customData.put("rpsideas:BroadcastAny", true);

        if (signalVal == null)
            throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_NUMBER);

        if (channelVal == null) channelVal = 0D;
        Integer chanInt = channelVal.intValue();

        if (!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);


        List<EntityPlayer> sec = new ArrayList<>();

        final String secSignalKey = "rpsideas:BroadcastedSignal";
        final String secChannelKey = "rpsideas:BroadcastedChannel";
        final String secPlayerKey = "rpsideas:BroadcastedToWhat";

        final String channelKey = "rpsideas:" + chanInt.toString();


        AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);


        List<EntityPlayer> list = context.caster.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, axis,
                (EntityPlayer e) -> e != null && e != context.caster && e != context.focalPoint && context.isInRadius(e));
        if (list.size() > 0) {

            //actually broadcasts it!
            for (Entity ent : list) {
                EntityPlayer pl = (EntityPlayer) ent;
                if (PsiAPI.getPlayerCAD(pl) != null) {
                    sec.add(pl);
                }
            }

            writeSecurity(sec, chanInt, signalVal, context.caster, secPlayerKey, secChannelKey, secSignalKey, context.caster.world);

            for (Entity ent : list) {
                EntityPlayer pl = (EntityPlayer) ent;
                if (PsiAPI.getPlayerCAD(pl) != null && pl != null) {
                    PlayerDataHandler.PlayerData temp = SpellHelpers.getPlayerData(pl);
                    if (temp != null && temp.getCustomData() != null) {
                        temp.getCustomData().setDouble(channelKey, signalVal);
                        temp.save();
                    }
                }
            }
        }

        return null;
    }

    private void writeSecurity(List<EntityPlayer> list, Integer secChannel, Double secSignal, EntityPlayer player, String playerKey, String channelKey, String signalKey, World world) {
        PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
        if (data == null)
            return;

        if (data.getCustomData().hasKey(playerKey) && data.getCustomData().hasKey(channelKey) && data.getCustomData().hasKey(signalKey)) {
            NBTTagList list1 = (NBTTagList) data.getCustomData().getTag(playerKey);
            Integer channel = data.getCustomData().getInteger(channelKey);
            double signal = data.getCustomData().getDouble(signalKey);
            String key = "rpsideas:" + channel.toString();
            for (NBTBase cmp : list1) {
                NBTTagCompound rcmp = (NBTTagCompound) cmp;
                EntityPlayer pl = world.getPlayerEntityByUUID(Objects.requireNonNull(rcmp.getUniqueId(playerKey)));
                if (pl != null) {
                    PlayerDataHandler.PlayerData pldata = SpellHelpers.getPlayerData(pl);
                    if (pldata != null && pldata.getCustomData() != null) {
                        if (pldata.getCustomData().hasKey(key) && pldata.getCustomData().getDouble(key) == signal) {
                            pldata.getCustomData().removeTag(key);
                            pldata.save();
                        }
                    }
                }
            }
        }


        NBTTagList list1 = new NBTTagList();
        for (EntityPlayer pl : list) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setUniqueId(playerKey, pl.getUniqueID());
            list1.appendTag(nbt);
        }
        data.getCustomData().setTag(playerKey, list1);
        data.getCustomData().setInteger(channelKey, secChannel);
        data.getCustomData().setDouble(signalKey, secSignal);
        data.save();
    }


}
