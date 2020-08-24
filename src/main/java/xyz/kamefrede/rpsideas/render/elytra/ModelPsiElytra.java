package xyz.kamefrede.rpsideas.render.elytra;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author WireSegal
 * Created at 10:25 AM on 4/3/19.
 */
@SideOnly(Side.CLIENT)
public class ModelPsiElytra extends ModelCustomElytra {
    private void createWingSection(List<WingElement> wingSections,
                                   int textureX, int textureY,
                                   int glowTextureX, int glowTextureY,
                                   Vec3d centroid, Vec3d rotation, double maxRotation,
                                   int direction, int size, boolean loadBearing) {
        int offset = direction > 0 ? 0 : -size;
        float secondOffset = direction > 0 ? 3.5f : -(size * 2) - 1.5f;

        ModelRenderer wing = new ModelRenderer(this, textureX, textureY);
        wing.mirror = direction == RIGHT;
        wing.addBox(offset, 0, 0.5f, size, 3, 1, 0);
        createWing(wingSections, wing, loadBearing,
                new Vec3d(1, 0.125, -0.25 * direction),
                centroid,
                (float) maxRotation, rotation);

        ModelRenderer glow = new GlowingModelRenderer(this, glowTextureX, glowTextureY);
        glow.mirror = direction == LEFT;
        glow.setRotationPoint(-2.5f, 0, 0);
        glow.addBox(secondOffset, 1, 1f, size * 2 + 3, 3, 0, 0);
        wing.addChild(glow);
    }

    @Override
    protected void createLeftWing(List<WingElement> wingSections) {
        createWingSection(wingSections, 0, 30, 0, 42,
                new Vec3d(0, 0, 1),
                new Vec3d(0, Math.PI / 12, -Math.PI * 12 / 32), Math.PI * 3 / 32,
                LEFT, 5, false);
        createWingSection(wingSections, 0, 34, 0, 45,
                new Vec3d(0, 0, 1.5),
                new Vec3d(0, Math.PI / 12, -Math.PI * 11 / 32), Math.PI * 11 / 32,
                LEFT, 7, false);
        createWingSection(wingSections, 0, 38, 0, 48,
                new Vec3d(0, 0, 2),
                new Vec3d(0, Math.PI / 12, -Math.PI * 9 / 32), Math.PI * 17 / 32,
                LEFT, 9, true);
    }

    @Override
    protected void createRightWing(List<WingElement> wingSections) {
        createWingSection(wingSections, 0, 30, 0, 42,
                new Vec3d(0, 0, 1),
                new Vec3d(0, -Math.PI / 12, Math.PI * 12 / 32), Math.PI * 3 / 32,
                RIGHT, 5, false);
        createWingSection(wingSections, 0, 34, 0, 45,
                new Vec3d(0, 0, 1.5),
                new Vec3d(0, -Math.PI / 12, Math.PI * 11 / 32), Math.PI * 11 / 32,
                RIGHT, 7, false);
        createWingSection(wingSections, 0, 38, 0, 48,
                new Vec3d(0, 0, 2),
                new Vec3d(0, -Math.PI / 12, Math.PI * 9 / 32), Math.PI * 17 / 32,
                RIGHT, 9, true);
    }

    @Override
    protected void setupTexture() {
        textureHeight = 128;
        textureWidth = 64;
    }
}
