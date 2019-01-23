package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.entity.EntityGaussPulse;
import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import com.kamefrede.rpsideas.util.helpers.FlowColorsHelper;
import com.kamefrede.rpsideas.util.helpers.IFlowColorAcceptor;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.IGlowingItem;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import kotlin.jvm.functions.Function2;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemGaussRifle extends ItemMod implements IFlowColorAcceptor {
    protected ItemGaussRifle() {
        super(RPSItemNames.ITEM_GAUSS_RIFLE);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nullable
    public IBakedModel transformToGlow(@NotNull ItemStack itemStack, @NotNull IBakedModel model) {
        return IGlowingItem.Helper.wrapperBake(model, false, 0, 1);
    }

    @Override
    public boolean shouldDisableLightingForGlow(@NotNull ItemStack itemStack, @NotNull IBakedModel model) {
        return false;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Function2<ItemStack, Integer, Integer> getItemColorFunction() {
        return (stack, tintIndex) -> {
            if (tintIndex == 0) {
                ItemStack colorizer = FlowColorsHelper.getColorizer(stack);
                if (colorizer.isEmpty())
                    return 0;
                else
                    return Psi.proxy.getColorizerColor(colorizer).getRGB();
            } else if (tintIndex == 1) {
                return ClientHelpers.pulseColor(0xB87333);
            }

            return -1;
        };
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @javax.annotation.Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            TooltipHelper.addDynamic(tooltip, getTranslationKey(stack) + ".desc");
        });
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerIn);
        ItemStack ammo = findAmmo(playerIn);
        ItemStack cad = PsiAPI.getPlayerCAD(playerIn);

        if (cad.isEmpty())
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        if (playerIn.capabilities.isCreativeMode || data.availablePsi > 0) {
            boolean wasEmpty = ammo.isEmpty();
            boolean noneLeft = false;
            if (!playerIn.capabilities.isCreativeMode) {
                if (wasEmpty) {
                    int cadBattery = ((ICAD) cad.getItem()).getStoredPsi(cad);
                    if (data.availablePsi + cadBattery < 625) noneLeft = true;
                    data.deductPsi(625, (int) (3 * playerIn.getCooldownPeriod() + 10 + (noneLeft ? 50 : 0)), true);
                } else {
                    data.deductPsi(200, 10, true);
                    ammo.setCount(ammo.getCount() - 1);
                }
            }
            playerIn.swingArm(handIn);
            EntityGaussPulse.AmmoStatus status;
            if (!wasEmpty) {
                if (playerIn.capabilities.isCreativeMode)
                    status = EntityGaussPulse.AmmoStatus.DEPLETED;
                else
                    status = EntityGaussPulse.AmmoStatus.AMMO;
            } else if (noneLeft)
                status = EntityGaussPulse.AmmoStatus.BLOOD;
            else
                status = EntityGaussPulse.AmmoStatus.PSI;
            EntityGaussPulse proj = new EntityGaussPulse(worldIn, playerIn, status);
            if (!worldIn.isRemote) worldIn.spawnEntity(proj);
            Vec3d look = playerIn.getLookVec();
            playerIn.motionX -= 0.5 * look.x;
            playerIn.motionY -= 0.25 * look.y;
            playerIn.motionZ -= 0.5 * look.z;
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                    status == EntityGaussPulse.AmmoStatus.BLOOD ? PsiSoundHandler.compileError : PsiSoundHandler.cadShoot,
                    SoundCategory.PLAYERS, 1f, 1f);


            if (!playerIn.capabilities.isCreativeMode)
                playerIn.getCooldownTracker().setCooldown(this, (int) (3 * playerIn.getCooldownPeriod()));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));

    }

    public ItemStack findAmmo(EntityPlayer player) {
        if (player.getHeldItemOffhand().getItem() == RPSItems.gaussBullet) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (player.getHeldItemMainhand().getItem() == RPSItems.gaussBullet) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory() - 1; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() == RPSItems.gaussBullet)
                    return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
