package xyz.kamefrede.rpsideas.render.elytra;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerCustomElytra extends LayerBipedArmor {
    protected final RenderLivingBase<?> renderPlayer;

    public LayerCustomElytra(RenderLivingBase<?> rendererIn) {
        super(rendererIn);
        this.renderPlayer = rendererIn;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack elytra = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (!elytra.isEmpty() && elytra.getItem() instanceof ICustomElytra) {
            renderArmor(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, elytra);

            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            renderPlayer.bindTexture(((ICustomElytra) elytra.getItem()).getElytraTexture(elytra));

            GlStateManager.pushMatrix();
            if (entity.isSneaking()) {
                GlStateManager.translate(0, 0.2F, 0);
                GlStateManager.rotate(90 / (float) Math.PI, 1, 0, 0);
            }
            GlStateManager.translate(0, 0.025f, 0.075f);

            ModelCustomElytra elytraModel = ((ICustomElytra) elytra.getItem()).getElytraModel(elytra);
            elytraModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            elytraModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (elytra.isItemEnchanted())
                LayerArmorBase.renderEnchantedGlint(this.renderPlayer, entity, elytraModel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    private void renderArmor(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, ItemStack elytra) {
        ModelBiped t = this.getModelFromSlot(EntityEquipmentSlot.CHEST);
        t = getArmorModelHook(entityLivingBaseIn, elytra, EntityEquipmentSlot.CHEST, t);
        t.setModelAttributes(renderPlayer.getMainModel());
        t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
        this.setModelSlotVisible(t, EntityEquipmentSlot.CHEST);
        renderPlayer.bindTexture(((ICustomElytra) elytra.getItem()).getElytraTexture(elytra));

        GlStateManager.color(1, 1, 1, 1);
        t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        if (!this.skipRenderGlint && elytra.hasEffect())
            renderEnchantedGlint(renderPlayer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
