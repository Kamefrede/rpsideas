package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.RPSItems;
import com.kamefrede.rpsideas.items.base.ICooldownAssembly;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = RPSIdeas.MODID)
public class ItemOverclockedIvoryAssembly extends ItemComponent implements IExtraVariantHolder, ICooldownAssembly {

    public static final String[] CAD_MODELS = {
            "ivory_overclocked_cad"
    };

    private static final double cooldownFactor = 1.2;



    public ItemOverclockedIvoryAssembly() {
        super(RPSItemNames.IVORY_OVERCLOCKED_ASSEMBLY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getCADModel(ItemStack itemStack, ItemStack itemStack1) {
        return new ModelResourceLocation(new ResourceLocation(RPSIdeas.MODID, "ivory_overclocked_cad"), "inventory");
    }

    @Override
    public double getCooldownFactor(ItemStack stack) {
        return cooldownFactor;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        addTooltipTag(tooltip, false, RPSIdeas.MODID + ".extra.cooldown", cooldownFactor);
    }

    @Override
    public void registerStats() {
        addStat(EnumCADStat.EFFICIENCY, 100);
        addStat(EnumCADStat.POTENCY, 320);
    }

    @Nonnull
    @Override
    public String[] getExtraVariants() {
        return CAD_MODELS;
    }

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent e) {
        ItemStack stack = e.getItemStack();
        Item item = stack.getItem();
        if (!(item instanceof ICAD)) return;
        if (SpellHelpers.hasComponent(stack, EnumCADComponent.ASSEMBLY, RPSItems.ivoryOverclocked) && GuiScreen.isShiftKeyDown()) {
            SpellHelpers.addTooltipTag(e.getToolTip(), false, RPSIdeas.MODID + ".extra.cooldown", cooldownFactor);
        }
    }

}
