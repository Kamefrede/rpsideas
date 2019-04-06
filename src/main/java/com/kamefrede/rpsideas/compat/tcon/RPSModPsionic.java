package com.kamefrede.rpsideas.compat.tcon;

import com.kamefrede.rpsideas.items.RPSItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.tinkering.Category;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;

import static com.kamefrede.rpsideas.items.components.ItemIntegratedBattlecaster.hasBattlecaster;
import static vazkii.psi.common.item.tool.IPsimetalTool.raytraceFromEntity;

public class RPSModPsionic extends ModifierTrait {

    protected static final int maxLevel = 3;

    public RPSModPsionic() {
        super("socketable", 0x5752CC, maxLevel, 0);
        addRecipeMatch(new RecipeMatch.ItemCombination(1, new ItemStack(RPSItems.wideBandSocket)));
        addAspects(new ModifierAspect.CategoryAnyAspect(Category.HARVEST, Category.TOOL, Category.WEAPON, Category.AOE));
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);
    }

    @Override
    public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
        return super.getTooltip(modifierTag, detailed);
    }

    @Override
    public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        super.afterBlockBreak(tool, world, state, pos, player, wasEffective);
        if (!hasBattlecaster(tool)) {
            EntityPlayer pl = (EntityPlayer) player;
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(pl);
            ItemStack playerCad = PsiAPI.getPlayerCAD(pl);

            if (!playerCad.isEmpty()) {
                ItemStack bullet = ISocketableCapability.socketable(tool).getBulletInSocket(ISocketableCapability.socketable(tool).getSelectedSlot());
                ItemCAD.cast(player.getEntityWorld(), pl, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                    context.tool = tool;
                    context.positionBroken = raytraceFromEntity(player.getEntityWorld(), player, false, player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue());
                });
            }
        }
    }


}
