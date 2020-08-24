package xyz.kamefrede.rpsideas.items.components;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.util.helpers.ClientHelpers;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;
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
import vazkii.psi.api.spell.detonator.DetonationEvent;
import vazkii.psi.common.entity.EntitySpellCharge;
import vazkii.psi.common.item.ItemDetonator;

import java.util.List;

import static vazkii.psi.api.spell.SpellContext.MAX_DISTANCE;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemTriggerSensor extends ItemMod implements IExosuitSensor, IItemColorProvider {
    public static final String EVENT_TRIGGER = RPSIdeas.MODID + ".event.spell_detonate";

    public ItemTriggerSensor() {
        super(RPSItemNames.TRIGGER_SENSOR);
        setMaxStackSize(1);
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
