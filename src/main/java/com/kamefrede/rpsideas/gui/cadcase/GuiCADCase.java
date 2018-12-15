package com.kamefrede.rpsideas.gui.cadcase;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCADCase extends GuiContainer {
    private static final ResourceLocation textureMain = new ResourceLocation(RPSIdeas.MODID, "textures/gui/case_base.png");
    private static final ResourceLocation textureCase = new ResourceLocation(RPSIdeas.MODID, "textures/gui/cases.png");
    private static final int xOffset = 72;
    private static final int yOffset = 5 + 29;

    private final ItemStack stack;

    public GuiCADCase(EntityPlayer player, ItemStack stack) {
        super(new ContainerCADCase(player, stack));

        this.stack = stack;
    }


    @Override
    public void initGui() {
        xSize = 227;
        ySize = 130;
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);

        mc.getTextureManager().bindTexture(textureMain);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y + yOffset, 0, 0, xSize, 96);

        //TODO flatten.
        mc.getTextureManager().bindTexture(textureCase);
        int u = (stack.getItemDamage() % 3) * 83;
        int v = (stack.getItemDamage() / 3) * 29;
        drawTexturedModalRect(x + xOffset, y, u, v, 83, 29);
    }
}
