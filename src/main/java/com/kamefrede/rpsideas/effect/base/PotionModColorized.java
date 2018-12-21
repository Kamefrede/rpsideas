package com.kamefrede.rpsideas.effect.base;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;

public class PotionModColorized extends Potion {

    private static final ResourceLocation resource = new ResourceLocation(RPSIdeas.MODID, "textures/gui/potions.png");

    private static int iconIndex = 0;

    private final int iconX;
    private final int iconY;

    protected PotionModColorized(String name, boolean badEffect, int color) {
        super(badEffect, color);
        iconIndex++;

        iconX = iconIndex % 8;
        iconY = iconIndex / 8;

        setPotionName(RPSIdeas.MODID + ".potion." + name);
        if (!badEffect)
            setBeneficial();
    }

    public static boolean hasEffect(EntityLivingBase entityLivingBase, Potion potion) {
        return entityLivingBase.getActivePotionEffect(potion) != null;
    }

    public static PotionEffect getEffect(EntityLivingBase entityLivingBase, Potion potion) {
        return entityLivingBase.getActivePotionEffect(potion);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        renderIcon(x, y, mc, alpha);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        renderIcon(x, y, mc, 1f);
    }

    @SideOnly(Side.CLIENT)
    public void renderIcon(int x, int y, Minecraft mc, float alpha) {
        int color = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack cad = PsiAPI.getPlayerCAD(Minecraft.getMinecraft().player);
        if (!cad.isEmpty()) color = Psi.proxy.getCADColor(cad).getRGB();
        int pulse = ClientHelpers.pulseColor(color);
        GlStateManager.color(((pulse >> 16) & 0xFF) / 255f, ((pulse >> 8) & 0xFF) / 255f, (pulse & 0xFF) / 255f, alpha);

        mc.renderEngine.bindTexture(resource);
        if (mc.currentScreen != null)
            mc.currentScreen.drawTexturedModalRect(x + 6, y + 7, iconX * 18, 198 + iconY * 18, 18, 18);

        GlStateManager.color(1f, 1f, 1f, 1f);
    }
}
