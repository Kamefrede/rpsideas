package com.kamefrede.rpsideas.render;

import com.kamefrede.rpsideas.entity.EntityConjuredText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;

@SideOnly(Side.CLIENT)
public class RenderConjuredText extends Render<EntityConjuredText> {
    public RenderConjuredText(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityConjuredText entity, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityPlayer player = (EntityPlayer) this.renderManager.renderViewEntity;
        int maxDist = 64;
        double d3 = entity.getDistanceSq(player);
        float f = this.renderManager.playerViewY;
        float f1 = this.renderManager.playerViewX;
        Color color = new Color(entity.getColor());
        String text = entity.getText();
        if(d3 <= maxDist * maxDist){
            drawNameplate(Minecraft.getMinecraft().fontRenderer, text, (float)x, (float)y, (float)z, 0,f, f1, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2, color);
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }



    public static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, Color color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x,y,z);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        GlStateManager.disableDepth();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)(-i - 1), (double)(-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double)(-i - 1), (double)(8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double)(i + 1), (double)(8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double)(i + 1), (double)(-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, color.getRGB());
        GlStateManager.enableDepth();

        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, color.getRGB());
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityConjuredText entity) {
        return null;
    }
}
