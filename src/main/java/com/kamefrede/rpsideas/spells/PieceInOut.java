package com.kamefrede.rpsideas.spells;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.common.lib.LibResources;

/**
 * @author WireSegal
 * Created at 2:21 PM on 2/7/19.
 */
public class PieceInOut extends SpellPiece implements IGenericRedirector {
    private static final ResourceLocation LINES = new ResourceLocation(LibResources.SPELL_CONNECTOR_LINES);

    private SpellParam in;
    private SpellParam out;
    private SpellParam tar;

    public PieceInOut(Spell spell) {
        super(spell);
    }

    private static final int LINE_ONE = 0xA0A0A0;
    private static final int LINE_TWO = 0xA040FF;

    @Override
    public void initParams() {
        addParam(in = new ParamAny(SpellParams.CONNECTOR_NAME_FROM1, LINE_ONE, false));
        addParam(out = new ParamAny(SpellParams.CONNECTOR_NAME_TO1, LINE_ONE, false));
        addParam(tar = new ParamAny(SpellParams.CONNECTOR_NAME_FROM2, LINE_TWO, false));
    }

    @Override
    public String getSortingName() {
        return "00000000000";
    }

    @Override
    public String getEvaluationTypeString() {
        return TooltipHelper.local("psi.datatype.Any");
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawAdditional() {
        drawSide(paramSides.get(in), LINE_ONE);
        drawSide(paramSides.get(out), LINE_ONE);

        drawSide(paramSides.get(tar), LINE_TWO);
    }

    @SideOnly(Side.CLIENT)
    public void drawSide(SpellParam.Side side, int color) {
        if(side.isEnabled()) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.renderEngine.bindTexture(LINES);

            double minU = 0;
            double minV = 0;
            switch(side) {
                case LEFT:
                    minU = 0.5;
                    break;
                case RIGHT: break;
                case TOP:
                    minV = 0.5;
                    break;
                case BOTTOM:
                    minU = 0.5;
                    minV = 0.5;
                    break;
                default: break;
            }

            double maxU = minU + 0.5;
            double maxV = minV + 0.5;

            float r = SpellHelpers.getR(color);
            float g = SpellHelpers.getG(color);
            float b = SpellHelpers.getB(color);

            GlStateManager.color(r, g, b);
            BufferBuilder wr = Tessellator.getInstance().getBuffer();
            wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            wr.pos(0, 16, 0).tex(minU, maxV).endVertex();
            wr.pos(16, 16, 0).tex(maxU, maxV).endVertex();
            wr.pos(16, 0, 0).tex(maxU, minV).endVertex();
            wr.pos(0, 0, 0).tex(minU, minV).endVertex();
            Tessellator.getInstance().draw();
        }
    }

    @Override
    public void drawParams() {
        Minecraft.getMinecraft().renderEngine.bindTexture(PsiAPI.internalHandler.getProgrammerTexture());
        GlStateManager.enableAlpha();
        drawParam(in);
        drawParam(tar);
    }

    @SideOnly(Side.CLIENT)
    public void drawParam(SpellParam param) {
        SpellParam.Side side = paramSides.get(param);
        if (side.isEnabled()) {
            int minX = 4;
            int minY = 4;
            minX += side.offx * 9;
            minY += side.offy * 9;

            int maxX = minX + 8;
            int maxY = minY + 8;

            float wh = 8F;
            float minU = side.u / 256F;
            float minV = side.v / 256F;
            float maxU = (side.u + wh) / 256F;
            float maxV = (side.v + wh) / 256F;
            GlStateManager.color(PsiRenderHelper.r(param.color) / 255F,
                    PsiRenderHelper.g(param.color) / 255F,
                    PsiRenderHelper.b(param.color) / 255F, 1F);

            BufferBuilder wr = Tessellator.getInstance().getBuffer();
            wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            wr.pos(minX, maxY, 0).tex(minU, maxV).endVertex();
            wr.pos(maxX, maxY, 0).tex(maxU, maxV).endVertex();
            wr.pos(maxX, minY, 0).tex(maxU, minV).endVertex();
            wr.pos(minX, minY, 0).tex(minU, minV).endVertex();
            Tessellator.getInstance().draw();
        }
    }

    @Override
    public EnumPieceType getPieceType() {
        return EnumPieceType.CONNECTOR;
    }

    @Override
    public SpellParam.Side remapSide(SpellParam.Side inputSide) {
        if (paramSides.get(out).getOpposite() == inputSide)
            return paramSides.get(tar);
        else if (paramSides.get(tar).getOpposite() == inputSide)
            return paramSides.get(in);
        else
            return SpellParam.Side.OFF;
    }

    // Since this class implements IGenericRedirector we don't need this
    @Override
    public Class<?> getEvaluationType() {
        return null;
    }

    @Override
    public Object evaluate() {
        return null;
    }

    @Override
    public Object execute(SpellContext context) {
        return null;
    }


}
