package com.kamefrede.rpsideas.render.elytra;

import com.google.common.collect.Lists;
import com.teamwizardry.librarianlib.features.animator.Easing;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.WeakHashMap;

@SideOnly(Side.CLIENT)
public abstract class ModelCustomElytra extends ModelBase {

    protected static final float PI = (float) Math.PI;

    protected static final int LEFT = 1;
    protected static final int RIGHT = -1;

    public static class WingElement {
        public final ModelRenderer wingSection;
        public final boolean loadBearing;
        public final Vec3d rotationAxis; // x - wingspan, y - tilt, z - curl
        public final Vec3d rotationCentroid;
        public final float maxRotation; // [0,1]
        public final Vec3d baseRotation;

        public WingElement(ModelRenderer wingSection, boolean loadBearing, Vec3d rotationAxis, Vec3d rotationCentroid, float maxRotation, Vec3d baseRotation) {
            this.wingSection = wingSection;
            this.loadBearing = loadBearing;
            this.rotationAxis = rotationAxis.normalize();
            this.rotationCentroid = rotationCentroid;
            this.maxRotation = maxRotation;
            this.baseRotation = baseRotation;
        }
    }

    private final WeakHashMap<Entity, float[]> angles = new WeakHashMap<>();

    private final List<WingElement> left = Lists.newArrayList();
    private final List<WingElement> right = Lists.newArrayList();

    public ModelCustomElytra() {
        setupTexture();
        createLeftWing(left);
        createRightWing(right);
    }

    protected void createWing(List<WingElement> wingSections, ModelRenderer box, boolean loadBearing, Vec3d rotationAxis, Vec3d rotationCentroid, float maxRotation, Vec3d baseRotation) {
        wingSections.add(new WingElement(box, loadBearing, rotationAxis, rotationCentroid, maxRotation, baseRotation));
    }

    protected void setupTexture() {
        // NO-OP
    }

    protected abstract void createLeftWing(List<WingElement> wingSections);

    protected abstract void createRightWing(List<WingElement> wingSections);

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        for (WingElement el : left)
            el.wingSection.render(scale);
        for (WingElement el : right)
            el.wingSection.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        // [0,1]
        float wingspan = 0;

        if (entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).isElytraFlying()) {
            wingspan += 1;

            Vec3d motionDir = new Vec3d(entityIn.motionX, entityIn.motionY, entityIn.motionZ).normalize();

            if (entityIn.motionY < 0)
                wingspan -= Math.pow(-motionDir.y, 1.5);
        } else if (entityIn.isSneaking())
            wingspan += 0.25;

        applyRotation(entityIn, left, wingspan, LEFT);
        applyRotation(entityIn, right, wingspan, RIGHT);
    }

    private float getAngle(float[] data, int index, int direction) {
        return data[direction < 0 ? index : left.size() + index];
    }

    private void setAngle(float[] data, int index, int direction, float value) {
        data[direction < 0 ? index : left.size() + index] = value;
    }

    private void applyRotation(Entity entity, List<WingElement> elements, float wingspan, int direction) {
        float[] angleData = angles.computeIfAbsent(entity, e -> new float[left.size() + right.size()]);

        float maxLoad = 0;
        for (WingElement el : elements) if (el.loadBearing && el.maxRotation > maxLoad)
            maxLoad = el.maxRotation;

        for (int i = 0; i < elements.size(); i++) {
            WingElement el = elements.get(i);

            float target = 0;

            if (el.loadBearing || entity.isSneaking())
                target = wingspan;
            else {
                float offset = (maxLoad - el.maxRotation) / PI;
                if (wingspan > offset)
                    target = Easing.easeInSine.invoke((wingspan - offset) / (1 - offset));
            }

            float currentAngle = getAngle(angleData, i, direction);
            float targetAngle = Math.min(1, target) * el.maxRotation;
            float newAngle = currentAngle + (targetAngle - currentAngle) * 0.1f;
            setAngle(angleData, i, direction, newAngle);

            el.wingSection.rotationPointX = (float) el.rotationCentroid.x;
            el.wingSection.rotationPointY = (float) el.rotationCentroid.y;
            el.wingSection.rotationPointZ = (float) el.rotationCentroid.z;

            double ux = direction * el.rotationAxis.x;
            double uy = direction * el.rotationAxis.y;
            double uz = direction * el.rotationAxis.z;
            double cos = MathHelper.cos(newAngle / 2);
            double sin = MathHelper.sin(newAngle / 2);
            double r = cos * sin;
            double n = sin * sin;

            el.wingSection.rotateAngleX = (float) (el.baseRotation.x +
                    MathHelper.atan2(2 * (uz * r + ux * uy * n), 1 - 2 * n * (uz * uz + uy * uy)));
            el.wingSection.rotateAngleY = (float) (el.baseRotation.y +
                    Math.asin(2 * (uy * r - ux * uz * n))) + PI;
            el.wingSection.rotateAngleZ = (float) (el.baseRotation.z +
                    MathHelper.atan2(2 * (ux * r + uy * uz * n), 1 - 2 * n * (ux * ux + uy * uy)));
        }
    }
}
