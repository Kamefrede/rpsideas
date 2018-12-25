package com.kamefrede.rpsideas.util.helpers;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.RPSItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class FlowColorsHelper {

    private static final String TAG_FLOW_COLOR = "FlowColor";

    @SubscribeEvent
    public static void playerUpdate(LivingEvent.LivingUpdateEvent e) {
        EntityLivingBase entity = e.getEntityLiving();
        if (entity.world.isRemote) return;

        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack cad = PsiAPI.getPlayerCAD(player);
            if (cad.isEmpty())
                clearColorizer(player);
            else {
                ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
                if (colorizer.isEmpty())
                    colorizer = new ItemStack(RPSItems.liquidColorizer);
                applyColorizer(player, colorizer);
            }
        }
    }

    public static void applyColorizer(EntityPlayer player, ItemStack color) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack slot = player.inventory.getStackInSlot(i);
            if (slot.isEmpty()) continue;
            if (!(slot.getItem() instanceof IFlowColorAcceptor)) continue;
            applyColorizer(slot, color);
        }
    }

    public static void applyColorizer(ItemStack colorable, ItemStack colorizer) {
        ItemNBTHelper.setCompound(colorable, TAG_FLOW_COLOR, colorizer.writeToNBT(new NBTTagCompound()));
    }

    public static void clearColorizer(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack slot = player.inventory.getStackInSlot(i);
            if (slot.isEmpty()) continue;
            if (!(slot.getItem() instanceof IFlowColorAcceptor)) continue;
            clearColorizer(slot);
        }
    }

    public static void clearColorizer(ItemStack colorable) {
        if (ItemNBTHelper.detectNBT(colorable)) ItemNBTHelper.getNBT(colorable).removeTag(TAG_FLOW_COLOR);
    }

    public static ItemStack getColorizer(ItemStack colorable) {
        NBTTagCompound colorizer = ItemNBTHelper.getCompound(colorable, TAG_FLOW_COLOR, true);
        if (colorizer == null) return ItemStack.EMPTY;
        else return new ItemStack(colorizer);
    }
}
