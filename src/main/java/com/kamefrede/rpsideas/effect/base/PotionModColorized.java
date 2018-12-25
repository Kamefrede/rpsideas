package com.kamefrede.rpsideas.effect.base;

import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;

import javax.annotation.Nonnull;

public class PotionModColorized extends PotionMod {

    protected PotionModColorized(String name, boolean badEffect, int color) {
        super(name, badEffect, color);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, @Nonnull Minecraft mc, float alpha) {
        int pulse = getPulsedColor();
        GlStateManager.color(((pulse >> 16) & 0xFF) / 255f, ((pulse >> 8) & 0xFF) / 255f, (pulse & 0xFF) / 255f, alpha);

        super.renderHUDEffect(x, y, effect, mc, alpha);

        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, @Nonnull PotionEffect effect, @Nonnull Minecraft mc) {
        int pulse = getPulsedColor();
        GlStateManager.color(((pulse >> 16) & 0xFF) / 255f, ((pulse >> 8) & 0xFF) / 255f, (pulse & 0xFF) / 255f);

        super.renderInventoryEffect(x, y, effect, mc);

        GlStateManager.color(1f, 1f, 1f);
    }


    @SideOnly(Side.CLIENT)
    public int getPulsedColor() {
        int color = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack cad = PsiAPI.getPlayerCAD(Minecraft.getMinecraft().player);
        if (!cad.isEmpty()) color = Psi.proxy.getCADColor(cad).getRGB();
        return ClientHelpers.pulseColor(color);
    }
}
