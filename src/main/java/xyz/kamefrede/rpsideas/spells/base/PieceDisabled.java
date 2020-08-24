package xyz.kamefrede.rpsideas.spells.base;

import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import vazkii.psi.api.spell.*;

public class PieceDisabled extends SpellPiece {

    public PieceDisabled(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        throw new SpellCompilationException(SpellCompilationExceptions.DISABLED, x, y);
    }

    @Override
    public EnumPieceType getPieceType() {
        return EnumPieceType.MODIFIER;
    }

    @Override
    public Class<?> getEvaluationType() {
        return null;
    }

    @Override
    public Object evaluate() throws SpellCompilationException {
        throw new SpellCompilationException(SpellCompilationExceptions.DISABLED, x, y);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        throw new SpellRuntimeException(SpellCompilationExceptions.DISABLED);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawAdditional() {
        int color = 0xFF0000;
        float r = SpellHelpers.getR(color);
        float g = SpellHelpers.getG(color);
        float b = SpellHelpers.getB(color);

        GlStateManager.color(r, g, b);
        BufferBuilder wr = Tessellator.getInstance().getBuffer();
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        GlStateManager.glLineWidth(3.2f);
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        wr.pos(0, 0, 0).endVertex();
        wr.pos(16, 16, 0).endVertex();

        wr.pos(16, 0, 0).endVertex();
        wr.pos(0, 16, 0).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
    }

}
