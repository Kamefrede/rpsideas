package com.kamefrede.rpsideas.items.flow;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.IPsiAddonTool;
import com.kamefrede.rpsideas.util.helpers.FlowColorsHelper;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.arl.item.ItemModSword;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.ItemPsimetalTool;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemFlowSword extends ItemModSword implements IPsiAddonTool {
    private final boolean ebony;

    public ItemFlowSword(String name, boolean ebony) {
        super(name, PsiAPI.PSIMETAL_TOOL_MATERIAL);
        this.ebony = ebony;
    }

    @Override
    public boolean hitEntity(ItemStack sword, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
        super.hitEntity(sword, target, attacker);

        if (attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;

            PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
            ItemStack cad = PsiAPI.getPlayerCAD(player);

            if (data != null && !cad.isEmpty()) {
                ItemStack bullet = getBulletInSocket(sword, getSelectedSlot(sword));
                ItemCAD.cast(player.world, player, data, bullet, cad, 5, 10, 0.05f, spellContext ->
                        spellContext.attackedEntity = target);
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
    public String getModNamespace() {
        return RPSIdeas.MODID;
    }
}
