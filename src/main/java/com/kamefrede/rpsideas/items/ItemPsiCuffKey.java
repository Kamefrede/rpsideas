package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class ItemPsiCuffKey extends ItemMod {
    public ItemPsiCuffKey() {
        super(RPSItemNames.PSI_CUFF_KEY);
        setMaxStackSize(1);
    }

    private static final String TAG_CUFFED = "rpsideas:cuffed";
    private static final String TAG_KEYNAME = "rpsideas:cuffKeyName";

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (playerIn.isSneaking() && target instanceof EntityPlayer) {

            if (!stack.hasDisplayName())
                return false;
            String keyName = stack.getDisplayName();

            EntityPlayer targetPlayer = (EntityPlayer) target;
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(targetPlayer);
            if (data.getCustomData().getBoolean(TAG_CUFFED)) {
                data.getCustomData().removeTag(TAG_CUFFED);
                if (!playerIn.world.isRemote) {
                    targetPlayer.getEntityData().removeTag(TAG_KEYNAME);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

}
