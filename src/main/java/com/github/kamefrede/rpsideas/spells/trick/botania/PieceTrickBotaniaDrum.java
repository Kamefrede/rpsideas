package com.github.kamefrede.rpsideas.spells.trick.botania;

import com.github.kamefrede.rpsideas.util.PieceComponentTrick;
import com.github.kamefrede.rpsideas.util.botania.EnumManaTier;
import com.github.kamefrede.rpsideas.util.botania.IManaTrick;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import vazkii.botania.common.item.ItemGrassHorn;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class PieceTrickBotaniaDrum extends PieceComponentTrick implements IManaTrick {
    public PieceTrickBotaniaDrum(Spell spell) {
        super(spell);
    }

    private SpellParam positionParam;

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
    public int manaDrain(SpellContext context, int x, int y) {
        return 120;
    }

    @Override
    public Object executeIfAllowed(SpellContext context) throws SpellRuntimeException {
        Vector3 position = getParamValue(context, positionParam);
        if(position == null) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        World world = context.caster.world; //Typing mehh

        if(world.isRemote) {
            world.spawnParticle(EnumParticleTypes.NOTE, position.x + .5, position.y + 1.2, position.z + .5, 1 / 24d, 0, 0);
        } else {
            for(int i = 0; i < 10; i++) {
                world.playSound(null, position.toBlockPos(), SoundEvents.BLOCK_NOTE_BASEDRUM, SoundCategory.BLOCKS, 1f, 1f); //Bwaaaaaaaaaappppp
            }
        }

        doEffect(context, position.toBlockPos());

        return null;
    }

    abstract void doEffect(SpellContext context, BlockPos pos);

    public static class DootGrass extends PieceTrickBotaniaDrum {
        public DootGrass(Spell spell) {
            super(spell);
        }

        @Override
        void doEffect(SpellContext context, BlockPos pos) {
            ItemGrassHorn.breakGrass(context.caster.world, ItemStack.EMPTY, 0, pos);
        }
    }

    public static class DootLeaves extends PieceTrickBotaniaDrum {
        public DootLeaves(Spell spell) {
            super(spell);
        }

        @Override
        void doEffect(SpellContext context, BlockPos pos) {
            ItemGrassHorn.breakGrass(context.caster.world, ItemStack.EMPTY, 1, pos);
        }
    }

    //This is actually technically new, Psionic upgrades didn't have this one.
    //Might as well complete the set, though.
    public static class DootSnow extends PieceTrickBotaniaDrum {
        public DootSnow(Spell spell) {
            super(spell);
        }

        @Override
        void doEffect(SpellContext context, BlockPos pos) {
            ItemGrassHorn.breakGrass(context.caster.world, ItemStack.EMPTY, 2, pos);
        }
    }

    //This is the harder one.
    public static class ShearDrum extends PieceTrickBotaniaDrum {
        public ShearDrum(Spell spell) {
            super(spell);
        }

        static final int RANGE = 10;
        static final AxisAlignedBB RANGE_AABB = new AxisAlignedBB(-RANGE, -RANGE, -RANGE, RANGE, RANGE, RANGE);

        @Override
        void doEffect(SpellContext context, BlockPos pos) {
            World world = context.caster.world;
            if(world.isRemote) return;

            List<EntityLiving> nearbyLiving = world.getEntitiesWithinAABB(EntityLiving.class, RANGE_AABB.offset(pos));
            List<EntityLiving> nearbyShearables = new ArrayList<>(nearbyLiving.size());
            ItemStack cad = PsiAPI.getPlayerCAD(context.caster);

            for(EntityLiving living : nearbyLiving) {
                if(living instanceof IShearable && ((IShearable) living).isShearable(cad, world, new BlockPos(living))) {
                    nearbyShearables.add(living);
                } else if (living instanceof EntityCow) {
                    //TODO: This feels sketch, like there's some directional bias to it. Test this
                    List<EntityItem> nearbyEmptyBuckets = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(living.posX, living.posY, living.posZ, living.posX + living.width, living.posY + living.height, living.posZ + living.width), itemEnt -> {
                        if(itemEnt == null) return false; //idk
                        ItemStack stack = itemEnt.getItem();
                        return !stack.isEmpty() && stack.getItem() == Items.BUCKET;
                    });

                    for(EntityItem bucketItemEnt : nearbyEmptyBuckets) {
                        ItemStack bucketStack = bucketItemEnt.getItem();
                        while(!bucketStack.isEmpty()) {
                            EntityItem milkEnt = living.entityDropItem(new ItemStack(Items.MILK_BUCKET), 1f);

                            nudgeItem(milkEnt, world.rand);

                            bucketStack.shrink(1);
                        }

                        bucketItemEnt.setDead();
                    }
                }
            }

            //Ok now on to shearing the shearable things.
            if(nearbyShearables.isEmpty()) return;

            Collections.shuffle(nearbyShearables);

            int shearedCount = 0;
            for(EntityLiving ent : nearbyShearables) {
                if(shearedCount++ > 4) break;

                List<ItemStack> shearDrops = ((IShearable)ent).onSheared(cad, world, new BlockPos(ent), 0);
                for(ItemStack drop : shearDrops) {
                    EntityItem woolEnt = ent.entityDropItem(drop, 1f);
                    nudgeItem(woolEnt, world.rand);
                }
            }
        }

        private static void nudgeItem(EntityItem ent, Random rand) {
            ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1f;
            ent.motionY += rand.nextFloat() * 0.05f;
            ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1f;
        }

        @Override
        public int manaDrain(SpellContext context, int x, int y) {
            return 500;
        }

        @Override
        public EnumManaTier tier() {
            return EnumManaTier.ALFHEIM;
        }
    }
}
