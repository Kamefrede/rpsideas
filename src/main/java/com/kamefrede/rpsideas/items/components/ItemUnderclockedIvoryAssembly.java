package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.api.spell.PreSpellCastEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = RPSIdeas.MODID)
public class ItemUnderclockedIvoryAssembly extends ItemComponent implements IExtraVariantHolder, ICADAssembly {

    public static final String[] CAD_MODELS = {
            "cad_ivory_overclocked"
    };

    private static final double cooldownFactor = 0.75;


    public ItemUnderclockedIvoryAssembly() {
        super(RPSItemNames.IVORY_UNDERCLOCKED_ASSEMBLY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getCADModel(ItemStack itemStack, ItemStack itemStack1) {
        return new ModelResourceLocation(new ResourceLocation(RPSIdeas.MODID, "cad_ivory_overclocked"), "inventory");
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack stack) {
        return EnumCADComponent.ASSEMBLY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        addTooltipTag(tooltip, true, RPSIdeas.MODID + ".extra.cooldown", cooldownFactor);
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

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void preSpellCast(PreSpellCastEvent event) {
        if (((ICAD) event.getCad().getItem()).getComponentInSlot(event.getCad(), EnumCADComponent.ASSEMBLY).getItem() instanceof ItemOverclockedIvoryAssembly) {
            event.setCooldown((int) (event.getCooldown() * cooldownFactor));
        }
    }
}
