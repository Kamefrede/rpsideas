package com.kamefrede.rpsideas.gui.magazine;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import vazkii.arl.util.ClientTicker;

import java.util.ArrayList;

public class GUICADMagazine extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation(RPSIdeas.MODID, "textures/gui/magazine.png");
    int lastTick = 0;

    public GUICADMagazine(EntityPlayer player, ItemStack stack) {
        super(new ContainerCADMagazine(player, stack));
    }

    private String getTooltipText() {
        return ((ContainerCADMagazine) inventorySlots).tooltipText;
    }

    private float getTooltipTime() {
        return ((ContainerCADMagazine) inventorySlots).tooltipTime;
    }

    private void decreaseTooltipTime(float value) {
        ((ContainerCADMagazine) inventorySlots).tooltipTime = getTooltipTime() - value;
    }

    @Override
    public void initGui() {
        xSize = 194;
        ySize = 192;
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        for (Slot slots : inventorySlots.inventorySlots) {
            if (!slots.isEnabled()) {
                boolean dark = ((ContainerCADMagazine.SlotCustomBullet) slots).dark;
                drawTexturedModalRect(slots.xPos + x, slots.yPos + y, 16, 224 + (dark ? 16 : 0), 16, 16);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (getTooltipTime() != 0) {
            ArrayList<String> list = new ArrayList<>();
            list.add(TextFormatting.RED.toString() + I18n.format(getTooltipText()));
            GuiUtils.drawHoveringText(list, -10, ySize / 2, width, height, xSize, Minecraft.getMinecraft().fontRenderer);
            decreaseTooltipTime(ClientTicker.ticksInGame - lastTick);
        }
        lastTick = ClientTicker.ticksInGame;
    }
}
