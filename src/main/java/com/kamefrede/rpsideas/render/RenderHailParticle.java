package com.kamefrede.rpsideas.render;

import com.kamefrede.rpsideas.entity.EntityHailParticle;
import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderHailParticle extends Render<EntityHailParticle> {


    public RenderHailParticle(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityHailParticle entity) {
        return null;
    }


    @Override
    public void doRender(EntityHailParticle entity, double x, double y, double z, float entityYaw, float partialTicks) {


        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableCull();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        Vec3d norm = new Vec3d(entity.motionX, entity.motionY, entity.motionZ).normalize();
        GlStateManager.rotate(90, 0, 1, 0);
        if (norm.length() > 0)
            GlStateManager.rotate((ClientTickHandler.getTicks() + partialTicks) * 50.0f, (float) norm.x, (float) norm.y, (float) norm.z);

        double s = entity.getSize() * 0.6;
        int color = entity.getColor();
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        // TOP
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, s, -s).color(r, g, b, 255).endVertex();
        buffer.pos(-s, s, s).color(r, g, b, 255).endVertex();
        buffer.pos(s, s, s).color(r, g, b, 255).endVertex();
        buffer.pos(s, s, -s).color(r, g, b, 255).endVertex();
        tess.draw();

        // BOTTOM
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, -s, -s).color(r, g, b, 255).endVertex();
        buffer.pos(-s, -s, s).color(r, g, b, 255).endVertex();
        buffer.pos(s, -s, s).color(r, g, b, 255).endVertex();
        buffer.pos(s, -s, -s).color(r, g, b, 255).endVertex();
        tess.draw();

        // TO THE RIGHT
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, -s, s).color(r, g, b, 255).endVertex();
        buffer.pos(-s, s, s).color(r, g, b, 255).endVertex();
        buffer.pos(s, s, s).color(r, g, b, 255).endVertex();
        buffer.pos(s, -s, s).color(r, g, b, 255).endVertex();
        tess.draw();

        // TO THE LEFT
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, -s, -s).color(r, g, b, 255).endVertex();
        buffer.pos(-s, s, -s).color(r, g, b, 255).endVertex();
        buffer.pos(s, s, -s).color(r, g, b, 255).endVertex();
        buffer.pos(s, -s, -s).color(r, g, b, 255).endVertex();
        tess.draw();

        // FRONT
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(s, -s, -s).color(r, g, b, 255).endVertex();
        buffer.pos(s, s, -s).color(r, g, b, 255).endVertex();
        buffer.pos(s, s, s).color(r, g, b, 255).endVertex();
        buffer.pos(s, -s, s).color(r, g, b, 255).endVertex();
        tess.draw();

        // BACK
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, -s, -s).color(r, g, b, 255).endVertex();
        buffer.pos(-s, s, -s).color(r, g, b, 255).endVertex();
        buffer.pos(-s, s, s).color(r, g, b, 255).endVertex();
        buffer.pos(-s, -s, s).color(r, g, b, 255).endVertex();
        tess.draw();

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
