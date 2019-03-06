package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.ICooldownAssembly;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADStat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = RPSIdeas.MODID)
public class ItemUnderclockedIvoryAssembly extends ItemComponent implements IExtraVariantHolder, ICooldownAssembly {

    public static final String[] CAD_MODELS = {
            "ivory_underclocked_cad"
    };

    private static final double cooldownFactor = 0.75;


    public ItemUnderclockedIvoryAssembly() {
        super(RPSItemNames.IVORY_UNDERCLOCKED_ASSEMBLY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getCADModel(ItemStack itemStack, ItemStack itemStack1) {
        return new ModelResourceLocation(new ResourceLocation(RPSIdeas.MODID, "ivory_underclocked_cad"), "inventory");
    }

    @Override
    public double getCooldownFactor(ItemStack stack) {
        return cooldownFactor;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        addTooltipTag(tooltip, true, RPSIdeas.MODID + ".extra.cooldown", cooldownFactor);
    }

    @Override
    public void registerStats() {
        addStat(EnumCADStat.EFFICIENCY, 75);
        addStat(EnumCADStat.POTENCY, 320);
    }

    @Nonnull
    @Override
    public String[] getExtraVariants() {
        return CAD_MODELS;
    }
}
