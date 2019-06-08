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
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.cad.CADStatEvent;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.SpellCastEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemUndervoltedEbonyAssembly extends ItemComponent implements IExtraVariantHolder {
    public ItemUndervoltedEbonyAssembly() {
        super(RPSItemNames.EBONY_UNDERVOLTED_ASSEMBLY);
    }

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
    protected void addStat(EnumCADStat stat, int value) {
        addStat(EnumCADStat.EFFICIENCY,90);
        addStat(EnumCADStat.POTENCY, 280);
    }

    //TODO casting spells spins up your cad, giving a small, flat efficiency upgrade until it expires

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
                        affinityEffect.getDuration() + 15,
                        Math.min(affinityEffect.getAmplifier() + 1, PotionAffinity.getMaxAmp()));
                affinityEffect.combine(newEffect);
            } else {
                event.player.addPotionEffect(new PotionEffect(RPSPotions.affinity, 15, 1));
            }
        }
    }
}
