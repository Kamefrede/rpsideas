package com.github.kamefrede.rpsideas.effect.base;

import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.common.Psi;

import java.util.HashMap;
import java.util.Map;

public class PotionModColorized extends Potion {

    private static final String modid = Reference.MODID;
    private static final ResourceLocation resource = new ResourceLocation(modid, "textures/gui/potions.png");

    public static class Companion{
        private static HashMap iconIndexByModId = new HashMap();

        public static boolean hasEffect(EntityLivingBase entityLivingBase, Potion potion){
            return entityLivingBase.getActivePotionEffect(potion) != null;
        }

        public static PotionEffect getEffect(EntityLivingBase entityLivingBase, Potion potion){
            return entityLivingBase.getActivePotionEffect(potion);
        }
    }

    protected PotionModColorized(String name, boolean badEffect, int color) {
        super(badEffect, color);
        int iconIndex =  Companion.iconIndexByModId.get(modid) == null ? 0 : (int)Companion.iconIndexByModId.get(modid) ;
        Companion.iconIndexByModId.put(modid, iconIndex + 1);
        int iconX = iconIndex % 8;
        int iconY = iconIndex / 8;

        setPotionName(modid + ".potion" +  name);
        if(!badEffect)
            setBeneficial();
    }

    public boolean hasEffect(EntityLivingBase ent){
        return Companion.hasEffect(ent, this);
    }

    public PotionEffect getEffect(EntityLivingBase ent){
        return Companion.getEffect(ent, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        int color = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack cad = PsiAPI.getPlayerCAD(Minecraft.getMinecraft().player);
        if(!cad.isEmpty()) color = Psi.proxy.getCADColor(cad).getRGB();
        Triple<Integer, Integer, Integer> pulse = pulseColor(color);
        GlStateManager.color(pulse.getLeft() / 255f, pulse.getMiddle() / 255f, pulse.getRight() / 255f);
        super.renderHUDEffect(x, y, effect, mc, alpha);
        GlStateManager.color(1f, 1f, 1f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        int color = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack cad = PsiAPI.getPlayerCAD(Minecraft.getMinecraft().player);
        if(!cad.isEmpty()) color = Psi.proxy.getCADColor(cad).getRGB();
        Triple<Integer, Integer, Integer> pulse = pulseColor(color);
        GlStateManager.color(pulse.getLeft() / 255f, pulse.getMiddle() / 255f, pulse.getRight() / 255f);
        super.renderInventoryEffect(x, y, effect, mc);
        GlStateManager.color(1f, 1f, 1f);
    }

    public Triple<Integer, Integer, Integer> pulseColor(Integer rgb){
        int add = (int)(MathHelper.sin(ClientTickHandler.ticksInGame * 0.2f) * 32);
        int r = (rgb & (0xFF << 16)) >> 16;
        int g= (rgb & (0xFF << 8)) >> 8;
        int b = (rgb & (0xFF));
        return Triple.of(Math.max(Math.min(r + add, 255), 0),
                Math.max(Math.min(b + add, 255), 0),
                Math.max(Math.min(g + add, 255), 0));
    }
}
