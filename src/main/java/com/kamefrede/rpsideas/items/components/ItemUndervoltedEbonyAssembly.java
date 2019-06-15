package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.effect.PotionAffinity;
import com.kamefrede.rpsideas.effect.RPSPotions;
import com.kamefrede.rpsideas.items.RPSItems;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.spell.PreSpellCastEvent;
import vazkii.psi.api.spell.SpellCastEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemUndervoltedEbonyAssembly extends ItemComponent implements IExtraVariantHolder, ICADAssembly {
    public ItemUndervoltedEbonyAssembly() {
        super(RPSItemNames.EBONY_UNDERVOLTED_ASSEMBLY);
    }

    public static final float affinityFactor = 0.005f;

    public static final String[] CAD_MODELS = {
            "ebony_undervolted_cad"
    };

    @Override
    public EnumCADComponent getComponentType(ItemStack stack) {
        return EnumCADComponent.ASSEMBLY;
    }

    @NotNull
    @Override
    public String[] getExtraVariants() {
        return CAD_MODELS;
    }


    @Override
    protected void registerStats() {
        addStat(EnumCADStat.EFFICIENCY,90);
        addStat(EnumCADStat.POTENCY, 280);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        addTooltipTag(tooltip, true, RPSIdeas.MODID + ".extra.affinity", affinityFactor * 100);
    }

    @SubscribeEvent
    public static void applyExtraEfficiency(PreSpellCastEvent event) {
        if (PotionMod.Companion.hasEffect(event.getPlayer(), RPSPotions.affinity)) {
            PotionEffect affinityEffect = Objects.requireNonNull(PotionMod.Companion.getEffect(event.getPlayer(), RPSPotions.affinity));
            event.setCost((int) Math.ceil(event.getCost() - event.getCost() * (affinityFactor * affinityEffect.getAmplifier())));
        }
    }

    @SubscribeEvent
    public static void modifyCadStat(CADStatEvent event) {
        ItemStack cad = event.getCad();
        ICAD cadItem = (ICAD) cad.getItem();
        ItemStack assembly = cadItem.getComponentInSlot(cad, EnumCADComponent.ASSEMBLY);
        if (assembly.getItem() instanceof ItemUndervoltedEbonyAssembly) {
            if (event.getStat() == EnumCADStat.PROJECTION)
                event.setStatValue(event.getStatValue() + 1);
        }
    }

    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent event) {
        if (SpellHelpers.hasComponent(event.cad, EnumCADComponent.ASSEMBLY, RPSItems.undervoltedCadAssembly)) {
            if (PotionMod.Companion.hasEffect(event.player, RPSPotions.affinity)) {
                PotionEffect affinityEffect = Objects.requireNonNull(PotionMod.Companion.getEffect(event.player, RPSPotions.affinity));
                PotionEffect newEffect = new PotionEffect(RPSPotions.affinity,
                        affinityEffect.getDuration() + 15 * 20,
                        Math.min(affinityEffect.getAmplifier() + 1, PotionAffinity.getMaxAmp()));
                affinityEffect.combine(newEffect);
            } else {
                event.player.addPotionEffect(new PotionEffect(RPSPotions.affinity, 15 * 20, 1));
            }
        }
    }

    @Override
    public ModelResourceLocation getCADModel(ItemStack stack, ItemStack cad) {
        return new ModelResourceLocation(new ResourceLocation(RPSIdeas.MODID, "ebony_undervolted_cad"), "inventory");
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent e) {
        ItemStack stack = e.getItemStack();
        Item item = stack.getItem();
        if (!(item instanceof ICAD)) return;
        if (SpellHelpers.hasComponent(stack, EnumCADComponent.ASSEMBLY, RPSItems.undervoltedCadAssembly) && GuiScreen.isShiftKeyDown() && e.getEntityPlayer() != null) {
            if (PotionMod.Companion.hasEffect(e.getEntityPlayer(), RPSPotions.affinity)) {
                PotionEffect burnoutEffect = Objects.requireNonNull(PotionMod.Companion.getEffect(e.getEntityPlayer(), RPSPotions.burnout));
                SpellHelpers.addTooltipTag(e.getToolTip(), true, RPSIdeas.MODID + ".extra.affinity_active", affinityFactor * burnoutEffect.getAmplifier() * 100);
                SpellHelpers.addTooltipTag(e.getToolTip(), true, RPSIdeas.MODID + ".explanation.affinity");
                SpellHelpers.addTooltipTag(e.getToolTip(), true, RPSIdeas.MODID + ".explanation.affinity1");
                return;
            }
            SpellHelpers.addTooltipTag(e.getToolTip(), true, RPSIdeas.MODID + ".extra.affinity", affinityFactor * 100);
            SpellHelpers.addTooltipTag(e.getToolTip(), true, RPSIdeas.MODID + ".explanation.affinity");
            SpellHelpers.addTooltipTag(e.getToolTip(), true, RPSIdeas.MODID + ".explanation.affinity1");
        }
    }
}
