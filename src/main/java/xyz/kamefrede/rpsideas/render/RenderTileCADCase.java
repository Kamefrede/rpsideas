package xyz.kamefrede.rpsideas.render;

import xyz.kamefrede.rpsideas.blocks.BlockCADCase;
import xyz.kamefrede.rpsideas.tiles.TileCADCase;
import com.teamwizardry.librarianlib.features.tesr.TileRenderHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderTileCADCase extends TileRenderHandler<TileCADCase> {

    public RenderTileCADCase(@NotNull TileCADCase tile) {
        super(tile);
    }

    @Override
    public void render(float partialTicks, int destroyStage, float alpha) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (!(state.getBlock() instanceof BlockCADCase) || !state.getValue(BlockCADCase.OPEN)) return;

        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (itemHandler == null) return;

        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();

        EnumFacing facing = state.getValue(BlockCADCase.FACING);
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.rotate(-facing.getHorizontalAngle(), 0f, 1f, 0f);
        GlStateManager.translate(-0.5, -0.5, -0.5);

        GlStateManager.pushMatrix();
        GlStateManager.rotate(-90f, 1f, 0f, 0f);
        GlStateManager.scale(0.35d, 0.35d, 0.35d);
        GlStateManager.translate(2.0f, -1.45f, 0.3f);
        renderItem.renderItem(itemHandler.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED);
        GlStateManager.popMatrix();

        GlStateManager.rotate(-90f, 1f, 0f, 0f);
        GlStateManager.scale(0.35d, 0.35d, 0.35d);
        GlStateManager.translate(0.75f, -1.45f, 0.3f);
        renderItem.renderItem(itemHandler.getStackInSlot(1), ItemCameraTransforms.TransformType.FIXED);

        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.disableBlend();
        GlStateManager.enableRescaleNormal();
        GlStateManager.popMatrix();
    }
}
