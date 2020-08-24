package xyz.kamefrede.rpsideas.items.components;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.items.base.INoCraftingComponent;
import xyz.kamefrede.rpsideas.items.base.ItemComponent;
import xyz.kamefrede.rpsideas.util.helpers.ClientHelpers;
import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.*;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemCreativeSuspension extends ItemComponent implements ICADColorizer, INoCraftingComponent {
    public ItemCreativeSuspension() {
        super(RPSItemNames.CREATIVE_COLORIZER);
    }

    @SubscribeEvent
    public static void modifyStats(CADStatEvent event) {
        ItemStack cad = event.getCad();
        ICAD cadItem = (ICAD) cad.getItem();
        ItemStack colorizer = cadItem.getComponentInSlot(cad, EnumCADComponent.DYE);
        if (!colorizer.isEmpty() && colorizer.getItem() instanceof ItemCreativeSuspension)
            event.setStatValue(-1);
        else {
            ItemStack assembly = cadItem.getComponentInSlot(cad, EnumCADComponent.ASSEMBLY);
            if (!assembly.isEmpty()) {
                ICADComponent componentItem = (ICADComponent) assembly.getItem();
                if (componentItem.getCADStatValue(assembly, EnumCADStat.POTENCY) == -1 &&
                        componentItem.getCADStatValue(assembly, EnumCADStat.EFFICIENCY) == -1)
                    event.setStatValue(-1);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        addTooltipTag(tooltip, true, getTranslationKey(stack) + ".desc");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColor(ItemStack itemStack) {
        return ClientHelpers.slideColor(0x9B3200, 0xFFAB00, 0.02f);
    }
}
