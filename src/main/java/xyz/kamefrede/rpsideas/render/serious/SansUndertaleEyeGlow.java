package com.kamefrede.rpsideas.render.serious;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class SansUndertaleEyeGlow implements LayerRenderer<AbstractClientPlayer> {

    private final RenderPlayer renderPlayer;
    private static final String TAG_SANS_UNDERTALE = "rpsideas:SansUndertale";
    private static final ResourceLocation SANS_OVERLAY = new ResourceLocation(RPSIdeas.MODID, "textures/model/sans_overlay.png");
    private int time = 0;
    private boolean reset = false;

    public SansUndertaleEyeGlow(RenderPlayer player) {
        this.renderPlayer = player;
    }

    @Override
    public void doRenderLayer(@Nonnull AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        float r = 0, g = 255, b = 255;
        if (time == 0) {
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            if (data.getCustomData().getInteger(TAG_SANS_UNDERTALE) > 0) {
                if (reset) {
                    data.getCustomData().setInteger(TAG_SANS_UNDERTALE, 0);
                    reset = false;
                } else {
                    time = data.getCustomData().getInteger(TAG_SANS_UNDERTALE);
                    doSkinOverlayRender(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, r, g, b);
                    time = time - 1;
                }

            }
        } else if (time > 0) {
            if (time - 1 == 0)
                reset = true;
            doSkinOverlayRender(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, r, g, b);
            time = time - 1;
        }


    }

    private void doSkinOverlayRender(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, float r, float g, float b) {
        if (!player.isInvisibleToPlayer(player)) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            float lastBrightnessX = OpenGlHelper.lastBrightnessX;
            float lastBrightnessY = OpenGlHelper.lastBrightnessY;

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);
            GlStateManager.color(r, g, b);
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);

            this.renderPlayer.bindTexture(SANS_OVERLAY);

            renderPlayer.setModelVisibilities(player);

            renderPlayer.getMainModel().render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            GlStateManager.color(1F, 1F, 1F);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
