package com.kamefrede.rpsideas.render;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.flow.ItemFlowExosuit;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;

/**
 * @author WireSegal
 * Created at 08 11 AM on 12/21/18.
 */
@SideOnly(Side.CLIENT)
public class ExosuitGlowLayer extends LayerBipedArmor {

    private static final ResourceLocation RESOURCE = new ResourceLocation(RPSIdeas.MODID, "textures/model/exosuit_overlay.png");
    private final RenderLivingBase<?> renderer;

    public ExosuitGlowLayer(RenderLivingBase<?> rendererIn) {
        super(rendererIn);
        renderer = rendererIn;
    }

    private static int getColorFromPlayer(EntityPlayer player) {
        ItemStack cad = PsiAPI.getPlayerCAD(player);
        return cad.isEmpty() ? 0 : Psi.proxy.getCADColor(cad).getRGB();
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            renderArmorLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
            renderArmorLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
            renderArmorLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
            renderArmorLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
        }
    }

    private void renderArmorLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn) {
        ItemStack stack = player.getItemStackFromSlot(slotIn);

        if (!stack.isEmpty() && stack.getItem() instanceof ItemFlowExosuit) {
            ItemFlowExosuit exosuit = (ItemFlowExosuit) stack.getItem();

            if (exosuit.getEquipmentSlot() == slotIn) {
                ModelBiped model = this.getModelFromSlot(slotIn);
                model = getArmorModelHook(player, stack, slotIn, model);
                model.setModelAttributes(this.renderer.getMainModel());
                model.setLivingAnimations(player, limbSwing, limbSwingAmount, partialTicks);
                this.setModelSlotVisible(model, slotIn);

                this.renderer.bindTexture(RESOURCE);
                int color = getColorFromPlayer(player);
                if (color == 0) return;
                float r = ((color >> 16) & 0xFF) / 255f;
                float g = ((color >> 8) & 0xFF) / 255f;
                float b = (color & 0xFF) / 255f;
                GlStateManager.disableLighting();
                GlStateManager.color(r, g, b, 1f);

                float lastBrightnessX = OpenGlHelper.lastBrightnessX;
                float lastBrightnessY = OpenGlHelper.lastBrightnessY;

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);
                model.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);

                GlStateManager.enableLighting();
                GlStateManager.color(1f, 1f, 1f, 1f);
            }
        }
    }
}
