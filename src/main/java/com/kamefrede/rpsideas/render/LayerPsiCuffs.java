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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerPsiCuffs implements LayerRenderer<AbstractClientPlayer> {

    private static final ResourceLocation TEXTURE_CUFFS_LIGHT_OVERLAY = new ResourceLocation(RPSIdeas.MODID, "textures/model/cuffs_light_overlay.png");
    private static final ResourceLocation TEXTURE_CUFFS_OVERLAY = new ResourceLocation(RPSIdeas.MODID, "textures/model/cuffs_overlay.png");
    private final RenderPlayer renderPlayer;

    public LayerPsiCuffs(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    private static final String TAG_CUFFED = "rpsideas:cuffed";

    @Override
    public void doRenderLayer(@Nonnull AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(entitylivingbaseIn);
        if (!data.getCustomData().getBoolean(TAG_CUFFED))
            return;
        ItemStack cad = PsiAPI.getPlayerCAD(entitylivingbaseIn);

        boolean hasCad = !cad.isEmpty();
        float r = 0, g = 0, b = 0;
        if (hasCad) {
            int color = Psi.proxy.getColorForCAD(cad);
            r = SpellHelpers.getR(color);
            g = SpellHelpers.getG(color);
            b = SpellHelpers.getB(color);
        }
        doCuffsRender(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        doCuffLightOverlay(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, r, g, b, hasCad);
    }

    private void doCuffLightOverlay(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, float r, float g, float b, boolean hasCad) {
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

    private void doCuffsRender(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
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

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
