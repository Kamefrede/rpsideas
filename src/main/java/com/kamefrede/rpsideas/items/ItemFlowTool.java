package com.kamefrede.rpsideas.items;

import com.google.common.collect.Sets;
import com.kamefrede.rpsideas.items.base.IPsiAddonTool;
import com.kamefrede.rpsideas.util.helpers.FlowColorsHelper;
import com.kamefrede.rpsideas.util.helpers.IFlowColorAcceptor;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.ItemPsimetalTool;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ItemFlowTool extends ItemTool implements IPsiAddonTool, IFlowColorAcceptor { // TODO: 12/15/18 look at
    static final ToolMaterial mat = PsiAPI.PSIMETAL_TOOL_MATERIAL;
    final boolean ebony;

    //Use constructors down below
    private ItemFlowTool(String toolClass, boolean ebony) {
        super(getAttackDamage(toolClass, mat), getAttackSpeed(toolClass, mat), mat, getEffectiveList(toolClass));
        this.ebony = ebony;
    }

    //Thanks mojang
    private static Set<Block> getEffectiveList(String toolClass) {
        switch (toolClass) {
            case "pickaxe":
                return Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE);
            case "shovel":
                return Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER);
            case "axe":
                return Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
            default:
                return Collections.emptySet();
        }
    }

    //Stoleded from liblib
    private static float getAttackDamage(String toolClass, ToolMaterial mat) {
        switch (toolClass) {
            case "axe":
                return mat.getHarvestLevel() == 0 ? 6f : 8f;
            case "shovel":
                return 1.5f;
            case "pickaxe":
                return 1f;
            default:
                return 0f;
        }
    }

    private static float getAttackSpeed(String toolClass, ToolMaterial mat) {
        switch (toolClass) {
            case "axe": {
                if (mat.getEfficiency() <= 5f) return -3.2f;
                else if (mat.getEfficiency() <= 7f) return -3.1f;
                else return -3f;
            }
            case "shovel":
                return -3f;
            case "pickaxe":
                return -2.8f;
            default:
                return 0f;
        }
    }

    //TODO liblib item glow stuff

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        super.onBlockStartBreak(stack, pos, player);

        PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
        ItemStack playerCad = PsiAPI.getPlayerCAD(player);

        if (!playerCad.isEmpty()) {
            ItemStack bullet = getBulletInSocket(stack, getSelectedSlot(stack));
            ItemCAD.cast(player.world, player, data, bullet, playerCad, 5, 10, 0.05f, spellContext -> {
                spellContext.tool = stack;
                spellContext.positionBroken = ItemPsimetalTool.raytraceFromEntity(player.world, player, false, 5.0);
            });
        }

        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
        ItemMod.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (repair.getItem() == ModItems.material) {
            return repair.getItemDamage() == (ebony ? 4 : 5);
        } else return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem ent) {
        FlowColorsHelper.clearColorizer(ent.getItem());
        return super.onEntityItemUpdate(ent);
    }

    //This is the part where I copypasta vanilla tools to accommodate all of vanilla's stupid special casing
    public static class Pickaxe extends ItemFlowTool {
        public Pickaxe(boolean ebony) {
            super("pickaxe", ebony);
        }

        @Override
        public boolean canHarvestBlock(IBlockState state) {
            Block block = state.getBlock();
            if (block == Blocks.OBSIDIAN) {
                return this.toolMaterial.getHarvestLevel() == 3;
            } else if (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE) {
                if (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK) {
                    if (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE) {
                        if (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE) {
                            if (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE) {
                                if (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE) {
                                    Material material = state.getMaterial();
                                    return material == Material.ROCK || material == Material.IRON || material == Material.ANVIL;
                                } else {
                                    return this.toolMaterial.getHarvestLevel() >= 2;
                                }
                            } else {
                                return this.toolMaterial.getHarvestLevel() >= 1;
                            }
                        } else {
                            return this.toolMaterial.getHarvestLevel() >= 1;
                        }
                    } else {
                        return this.toolMaterial.getHarvestLevel() >= 2;
                    }
                } else {
                    return this.toolMaterial.getHarvestLevel() >= 2;
                }
            } else {
                return this.toolMaterial.getHarvestLevel() >= 2;
            }
        }

        @Override
        public float getDestroySpeed(ItemStack stack, IBlockState state) {
            Material material = state.getMaterial();
            return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getDestroySpeed(stack, state) : this.efficiency;
        }
    }

    public static class Axe extends ItemFlowTool {
        public Axe(boolean ebony) {
            super("axe", ebony);
        }

        @Override
        public float getDestroySpeed(ItemStack stack, IBlockState state) {
            Material material = state.getMaterial();
            return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
        }
    }

    public static class Shovel extends ItemFlowTool {
        public Shovel(boolean ebony) {
            super("shovel", ebony);
        }

        @Override
        public boolean canHarvestBlock(IBlockState state) {
            Block block = state.getBlock();
            return block == Blocks.SNOW_LAYER || block == Blocks.SNOW;
        }

        //so you're able to create paths and such with it
        @Override
        public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            return Items.IRON_SHOVEL.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
    }
}
