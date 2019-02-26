package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import kotlin.jvm.functions.Function2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.entity.EntitySpellCharge;
import vazkii.psi.common.item.ItemDetonator;

import java.util.List;

import static vazkii.psi.api.spell.SpellContext.MAX_DISTANCE;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemTriggerSensor extends ItemMod implements IExosuitSensor, IItemColorProvider {
    public static final String EVENT_TRIGGER = RPSIdeas.MODID + ".event.spell_detonate";
    public static final String TRIGGER_TICK = RPSIdeas.MODID + ":LastTriggeredDetonation";

    public ItemTriggerSensor() {
        super(RPSItemNames.TRIGGER_SENSOR);
        setMaxStackSize(1);
    }

    public static void firePlayerDetonation(EntityPlayer player) {
        NBTTagCompound playerData = player.getEntityData();
        long detonated = playerData.getLong(TRIGGER_TICK);
        long worldTime = player.world.getTotalWorldTime();

        if (detonated != worldTime) {
            playerData.setLong(TRIGGER_TICK, worldTime);

            PsiArmorEvent.post(new PsiArmorEvent(player, ItemTriggerSensor.EVENT_TRIGGER));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem ev) {
        EntityPlayer player = ev.getEntityPlayer();
        ItemStack stack = ev.getItemStack();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemDetonator) {
            AxisAlignedBB boundingBox = player.getEntityBoundingBox().grow(32);

            if (player.world instanceof WorldServer) {
                WorldServer server = (WorldServer) player.world;

                List<EntityPlayer> players = player.world.getEntitiesWithinAABB(EntityPlayer.class, boundingBox,
                        e -> e != null && e.getDistanceSq(player) < MAX_DISTANCE * MAX_DISTANCE);

                for (EntityPlayer target : players)
                    SpellHelpers.scheduleTask(server, () -> firePlayerDetonation(target));
            }

            List<EntitySpellCharge> charges = player.world.getEntitiesWithinAABB(EntitySpellCharge.class, boundingBox,
                    e -> e != null && e.getDistanceSq(player) < MAX_DISTANCE * MAX_DISTANCE);
            if (charges.isEmpty()) {
                if (!player.world.isRemote)
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

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Function2<ItemStack, Integer, Integer> getItemColorFunction() {
        return (stack, tintIndex) -> {
            if (tintIndex == 1)
                return getColor(stack);
            else return -1;
        };
    }
}
