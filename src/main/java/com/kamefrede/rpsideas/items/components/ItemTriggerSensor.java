package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.RPSItem;
import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.entity.EntitySpellCharge;
import vazkii.psi.common.item.ItemDetonator;

import java.util.List;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemTriggerSensor extends RPSItem implements IExosuitSensor, IItemColorProvider {
    public static final String EVENT_TRIGGER = RPSIdeas.MODID + ".event.spelldetonate";
    private static final double RANGE = 10d;

    public ItemTriggerSensor(){
        super(RPSItemNames.TRIGGER_SENSOR);
        setMaxStackSize(1);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem ev) {
        EntityPlayer player = ev.getEntityPlayer();
        ItemStack stack = ev.getItemStack();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemDetonator) {
            if (!player.world.isRemote) PsiArmorEvent.post(new PsiArmorEvent(player, EVENT_TRIGGER));

            List<EntitySpellCharge> charges = player.world.getEntitiesWithinAABB(EntitySpellCharge.class, player.getEntityBoundingBox().grow(32, 32, 32));
            if (charges.isEmpty()) {
                if(!player.world.isRemote)
                    player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1F);
                else player.swingArm(ev.getHand());
                ev.setCancellationResult(EnumActionResult.SUCCESS);
                ev.setCanceled(true);
            }
        }
    }


    @Override
    public String getEventType(ItemStack itemStack) {
        return EVENT_TRIGGER;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColor(ItemStack stack) {
        return ClientHelpers.pulseColor(0xBC650F, 0.1f, 96);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColor() {
        return (stack, tintIndex) -> {
            if (tintIndex == 1)
                return getColor(stack);
            else return -1;
        };
    }
}
