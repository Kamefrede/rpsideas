package com.kamefrede.rpsideas.util.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;
import vazkii.psi.common.spell.trick.block.PieceTrickPlaceBlock;

import java.util.ConcurrentModificationException;

public class SpellHelpers {
    public static void addAllParams(SpellPiece piece, SpellParam... params) {
        for (SpellParam param : params)
            piece.addParam(param);
    }

    public static double ensurePositiveAndNonzero(SpellPiece piece, SpellParam param) throws SpellCompilationException {
        Double val = piece.getParamEvaluation(param);

        if (val == null || val <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);

        return val;
    }

    public static double ensurePositiveOrZero(SpellPiece piece, SpellParam param) throws SpellCompilationException {
        Double val = piece.getParamEvaluation(param);

        if (val == null || val <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);

        return val;
    }

    public static BlockPos getBlockPosFromVectorParam(SpellPiece piece, SpellContext context, SpellParam param) {
        return piece.<Vector3>getParamValue(context, param).toBlockPos();
    }

    public static void checkPos(SpellContext context, BlockPos pos) throws SpellRuntimeException {
        if (pos == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (!isBlockPosInRadius(context, pos))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
    }

    public static boolean isBlockPosInRadius(SpellContext context, BlockPos pos) {
        return context.isInRadius(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
    }

    public static double getNumber(SpellPiece piece, SpellContext context, SpellParam param, double def) {
        Double value = piece.getParamValue(context, param);
        if (value == null) return def;
        else return value;
    }

    public static void placeBlockFromInventory(SpellContext context, BlockPos pos, boolean particles) {
        PieceTrickPlaceBlock.placeBlock(context.caster, context.caster.world, pos, context.targetSlot, particles);
    }

    public static boolean placeBlock(World world, BlockPos pos, IBlockState setState, boolean particles) {
        if (world.isRemote || !world.isBlockLoaded(pos)) return false;

        IBlockState currentState = world.getBlockState(pos);
        if (currentState != setState && (currentState.getBlock().isReplaceable(world, pos) || currentState.getBlock().isAir(currentState, world, pos))) {
            boolean couldPlace = world.setBlockState(pos, setState);
            if (couldPlace && particles)
                world.playEvent(2001, pos, Block.getStateId(setState));
            return couldPlace;
        }
        return false;
    }

    public static void breakBlock(SpellContext context, BlockPos pos, boolean particles) {
        PieceTrickBreakBlock.removeBlockWithDrops(context, context.caster, context.caster.world, context.tool, pos, particles);
    }

    public static void emitSoundFromEntity(World world, EntityLivingBase entity, SoundEvent sound) {
        emitSoundFromEntity(world, entity, sound, 1f, 1f);
    }

    public static void emitSoundFromEntity(World world, EntityLivingBase entity, SoundEvent sound, float volume, float pitch) {
        emitSoundFromEntity(world, entity, sound, SoundCategory.PLAYERS, volume, pitch);
    }

    public static void emitSoundFromEntity(World world, EntityLivingBase entity, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        world.playSound(null, entity.posX, entity.posY, entity.posZ, sound, category, volume, pitch);
    }

    public static double distanceSquared(double x1, double y1, double z1, double x2, double y2, double z2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) + (z1 - z2);
    }

    public static double distanceSquared(Entity a, Entity b) {
        return distanceSquared(a.posX, a.posY, a.posZ, b.posX, b.posY, b.posZ);
    }

    public static PlayerDataHandler.PlayerData getPlayerData(EntityPlayer player) {
        try {
            return PlayerDataHandler.get(player);
        } catch (ConcurrentModificationException ignored) {
            // Sometimes the PlayerDataHandler will CME for no known reason.
            return null;
        }
    }
}
