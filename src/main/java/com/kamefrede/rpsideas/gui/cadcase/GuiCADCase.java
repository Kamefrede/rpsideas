package com.kamefrede.rpsideas.gui.cadcase;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.blocks.BlockCADCase;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCADCase extends GuiContainer {
    private static final ResourceLocation textureMain = new ResourceLocation(RPSIdeas.MODID, "textures/gui/case_base.png");
    private static final ResourceLocation textureCase = new ResourceLocation(RPSIdeas.MODID, "textures/gui/cases.png");
    private static final int xOffset = 72;
    private static final int yOffset = 5 + 29;

    private final EnumDyeColor color;

    public GuiCADCase(EntityPlayer player, ItemStack stack) {
        super(new ContainerCADCase(player, stack));

        Block block = Block.getBlockFromItem(stack.getItem());
        if (block instanceof BlockCADCase)
            color = ((BlockCADCase) block).color;
        else
            color = EnumDyeColor.WHITE;
    }

    @Override
    public void initGui() {
        xSize = 227;
        ySize = 130;
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);

        mc.getTextureManager().bindTexture(textureMain);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y + yOffset, 0, 0, xSize, 96);

        mc.getTextureManager().bindTexture(textureCase);
        int u = (color.getMetadata() % 3) * 83;
        int v = (color.getMetadata() / 3) * 29;
        drawTexturedModalRect(x + xOffset, y, u, v, 83, 29);
    }
}
