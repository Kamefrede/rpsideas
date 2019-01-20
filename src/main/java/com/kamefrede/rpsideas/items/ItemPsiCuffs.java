package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.util.helpers.IFlowColorAcceptor;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

public class ItemPsiCuffs extends ItemMod implements IFlowColorAcceptor {

    public ItemPsiCuffs() {
        super(RPSItemNames.PSI_CUFFS);
        setMaxStackSize(1);
    }

    private static final String TAG_CUFFED = "rpsideas:cuffed";
    private static final String TAG_KEYNAME = "rpsideas:cuffKeyName";


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.isSneaking() && ItemNBTHelper.getString(playerIn.getHeldItem(handIn), TAG_KEYNAME, null) != null) {
            ItemNBTHelper.removeEntry(playerIn.getHeldItem(handIn), TAG_KEYNAME);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (playerIn.isSneaking() && target instanceof EntityPlayer) {
            String keyName = ItemNBTHelper.getString(stack, TAG_KEYNAME, null);
            if (keyName == null)
                return false;

            EntityPlayer targetPlayer = (EntityPlayer) target;
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(targetPlayer);
            if (data.getCustomData() != null && !data.getCustomData().getBoolean(TAG_CUFFED)) {
                data.getCustomData().setBoolean(TAG_CUFFED, true);
                if (!playerIn.world.isRemote) {
                    targetPlayer.getEntityData().setString(TAG_KEYNAME, keyName);
                }
                stack.shrink(1);
                data.save();
                if (targetPlayer instanceof EntityPlayerMP)
                    NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) targetPlayer);
                return true;
            }
        }
        return false;
    }


}
