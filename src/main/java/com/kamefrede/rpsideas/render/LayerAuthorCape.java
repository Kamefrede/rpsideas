package com.kamefrede.rpsideas.render;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.RPSItems;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.common.Psi;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.UUID;

/**
 * @author WireSegal
 * Created at 18 11 AM on 12/21/18.
 */
@SideOnly(Side.CLIENT)
public class LayerAuthorCape implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer renderPlayer;

    public static boolean isAuthor(EntityPlayer player) {
        return player.getUniqueID().equals(WIRE_UUID) || player.getUniqueID().equals(KAMEFREDE_UUID);
    }

    public LayerAuthorCape(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    private final ModelElytra modelElytra = new ModelElytra();

    private ResourceLocation getTexture(AbstractClientPlayer player) {
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if (stack.isEmpty() || stack.getItem() == RPSItems.ebonyLegs)
            return TEXTURE_EBONY;
        return TEXTURE_IVORY;
    }

    @Override
    public void doRenderLayer(@Nonnull AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!isAuthor(player))
            return;
        
        ItemStack cad = PsiAPI.getPlayerCAD(player);
        
        boolean hasCad = !cad.isEmpty();
        float r = 0, g = 0, b = 0;
        if (hasCad) {
            Color color = Psi.proxy.getCADColor(cad);
            r = color.getRed() / 255f;
            g = color.getGreen() / 255f;
            b = color.getBlue() / 255f;
        }
        
        doCapeRender(player, partialTicks, r, g, b, hasCad);
        doElytraRender(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, r, g, b, hasCad);
        doSkinOverlayRender(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, r, g, b, hasCad);
        
    }

    private void doCapeRender(AbstractClientPlayer player, float partialTicks, float r, float g, float b, boolean hasCad) {
        if (player.hasPlayerInfo() && !player.isInvisible() && player.isWearing(EnumPlayerModelParts.CAPE) && player.getLocationCape() != null) {
            ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (stack.isEmpty() || stack.getItem() == Items.ELYTRA) {
                GlStateManager.color(1, 1, 1, 1);
                this.renderPlayer.bindTexture(getTexture(player));
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0f, 0.0f, 0.125f);
                double xShift = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
                double yShift = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * partialTicks - (player.prevPosY + (player.posY - player.prevPosY) * partialTicks);
                double zShift = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
                float yawShift = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
                double xLook = MathHelper.sin(yawShift * (float) Math.PI / 180);
                double yLook = -MathHelper.cos(yawShift * (float) Math.PI / 180);
                float floatAmount = MathHelper.clamp((float) yShift * 10.0f, -6.0f, 32.0f);
                float leftTurn = (float) (xShift * xLook + zShift * yLook) * 100.0f;
                float rightTurn = (float) (xShift * yLook - zShift * xLook) * 100.0f;

                if (leftTurn < 0.0f) leftTurn = 0.0f;

                float cameraYawShift = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
                floatAmount += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * cameraYawShift;

                if (player.isSneaking()) {
                    floatAmount += 25.0f;
                }

                GlStateManager.rotate(6.0f + leftTurn / 2.0f + floatAmount, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(rightTurn / 2.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-rightTurn / 2.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                this.renderPlayer.getMainModel().renderCape(0.0625f);

                if (hasCad) {
                    this.renderPlayer.bindTexture(TEXTURE_OVERLAY);
                    GlStateManager.disableLighting();

                    float lastBrightnessX = OpenGlHelper.lastBrightnessX;
                    float lastBrightnessY = OpenGlHelper.lastBrightnessY;

                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);
                    GlStateManager.color(r, g, b);
                    this.renderPlayer.getMainModel().renderCape(0.0625f);
                    GlStateManager.color(1f, 1f, 1f);
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);

                    GlStateManager.enableLighting();
                }

                GlStateManager.popMatrix();
            }
        }
    }

    private void doElytraRender(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, float r, float g, float b, boolean hasCad) {
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (player.isWearing(EnumPlayerModelParts.CAPE) && player.getLocationCape() != null && !stack.isEmpty() && stack.getItem() == Items.ELYTRA) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            this.renderPlayer.bindTexture(getTexture(player));

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.125f);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, player);
            this.modelElytra.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (stack.isItemEnchanted())
                LayerArmorBase.renderEnchantedGlint(this.renderPlayer, player, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);

            if (hasCad) {
                this.renderPlayer.bindTexture(TEXTURE_OVERLAY);
                GlStateManager.disableLighting();
                float lastBrightnessX = OpenGlHelper.lastBrightnessX;
                float lastBrightnessY = OpenGlHelper.lastBrightnessY;

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);
                GlStateManager.color(r, g, b);
                this.modelElytra.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.color(1f, 1f, 1f);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
                GlStateManager.enableLighting();
            }

            GlStateManager.disableBlend();

            GlStateManager.popMatrix();
        }
    }

    private void doSkinOverlayRender(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, float r, float g, float b, boolean hasCad) {
        if (hasCad && !player.isInvisibleToPlayer(player)) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            ShaderHandler.useShader(ShaderHandler.rawColor);
            GlStateManager.color(r, g, b);
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);

            if (player.getUniqueID().equals(WIRE_UUID))
                this.renderPlayer.bindTexture(TEXTURE_SKIN_OVERLAY_WIRE);
            else
                this.renderPlayer.bindTexture(TEXTURE_SKIN_OVERLAY_KAMEFREDE);

            renderPlayer.setModelVisibilities(player);

            renderPlayer.getMainModel().render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            GlStateManager.color(1F, 1F, 1F);
            ShaderHandler.releaseShader();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private static final UUID WIRE_UUID = UUID.fromString("458391f5-6303-4649-b416-e4c0d18f837a");
    private static final UUID KAMEFREDE_UUID = UUID.fromString("ed21cb5c-f66a-4e26-abf0-f209f7169751");
    private static final ResourceLocation TEXTURE_EBONY = new ResourceLocation(RPSIdeas.MODID, "textures/model/wire/ebony_cape2016.png");
    private static final ResourceLocation TEXTURE_IVORY = new ResourceLocation(RPSIdeas.MODID, "textures/model/wire/ivory_cape2016.png");
    private static final ResourceLocation TEXTURE_OVERLAY = new ResourceLocation(RPSIdeas.MODID, "textures/model/wire/cape2016_overlay.png");
    private static final ResourceLocation TEXTURE_SKIN_OVERLAY_WIRE = new ResourceLocation(RPSIdeas.MODID, "textures/model/wire/skin_overlay.png");
    private static final ResourceLocation TEXTURE_SKIN_OVERLAY_KAMEFREDE = new ResourceLocation(RPSIdeas.MODID, "textures/model/wire/kamefrede_overlay.png");

}
