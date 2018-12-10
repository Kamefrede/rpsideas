package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.items.base.IPsiAddonTool;
import com.github.kamefrede.rpsideas.util.helpers.FlowColorsHelper;
import net.minecraft.item.ItemSword;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.translation.I18n;
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

import javax.annotation.Nullable;
import java.util.List;

public class ItemFlowSword extends ItemSword implements IPsiAddonTool {
    public ItemFlowSword(boolean ebony) {
        super(PsiAPI.PSIMETAL_TOOL_MATERIAL);
        this.ebony = ebony;
    }

    final boolean ebony;

    //TODO liblib glow

    @Override
    public boolean hitEntity(ItemStack sword, EntityLivingBase target, EntityLivingBase attacker) {
        super.hitEntity(sword, target, attacker);

        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;

            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            ItemStack cad = PsiAPI.getPlayerCAD(player);

            if(!cad.isEmpty()) {
                ItemStack bullet = getBulletInSocket(sword, getSelectedSlot(sword));
                ItemCAD.cast(player.world, player, data, bullet, cad, 5, 10, 0.05f, spellContext -> {
                    spellContext.attackedEntity = target;
                });
            }
        }

        return true;
    }

    @Override
    public void onUpdate(ItemStack sword, World world, Entity entity, int itemSlot, boolean isSelected) {
        ItemPsimetalTool.regen(sword, entity, isSelected);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
        ItemMod.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if(repair.getItem() == ModItems.material) {
            return repair.getItemDamage() == (ebony ? 4 : 5);
        } else return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem ent) {
        FlowColorsHelper.clearColorizer(ent.getItem());
        return super.onEntityItemUpdate(ent);
    }
}
