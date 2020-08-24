package xyz.kamefrede.rpsideas.spells.trick.botania;

import xyz.kamefrede.rpsideas.spells.enabler.PieceComponentTrick;
import xyz.kamefrede.rpsideas.spells.enabler.botania.EnumManaTier;
import xyz.kamefrede.rpsideas.spells.enabler.botania.IManaTrick;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import vazkii.botania.common.item.ItemGrassHorn;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class PieceTrickBotaniaDrum extends PieceComponentTrick implements IManaTrick {

    private SpellParam positionParam;

    public PieceTrickBotaniaDrum(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);

        addParam(positionParam);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);

        meta.addStat(EnumSpellStat.COST, 400);
        meta.addStat(EnumSpellStat.POTENCY, 125);
    }

    @Override
    public int manaDrain(SpellContext context) {
        return 120;
    }

    @Override
    public Object executeIfAllowed(SpellContext context) throws SpellRuntimeException {
        BlockPos position = SpellHelpers.getBlockPos(this, context, positionParam, true, false);

        World world = context.caster.world;

        for (int i = 0; i < 10; i++)
            world.playSound(null, position, SoundEvents.BLOCK_NOTE_BASEDRUM, SoundCategory.BLOCKS, 1f, 1f); //Bwaaaaaaaaaappppp

        doEffect(context, position);

        return null;
    }

    public abstract void doEffect(SpellContext context, BlockPos pos);

    public static class DootGrass extends PieceTrickBotaniaDrum {
        public DootGrass(Spell spell) {
            super(spell);
        }

        @Override
        public void doEffect(SpellContext context, BlockPos pos) {
            ItemGrassHorn.breakGrass(context.caster.world, ItemStack.EMPTY, 0, pos);
        }
    }

    public static class DootLeaves extends PieceTrickBotaniaDrum {
        public DootLeaves(Spell spell) {
            super(spell);
        }

        @Override
        public void doEffect(SpellContext context, BlockPos pos) {
            ItemGrassHorn.breakGrass(context.caster.world, ItemStack.EMPTY, 1, pos);
        }
    }

    public static class DootSnow extends PieceTrickBotaniaDrum {
        public DootSnow(Spell spell) {
            super(spell);
        }

        @Override
        public void doEffect(SpellContext context, BlockPos pos) {
            ItemGrassHorn.breakGrass(context.caster.world, ItemStack.EMPTY, 2, pos);
        }
    }

    public static class ShearDrum extends PieceTrickBotaniaDrum {
        static final int RANGE = 10;
        static final AxisAlignedBB RANGE_AABB = new AxisAlignedBB(-RANGE, -RANGE, -RANGE, RANGE, RANGE, RANGE);

        public ShearDrum(Spell spell) {
            super(spell);
        }

        private static void nudgeItem(EntityItem ent, Random rand) {
            if (ent != null) {
                ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1f;
                ent.motionY += rand.nextFloat() * 0.05f;
                ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1f;
            }
        }

        @Override
        public void doEffect(SpellContext context, BlockPos pos) {
            World world = context.caster.world;

            List<EntityLiving> nearbyLiving = world.getEntitiesWithinAABB(EntityLiving.class, RANGE_AABB.offset(pos));
            List<EntityLiving> nearbyShearables = new ArrayList<>(nearbyLiving.size());
            ItemStack cad = PsiAPI.getPlayerCAD(context.caster);

            for (EntityLiving living : nearbyLiving) {
                if (living instanceof IShearable && ((IShearable) living).isShearable(cad, world, new BlockPos(living))) {
                    nearbyShearables.add(living);
                } else if (living instanceof EntityCow) {
                    List<EntityItem> nearbyEmptyBuckets = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(
                            living.posX - living.width / 2,
                            living.posY,
                            living.posZ - living.width / 2,
                            living.posX + living.width / 2,
                            living.posY + living.height,
                            living.posZ + living.width / 2), itemEnt -> {
                        if (itemEnt == null) return false;
                        ItemStack stack = itemEnt.getItem();
                        return !stack.isEmpty() && stack.getItem() == Items.BUCKET;
                    });

                    for (EntityItem bucketItemEnt : nearbyEmptyBuckets) {
                        ItemStack bucketStack = bucketItemEnt.getItem();
                        while (!bucketStack.isEmpty()) {
                            EntityItem milkEnt = living.entityDropItem(new ItemStack(Items.MILK_BUCKET), 1f);

                            nudgeItem(milkEnt, world.rand);

                            bucketStack.shrink(1);
                        }

                        bucketItemEnt.setDead();
                    }
                }
            }

            if (nearbyShearables.isEmpty()) return;

            Collections.shuffle(nearbyShearables);

            int shearedCount = 0;
            for (EntityLiving ent : nearbyShearables) {
                if (shearedCount++ > 4) break;

                List<ItemStack> shearDrops = ((IShearable) ent).onSheared(cad, world, new BlockPos(ent), 0);
                for (ItemStack drop : shearDrops) {
                    EntityItem woolEnt = ent.entityDropItem(drop, 1f);
                    nudgeItem(woolEnt, world.rand);
                }
            }
        }

        @Override
        public int manaDrain(SpellContext context) {
            return 500;
        }

        @Override
        public EnumManaTier tier() {
            return EnumManaTier.ALFHEIM;
        }
    }
}
