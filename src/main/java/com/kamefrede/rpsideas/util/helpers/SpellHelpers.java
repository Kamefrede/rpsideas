package com.kamefrede.rpsideas.util.helpers;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;
import vazkii.psi.common.spell.trick.block.PieceTrickPlaceBlock;

import java.util.concurrent.Executors;

public class SpellHelpers {
    public static void scheduleTask(WorldServer server, Runnable task) {
        MinecraftServer dedicated = server.getMinecraftServer();
        if (dedicated != null)
            dedicated.futureTaskQueue.add(ListenableFutureTask.create(Executors.callable(task)));
    }

    public static void addAllParams(SpellPiece piece, SpellParam... params) {
        for (SpellParam param : params)
            piece.addParam(param);
    }

    public static double evaluateNumber(SpellPiece piece, SpellParam param, double def) throws SpellCompilationException {
        Double value = piece.getParamEvaluation(param);
        if (value == null) return def;
        else return value;
    }


    public static double ensurePositiveAndNonzero(SpellPiece piece, SpellParam param, double def) throws SpellCompilationException {
        double val = evaluateNumber(piece, param, def);

        if (val <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);

        return val;
    }

    public static double ensurePositiveAndNonzero(SpellPiece piece, SpellParam param) throws SpellCompilationException {
        return evaluateNumber(piece, param, 0);
    }

    public static double ensurePositiveOrZero(SpellPiece piece, SpellParam param) throws SpellCompilationException {
        double val = evaluateNumber(piece, param, 0);
        if (val < 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);
        return val;
    }

    public static BlockPos getBlockPos(SpellPiece piece, SpellContext context, SpellParam param) throws SpellRuntimeException {
        Vector3 position = piece.getParamValue(context, param);
        if (position == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (!context.isInRadius(position))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        return position.toBlockPos();
    }

    public static Vector3 getVector3(SpellPiece piece, SpellContext context, SpellParam param) throws SpellRuntimeException {
        return checkPos(piece, context, param, false, false);
    }

    public static Vector3 getVector3(SpellPiece piece, SpellContext context, SpellParam param, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
        return checkPos(piece, context, param, check, shouldBeAxial);
    }

    public static Vector3 getVector3(SpellPiece piece, SpellContext context, SpellParam param, boolean nonnull, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
        return checkPos(piece, context, param, nonnull, check, shouldBeAxial);
    }

    public static BlockPos getBlockPos(SpellPiece piece, SpellContext context, SpellParam param, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
        return checkPos(piece, context, param, check, shouldBeAxial).toBlockPos();
    }

    public static BlockPos getBlockPos(SpellPiece piece, SpellContext context, SpellParam param, boolean nonnull, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
        return checkPos(piece, context, param, nonnull, check, shouldBeAxial).toBlockPos();
    }

    public static Vector3 checkPos(SpellPiece piece, SpellContext context, SpellParam param, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
        return checkPos(piece, context, param, true, check, shouldBeAxial);
    }

    public static Vector3 checkPos(SpellPiece piece, SpellContext context, SpellParam param, boolean nonnull, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
        Vector3 position = piece.getParamValue(context, param);
        if (nonnull && (position == null || position.isZero()))
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (check && !context.isInRadius(position))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        if (shouldBeAxial && !position.isAxial())
            throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);
        return position;
    }

    public static Vector3 getDefaultedVector(SpellPiece piece, SpellContext context, SpellParam param, boolean check, boolean shouldBeAxial, Vector3 def) throws SpellRuntimeException {
        Vector3 position = piece.getParamValue(context, param);
        if (position == null || position.isZero()) {
            if (def == null || def.isZero())
                throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
            return def;
        } else return checkPos(piece, context, param, false, check, shouldBeAxial);

    }

    public static EnumFacing getFacing(SpellPiece piece, SpellContext context, SpellParam param) throws SpellRuntimeException {
        Vector3 face = getVector3(piece, context, param, false, true);
        return EnumFacing.getFacingFromVector((float) face.x, (float) face.y, (float) face.z);
    }

    public static EnumFacing getFacing(SpellPiece piece, SpellContext context, SpellParam param, EnumFacing def) throws SpellRuntimeException {
        Vector3 face = getVector3(piece, context, param, true, false, true);
        if (face == null)
            return def;
        return EnumFacing.getFacingFromVector((float) face.x, (float) face.y, (float) face.z);
    }

    public static boolean isBlockPosInRadius(SpellContext context, BlockPos pos) {
        return context.isInRadius(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
    }

    public static double getNumber(SpellPiece piece, SpellContext context, SpellParam param, double def) {
        Double value = piece.getParamValue(context, param);
        if (value == null) return def;
        else return value;
    }

    public static double getBoundedNumber(SpellPiece piece, SpellContext context, SpellParam param, double def) {
        double val = getNumber(piece, context, param, def);
        if (val > def)
            return def;
        else
            return val;
    }

    public static EntityListWrapper ensureNonnullOrEmptyList(SpellPiece piece, SpellContext context, SpellParam param) throws SpellRuntimeException {
        EntityListWrapper list = piece.getParamValue(context, param);
        if (list == null || list.unwrap().isEmpty())
            throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_LIST);
        return list;
    }

    public static EntityListWrapper ensureNonnullList(SpellPiece piece, SpellContext context, SpellParam param) throws SpellRuntimeException {
        EntityListWrapper list = piece.getParamValue(context, param);
        if (list == null)
            throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_LIST);
        return list;
    }

    public static Entity ensureNonnullEntity(SpellPiece piece, SpellContext context, SpellParam param) throws SpellRuntimeException {
        Entity ent = piece.getParamValue(context, param);
        if (ent == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        return ent;
    }

    public static EntityLivingBase ensureNonnullAndLivingEntity(SpellPiece piece, SpellContext context, SpellParam param) throws SpellRuntimeException {
        Entity ent = ensureNonnullEntity(piece, context, param);
        if (!(ent instanceof EntityLivingBase))
            throw new SpellRuntimeException(SpellRuntimeExceptions.ENTITY_NOT_LIVING);
        return (EntityLivingBase) ent;
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

    public static int getColor(ItemStack stack) {
        int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
        if (!stack.isEmpty() && stack.getItem() instanceof ICADColorizer)
            colorVal = Psi.proxy.getColorForColorizer(stack);
        return colorVal;
    }

    public static int getCADColor(ItemStack stack) {
        int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
        if (!stack.isEmpty() && stack.getItem() instanceof ICAD)
            colorVal = Psi.proxy.getColorForCAD(stack);
        return colorVal;
    }

    public static float getR(int color) {
        return PsiRenderHelper.r(color) / 255f;
    }

    public static float getG(int color) {
        return PsiRenderHelper.g(color) / 255f;
    }

    public static float getB(int color) {
        return PsiRenderHelper.b(color) / 255f;
    }

    public static boolean hasComponent(ItemStack cad, EnumCADComponent component, Item check) {
        ICAD icad = (ICAD) cad.getItem();
        ItemStack comp = icad.getComponentInSlot(cad, component);
        return !comp.isEmpty() && comp.getItem().getClass().isInstance(check);
    }

}
