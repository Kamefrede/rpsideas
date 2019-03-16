package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.network.MessageCuffSync;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

import java.util.List;

public class ItemPsiCuffKey extends ItemMod {
    public ItemPsiCuffKey() {
        super(RPSItemNames.PSI_CUFF_KEY);
        setMaxStackSize(1);
    }

    private static final String TAG_CUFFED = "rpsideas:cuffed";
    private static final String TAG_KEYNAME = "rpsideas:cuffKeyName";

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.isSneaking()) {
            ItemStack key = playerIn.getHeldItem(handIn);
            if (!key.hasDisplayName())
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, key);
            String keyName = key.getDisplayName();
            removeKey(playerIn, key, keyName);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (playerIn.isSneaking() && target instanceof EntityPlayer) {

            if (!stack.hasDisplayName())
                return false;
            String keyName = stack.getDisplayName();
            removeKey((EntityPlayer) target, stack, keyName, playerIn);
        }
        return false;
    }

    public static void removeKey(EntityPlayer cuffedPlayer, ItemStack key, String keyName, EntityPlayer sync) {
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(cuffedPlayer);
        if (data.getCustomData().getBoolean(TAG_CUFFED) && keyName.equals(cuffedPlayer.getEntityData().getString(TAG_KEYNAME))) {
            data.getCustomData().removeTag(TAG_CUFFED);
            if (!cuffedPlayer.world.isRemote) {
                cuffedPlayer.getEntityData().removeTag(TAG_KEYNAME);
                PacketHandler.NETWORK.sendTo(new MessageCuffSync(cuffedPlayer.getEntityId(), false), (EntityPlayerMP) sync);
            }
            data.save();
            if (cuffedPlayer instanceof EntityPlayerMP)
                PacketHandler.NETWORK.sendTo(new MessageDataSync(data), (EntityPlayerMP) cuffedPlayer);

        }
    }

    public static void removeKey(EntityPlayer cuffedPlayer, ItemStack key, String keyName) {
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(cuffedPlayer);
        if (data.getCustomData().getBoolean(TAG_CUFFED) && keyName.equals(cuffedPlayer.getEntityData().getString(TAG_KEYNAME))) {
            data.getCustomData().removeTag(TAG_CUFFED);
            if (!cuffedPlayer.world.isRemote) {
                cuffedPlayer.getEntityData().removeTag(TAG_KEYNAME);
                PacketHandler.NETWORK.sendToAllAround(new MessageCuffSync(cuffedPlayer.getEntityId(), false), new NetworkRegistry.TargetPoint(cuffedPlayer.dimension, cuffedPlayer.posX, cuffedPlayer.posY, cuffedPlayer.posZ, 32));
            }
            data.save();
            if (cuffedPlayer instanceof EntityPlayerMP)
                NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) cuffedPlayer);

        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @javax.annotation.Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            TooltipHelper.addDynamic(tooltip, getTranslationKey(stack) + ".desc");
        });
    }

}
