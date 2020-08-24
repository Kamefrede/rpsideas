package xyz.kamefrede.rpsideas.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 18 11 AM on 12/21/18.
 */
@SideOnly(Side.CLIENT)
public class LayerAuthorOccludeElytra implements LayerRenderer<AbstractClientPlayer> {
    private final LayerElytra parent;

    public LayerAuthorOccludeElytra(LayerElytra parent) {
        this.parent = parent;
    }

    @Override
    public void doRenderLayer(@Nonnull AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!LayerAuthorCape.isAuthor(player) || !player.isWearing(EnumPlayerModelParts.CAPE) || player.getLocationCape() == null)
            parent.doRenderLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

}
