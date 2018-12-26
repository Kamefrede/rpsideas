package com.kamefrede.rpsideas.render;

import com.kamefrede.rpsideas.entity.EntityFancyCircle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.common.Psi;
import vazkii.psi.common.entity.EntitySpellCircle;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nullable;
import java.awt.*;

@SideOnly(Side.CLIENT)
public class RenderFancyCircle extends Render<EntityFancyCircle> {
    private static final ResourceLocation[] layers = new ResourceLocation[] {
            new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 0)),
            new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 1)),
            new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 2)),
    };

    public RenderFancyCircle(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityFancyCircle entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        int colorVal = entity.getColor();
        float alive = entity.ticksExisted + partialTicks;
        float s1 = entity.getScale();
        float angle = (float) (Math.acos(entity.getZFacing()) * 180 / Math.PI);
        if(alive > entity.getLiveTime() - 5)
           s1 =Math.min(entity.getScale(), Math.max(0, alive - (entity.getLiveTime() - 5)) / 5);
            //s1 =entity.getScale() -  Math.max(0, alive - (entity.getLiveTime() - 5) / 5);


        renderSpellCircle(alive, s1, x, y, z, colorVal, entity, angle);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityFancyCircle entity) {
        return null;
    }

    public static void renderSpellCircle(float time, float s1, double x, double y, double z, int colorVal, EntityFancyCircle entity, float angle) {
        Vec3d vec = entity.getDirectionVector();
        float s = 1F / 16F;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x - s1 * 2, y + 0.01, z - s1 * 2);
        GlStateManager.scale(s, s, s);
        GlStateManager.scale(s1, 1F, s1);



        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        if(vec == new Vec3d(0f,0f,1f)){
            //do nothin
        }else if(vec == new Vec3d(0f,0,-1f)){
            GlStateManager.rotate(180f, 1f,0f,0f);
        }else
            GlStateManager.rotate(angle, -entity.getYFacing(), entity.getXFacing(), 0);

        for(int i = 0; i < layers.length; i++) {
            Color color = new Color(colorVal);
            if(i == 2)
                color = color.brighter();

            float r = color.getRed() / 255F;
            float g = color.getGreen() / 255F;
            float b = color.getBlue() / 255F;

            float d = 2F / s;
            GlStateManager.pushMatrix();
            GlStateManager.translate(d, d, 0F);
            float rot = time;
            if(i == 0)
                rot = -rot;
            GlStateManager.rotate(rot, 0F, 0F, 1F);
            GlStateManager.translate(-d, -d, 0F);

            if(i == 1)
                GlStateManager.color(1F, 1F, 1F);
            else GlStateManager.color(r, g, b);

            Minecraft.getMinecraft().renderEngine.bindTexture(layers[i]);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 64, 64, 64, 64);
            GlStateManager.popMatrix();
            GlStateManager.translate(0F, 0F, -0.5F);
        }

        ShaderHandler.releaseShader();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

}
