package com.kamefrede.rpsideas.items;

import com.google.common.collect.ImmutableSet;
import com.kamefrede.rpsideas.items.base.IPsiAddonTool;
import com.kamefrede.rpsideas.util.helpers.FlowColorsHelper;
import com.kamefrede.rpsideas.util.helpers.IFlowColorAcceptor;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemInlineCaster extends ItemMod implements IPsiAddonTool, IFlowColorAcceptor {
    public ItemInlineCaster() {
        super(RPSItemNames.INLINE_CASTER);
        setMaxStackSize(1);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        ItemStack cad = PsiAPI.getPlayerCAD(player);

        if (!cad.isEmpty()) {
            ItemStack bullet = getBulletInSocket(held, getSelectedSlot(held));
            if (bullet.isEmpty()) {
                if (ItemCAD.craft(player, new ItemStack(Items.REDSTONE), new ItemStack(ModItems.material, 1, 0))) {
                    if (!world.isRemote) {
                        SpellHelpers.emitSoundFromEntity(world, player, PsiSoundHandler.cadShoot, .5f, (float) (.5f + Math.random() * .5));
                    }

                    data.deductPsi(100, 60, true);
                    if (data.level == 0) data.levelUp();

                    return new ActionResult<>(EnumActionResult.SUCCESS, held);
                }
            } else {
                //Cast a spell with the bullet.
                ItemCAD.cast(world, player, data, bullet, cad, 40, 25, 0.5f, ctx -> ctx.castFrom = hand);
                return new ActionResult<>(EnumActionResult.SUCCESS, held);
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean isSocketSlotAvailable(ItemStack stack, int slot) {
        return slot < 1;
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isItemValid(ItemStack stack, int slot, ItemStack bullet) {
        if (!this.isSocketSlotAvailable(stack, slot)) {
            return false;
        } else if (!bullet.isEmpty() && bullet.getItem() instanceof ISpellContainer) {
            ISpellContainer container = (ISpellContainer) bullet.getItem();
            return container.containsSpell(bullet);
        } else {
            return false;
        }
    }

    @Override
    public boolean canLoopcast(ItemStack stack) {
        return true;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem ent) {
        FlowColorsHelper.clearColorizer(ent.getItem());
        return super.onEntityItemUpdate(ent);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            TooltipHelper.addDynamic(tooltip, getTranslationKey(stack) + ".desc");
            String componentName = TooltipHelper.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
            TooltipHelper.addToTooltip(tooltip, "psimisc.spellSelected", componentName);

        });
    }
}
