package xyz.kamefrede.rpsideas.render;

import xyz.kamefrede.rpsideas.entity.EntityHailParticle;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderHailParticle extends Render<EntityHailParticle> {


    public RenderHailParticle(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityHailParticle entity) {
        return null;
    }


    @Override
    public void doRender(@Nonnull EntityHailParticle entity, double x, double y, double z, float entityYaw, float partialTicks) {


        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        Vec3d norm = new Vec3d(entity.motionX, entity.motionY, entity.motionZ).normalize();
        GlStateManager.rotate(90, 0, 1, 0);
        if (norm.length() > 0)
            GlStateManager.rotate((ClientTickHandler.getTicks() + partialTicks) * 50.0f, (float) norm.x, (float) norm.y, (float) norm.z);

        double s = 0.25;
        int color = entity.getColor();

        GlStateManager.color(SpellHelpers.getR(color), SpellHelpers.getG(color), SpellHelpers.getB(color));
        // TOP
        buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);
        buffer.pos(-s, -s, -s).endVertex();
        buffer.pos(-s, s, -s).endVertex();
        buffer.pos(s, -s, -s).endVertex();
        buffer.pos(s, s, -s).endVertex();
        buffer.pos(s, s, s).endVertex();
        buffer.pos(-s, s, -s).endVertex();
        buffer.pos(-s, s, s).endVertex();
        buffer.pos(-s, -s, s).endVertex();
        buffer.pos(s, s, s).endVertex();
        buffer.pos(s, -s, s).endVertex();
        buffer.pos(s, -s, -s).endVertex();
        buffer.pos(-s, -s, s).endVertex();
        buffer.pos(-s, -s, -s).endVertex();
        buffer.pos(-s, s, -s).endVertex();
        tess.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
