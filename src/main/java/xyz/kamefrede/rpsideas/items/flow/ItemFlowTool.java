package xyz.kamefrede.rpsideas.items.flow;

import xyz.kamefrede.rpsideas.items.ItemPsimetalHoe;
import xyz.kamefrede.rpsideas.util.helpers.FlowColorsHelper;
import xyz.kamefrede.rpsideas.util.helpers.IFlowColorAcceptor;
import com.teamwizardry.librarianlib.features.base.item.ItemModTool;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.IPsimetalTool;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemFlowTool extends ItemModTool implements IFlowColorAcceptor, IPsimetalTool {
    private static final ToolMaterial mat = PsiAPI.PSIMETAL_TOOL_MATERIAL;
    private final boolean ebony;


    private ItemFlowTool(String name, float attackDamage, float attackSpeed, String toolClass, boolean ebony) {
        super(name, attackDamage, attackSpeed, mat, toolClass);
        this.ebony = ebony;
    }

    private ItemFlowTool(String name, String toolClass, boolean ebony) {
        super(name, mat, toolClass);
        this.ebony = ebony;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        super.onBlockStartBreak(stack, pos, player);

        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        ItemStack playerCad = PsiAPI.getPlayerCAD(player);

        if (!playerCad.isEmpty()) {
            ItemStack bullet = getBulletInSocket(stack, getSelectedSlot(stack));
            ItemCAD.cast(player.world, player, data, bullet, playerCad, 5, 10, 0.05f, spellContext -> {
                spellContext.tool = stack;
                spellContext.positionBroken = IPsimetalTool.raytraceFromEntity(player.world, player, false, 5.0);
            });
        }

        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        ItemPsimetalHoe.regenPsi(stack, entityIn, isSelected);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        String componentName = TooltipHelper.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
        TooltipHelper.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repair) {
        if (repair.getItem() == ModItems.material) {
            return repair.getItemDamage() == (ebony ? 4 : 5);
        } else return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem ent) {
        FlowColorsHelper.clearColorizer(ent.getItem());
        return super.onEntityItemUpdate(ent);
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }


    public static class Pickaxe extends ItemFlowTool {
        public Pickaxe(String name, boolean ebony) {
            super(name, "pickaxe", ebony);
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
        public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state) {
            Material material = state.getMaterial();
            return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getDestroySpeed(stack, state) : this.efficiency;
        }
    }

    public static class Axe extends ItemFlowTool {
        public Axe(String name, boolean ebony) {
            super(name, 8f - mat.getAttackDamage(), -3.1f, "axe", ebony);
        }

        @Override
        public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state) {
            Material material = state.getMaterial();
            return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
        }
    }

    public static class Shovel extends ItemFlowTool {
        public Shovel(String name, boolean ebony) {
            super(name, "shovel", ebony);
        }

        @Override
        public boolean canHarvestBlock(IBlockState state) {
            Block block = state.getBlock();
            return block == Blocks.SNOW_LAYER || block == Blocks.SNOW;
        }

        @Nonnull
        @Override
        public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            return Items.IRON_SHOVEL.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
    }
}
