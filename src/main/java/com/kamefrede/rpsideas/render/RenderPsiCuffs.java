package com.kamefrede.rpsideas.render;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import javax.annotation.Nonnull;
import java.awt.*;

public class RenderPsiCuffs implements LayerRenderer<AbstractClientPlayer> {

    private static final ResourceLocation TEXTURE_CUFFS_LIGHT_OVERLAY = new ResourceLocation(RPSIdeas.MODID, "textures/model/cuffs_light_overlay.png");
    private static final ResourceLocation TEXTURE_CUFFS_OVERLAY = new ResourceLocation(RPSIdeas.MODID, "textures/model/cuffs_overlay.png");
    private final RenderPlayer renderPlayer;

    public RenderPsiCuffs(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    private static final String TAG_CUFFED = "rpsideas:cuffed";

    @Override
    public void doRenderLayer(@Nonnull AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(entitylivingbaseIn);
        if (data != null && data.getCustomData() != null && !data.getCustomData().getBoolean(TAG_CUFFED))
            return;
        ItemStack cad = PsiAPI.getPlayerCAD(entitylivingbaseIn);

        boolean hasCad = !cad.isEmpty();
        float r = 0, g = 0, b = 0;
        if (hasCad) {
            Color color = Psi.proxy.getCADColor(cad);
            r = color.getRed() / 255f;
            g = color.getGreen() / 255f;
            b = color.getBlue() / 255f;
        }
        doSkinOverlayRender(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, r, g, b, hasCad);
        doCuffsRender(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

    }

    private void doSkinOverlayRender(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, float r, float g, float b, boolean hasCad) {
        if (hasCad && !player.isInvisibleToPlayer(player)) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            float lastBrightnessX = OpenGlHelper.lastBrightnessX;
            float lastBrightnessY = OpenGlHelper.lastBrightnessY;

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);
            GlStateManager.color(r, g, b);
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);

            this.renderPlayer.bindTexture(TEXTURE_CUFFS_LIGHT_OVERLAY);

            renderPlayer.setModelVisibilities(player);

            renderPlayer.getMainModel().render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            GlStateManager.color(1F, 1F, 1F);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    private void doCuffsRender(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!player.isInvisibleToPlayer(player)) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            float lastBrightnessX = OpenGlHelper.lastBrightnessX;
            float lastBrightnessY = OpenGlHelper.lastBrightnessY;

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);

            this.renderPlayer.bindTexture(TEXTURE_CUFFS_OVERLAY);

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
