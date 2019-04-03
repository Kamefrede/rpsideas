package com.kamefrede.rpsideas.render.elytra;

import net.minecraft.client.model.ModelRenderer;
import vazkii.arl.item.ModelModArmor;

/**
 * @author WireSegal
 * Created at 1:13 PM on 4/3/19.
 */
public class ModelElytraChestpiece extends ModelModArmor {

    public ModelRenderer armR;
    public ModelRenderer armL;
    public ModelRenderer body;
    public ModelRenderer armRPauldron;
    public ModelRenderer armLPauldron;

    public ModelElytraChestpiece() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0, 0, 0);
        this.body.addBox(-4.5f, -0.5f, -3, 9, 5, 6, 0);
        this.armR = new ModelRenderer(this, 0, 21);
        this.armR.setRotationPoint(-5, 2, 0);
        this.armR.addBox(-3.5f, 6, -2.51f, 3, 2, 5, 0);
        this.armRPauldron = new ModelRenderer(this, 0, 11);
        this.armRPauldron.setRotationPoint(0, 0, 0);
        this.armRPauldron.addBox(-3.5f, -2.5f, -2.5f, 3, 5, 5, 0);
        this.setRotateAngle(armRPauldron, 0, 0, (float) Math.PI / 18);
        this.armL = new ModelRenderer(this, 0, 21);
        this.armL.mirror = true;
        this.armL.setRotationPoint(5, 2, -0);
        this.armL.addBox(0.5f, 6, -2.5f, 3, 2, 5, 0);
        this.armLPauldron = new ModelRenderer(this, 0, 11);
        this.armLPauldron.mirror = true;
        this.armLPauldron.setRotationPoint(0, 0, 0);
        this.armLPauldron.addBox(0.5f, -2.5f, -2.5f, 3, 5, 5, 0);
        this.setRotateAngle(armLPauldron, 0, 0, (float) -Math.PI / 18);

        this.armR.addChild(this.armRPauldron);
        this.armL.addChild(this.armLPauldron);
    }

    @Override
    public void setModelParts() {
        bipedBody = body;
        bipedRightArm = armR;
        bipedLeftArm = armL;
    }
}
