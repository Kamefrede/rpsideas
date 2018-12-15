package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.entity.EntityGaussPulse;
import com.kamefrede.rpsideas.util.RPSCreativeTab;
import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import com.kamefrede.rpsideas.util.helpers.FlowColorsHelper;
import com.kamefrede.rpsideas.util.helpers.IFlowColorAcceptor;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.LibItemNames;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.item.ItemMod;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;

public class ItemGaussRifle extends ItemMod implements IItemColorProvider, IFlowColorAcceptor { // TODO: 12/15/18 look at
    protected ItemGaussRifle() {
        super(LibItemNames.ITEM_GAUSS_RIFLE);
        setCreativeTab(RPSCreativeTab.INSTANCE);
        setMaxStackSize(1);
    }

    @Override
    public IItemColor getItemColor() {
        return (stack, tintindex) -> {
            if (tintindex == 0)
                return ClientHelpers.pulseColor(0xB87333);
            else if (tintindex == 1) {
                ItemStack colorizer = FlowColorsHelper.getColorizer(stack);
                if (colorizer.isEmpty())
                    return 0;
                else
                    return Psi.proxy.getColorizerColor(colorizer).getRGB();
            }

            return -1;
        };
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(playerIn);
        ItemStack ammo = findAmmo(playerIn);
        ItemStack cad = PsiAPI.getPlayerCAD(playerIn);
        if (cad.isEmpty()) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        if (playerIn.capabilities.isCreativeMode || data.availablePsi > 0 || (!ammo.isEmpty() && data.availablePsi > 0)) {
            boolean wasEmpty = ammo.isEmpty();
            boolean noneLeft = false;
            if (!playerIn.capabilities.isCreativeMode) {
                if (wasEmpty) {
                    int cadBattery = cad.isEmpty() ? 0 : ((ICAD) cad.getItem()).getStoredPsi(cad);
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
                status = EntityGaussPulse.AmmoStatus.NOTAMMO;
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
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));

    }

    public ItemStack findAmmo(EntityPlayer player) {
        if (player.getHeldItemOffhand().getItem() == ModItems.gaussBullet) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (player.getHeldItemMainhand().getItem() == ModItems.gaussBullet) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory() - 1; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() == ModItems.gaussBullet) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public String getModNamespace() {
        return RPSIdeas.MODID;
    }
}
