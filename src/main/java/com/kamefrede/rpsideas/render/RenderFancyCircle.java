package com.kamefrede.rpsideas.render;

import com.kamefrede.rpsideas.entity.EntityFancyCircle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderFancyCircle extends Render<EntityFancyCircle> {
    private static final ResourceLocation[] layers = new ResourceLocation[] {
            new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 0)),
            new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 1)),
            new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 2)),
    };

    private static final float BRIGHTNESS_FACTOR = 0.7F;

    public RenderFancyCircle(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityFancyCircle entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        int color = entity.getColor();
        float alive = entity.ticksExisted + partialTicks;
        float scale = Math.min(1F, alive / 5);
        if (alive > entity.getLiveTime() - 5)
            scale = 1F - Math.min(1F, Math.max(0, alive - (entity.getLiveTime() - 5f)) / 5);

        GlStateManager.pushMatrix();
        float facingZ = entity.getZFacing();
        double ratio = 0.0625 * entity.getScale();
        GlStateManager.translate(x, y, z);

        if (facingZ == -1)
            GlStateManager.rotate(180, 1, 0, 0);
        else if (facingZ != 1)
            GlStateManager.rotate((float) (Math.acos(facingZ) * 180 / Math.PI),
                    -entity.getYFacing(), entity.getXFacing(), 0);
        GlStateManager.translate(-scale * 2, -scale * 2, 0.01);
        GlStateManager.scale(ratio * scale, ratio * scale, ratio);

        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);



        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        for (int i = 0; i < layers.length; i++) {
            int rValue = r;
            int gValue = g;
            int bValue = b;

            if (i == 1)
                rValue = gValue = bValue = 0xFF;
            else if (i == 2) {
                int minBrightness = (int) (1 / (1 - BRIGHTNESS_FACTOR));
                if (r == 0 && g == 0 && b == 0)
                    r = g = b = minBrightness;
                if (r > 0 && r < minBrightness) r = minBrightness;
                if (g > 0 && g < minBrightness) g = minBrightness;
                if (b > 0 && b < minBrightness) b = minBrightness;

                r = (int) Math.min(r / BRIGHTNESS_FACTOR, 0xFF);
                r = (int) Math.min(g / BRIGHTNESS_FACTOR, 0xFF);
                r = (int) Math.min(b / BRIGHTNESS_FACTOR, 0xFF);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(32, 32, 0);
            GlStateManager.rotate(i == 0 ? -alive : alive, 0, 0, 1);
            GlStateManager.translate(-32, -32, 0);

            GlStateManager.color(r / 255f, g / 255f, b / 255f);

            Minecraft.getMinecraft().renderEngine.bindTexture(layers[i]);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 64, 64, 64, 64);
            GlStateManager.popMatrix();

            GlStateManager.translate(0, 0, -0.5);
        }

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityFancyCircle entity) {
        return null;
    }

}