package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.effect.PotionBurnout;
import com.kamefrede.rpsideas.effect.RPSPotions;
import com.kamefrede.rpsideas.items.RPSItems;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.spell.PreSpellCastEvent;
import vazkii.psi.api.spell.SpellCastEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemOvervoltedEbonyAssembly extends ItemComponent implements IExtraVariantHolder {
    public ItemOvervoltedEbonyAssembly() {
        super(RPSItemNames.EBONY_OVERVOLTED_ASSEMBLY);
    }

    public static final float burnoutFactor = 0.05f;

    public static final String[] CAD_MODELS = {
            "ebony_overvolted_cad"
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
        addStat(EnumCADStat.POTENCY, 420);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (PotionMod.Companion.hasEffect(player, RPSPotions.burnout)) {
            PotionEffect burnoutEffect = Objects.requireNonNull(PotionMod.Companion.getEffect(player, RPSPotions.burnout));
            addTooltipTag(tooltip, false, RPSIdeas.MODID + ".extra.burnout_active", burnoutFactor * burnoutEffect.getAmplifier() * 100);
        }
        addTooltipTag(tooltip, false, RPSIdeas.MODID + ".extra.burnout", burnoutFactor * 100 );
    }


    @SubscribeEvent
    public static void addBurnout(SpellCastEvent event) {
        if (SpellHelpers.hasComponent(event.cad, EnumCADComponent.ASSEMBLY, RPSItems.overvoltedCadAssembly)) {
            if (PotionMod.Companion.hasEffect(event.player, RPSPotions.burnout)) {
                PotionEffect burnoutEffect = Objects.requireNonNull(PotionMod.Companion.getEffect(event.player, RPSPotions.burnout));
                PotionEffect newEffect = new PotionEffect(RPSPotions.burnout,
                        burnoutEffect.getDuration() + 10,
                        Math.min(burnoutEffect.getAmplifier() + 1, PotionBurnout.getMaxAmp()));
                burnoutEffect.combine(newEffect);
            } else {
                event.player.addPotionEffect(new PotionEffect(RPSPotions.burnout, 10, 1));
            }
        }

    }

    @SubscribeEvent
    public static void modifyCost(PreSpellCastEvent event) {
        if (PotionMod.Companion.hasEffect(event.getPlayer(), RPSPotions.burnout)) {
            PotionEffect burnoutEffect = Objects.requireNonNull(PotionMod.Companion.getEffect(event.getPlayer(), RPSPotions.burnout));
            event.setCost((int) Math.ceil(event.getCost() + event.getCost() * (burnoutFactor * burnoutEffect.getAmplifier())));
        }
    }



}
