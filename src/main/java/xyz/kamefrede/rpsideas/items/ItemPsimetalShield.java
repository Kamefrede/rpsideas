package xyz.kamefrede.rpsideas.items;

import com.teamwizardry.librarianlib.features.base.item.ItemModShield;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.tool.IPsimetalTool;

import java.util.List;

public class ItemPsimetalShield extends ItemModShield implements IPsimetalTool {


    public ItemPsimetalShield(@NotNull String name) {
        super(name, 900);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        String componentName = TooltipHelper.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
        TooltipHelper.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
    }

    @Override
    public void onDamageBlocked(@NotNull ItemStack stack, @NotNull EntityPlayer player, @Nullable Entity indirectSource, @Nullable Entity directSource, float amount, @NotNull DamageSource source) {
        super.onDamageBlocked(stack, player, indirectSource, directSource, amount, source);
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        ItemStack cad = PsiAPI.getPlayerCAD(player);
        if (!cad.isEmpty() && (indirectSource != null || directSource != null)) {
            ItemStack bullet = getBulletInSocket(stack, getSelectedSlot(stack));
            ItemCAD.cast(player.world, player, data, bullet, cad, 5, 10, 0.05f, spellContext -> {
                        if (directSource instanceof EntityLivingBase)
                            spellContext.attackingEntity = (EntityLivingBase) directSource;
                        else if (indirectSource instanceof EntityLivingBase)
                            spellContext.attackingEntity = (EntityLivingBase) indirectSource;
                        spellContext.tool = stack;

                    }

            );
        }
    }

    @Override
    public boolean onAxeBlocked(@NotNull ItemStack stack, @NotNull EntityPlayer player, @NotNull EntityLivingBase attacker, float amount, @NotNull DamageSource source) {
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        int psi = data.availablePsi;
        data.deductPsi(data.totalPsi / 4, 20, true);
        return psi >= (data.totalPsi / 4);

    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        ItemPsimetalHoe.regenPsi(stack, entityIn, isSelected);
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repairItem) {
        return OreDictionary.containsMatch(false, OreDictionary.getOres("ingotPsi"), repairItem) || super.getIsRepairable(toRepair, repairItem);
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }

}
