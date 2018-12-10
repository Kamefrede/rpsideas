package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.helpers.FlowColorsHelper;
import com.github.kamefrede.rpsideas.util.helpers.MiscHelpers;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.IPsimetalTool;

import java.util.List;

public class ItemInlineCaster extends Item implements IPsimetalTool {
    public ItemInlineCaster() {
        setMaxStackSize(1);
    }

    //TODO Liblib glow

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        ItemStack cad = PsiAPI.getPlayerCAD(player);

        if(!cad.isEmpty()) {
            ItemStack bullet = getBulletInSocket(held, getSelectedSlot(held));
            if(bullet.isEmpty()) {
                //Craft redstone into psidust
                if(ItemCAD.craft(player, new ItemStack(Items.REDSTONE), new ItemStack(ModItems.material, 1, 0))) {
                    if(!world.isRemote) {
                        MiscHelpers.emitSoundFromEntity(world, player, PsiSoundHandler.cadShoot, .5f, (float) (.5f + Math.random() * .5));
                    }

                    data.deductPsi(100, 60, true);
                    if(data.level == 0) data.levelUp();

                    return new ActionResult<>(EnumActionResult.SUCCESS, held);
                }
            } else {
                //Cast a spell with the bullet.
                ItemCAD.cast(world, player, data, bullet, cad, 40, 25, 0.5f, null);
                return new ActionResult<>(EnumActionResult.SUCCESS, held);
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    //The rest is taken care of in IPsiAddonTool
    @Override
    public boolean isSocketSlotAvailable(ItemStack stack, int slot) {
        return slot < 1;
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem ent) {
        FlowColorsHelper.clearColorizer(ent.getItem());
        return super.onEntityItemUpdate(ent);
    }

    @Override
    public String[] getVariants() {
        return new String[0];
    }

    @Override
    public ItemMeshDefinition getCustomMeshDefinition() {
        return null;
    }

    @Override
    public String getModNamespace() {
        return Reference.MODID;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        ItemMod.tooltipIfShift(tooltip, () -> {
            String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
            ItemMod.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
        });
    }
}
