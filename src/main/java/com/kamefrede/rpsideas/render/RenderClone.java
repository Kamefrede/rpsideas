package com.kamefrede.rpsideas.render;

import com.kamefrede.rpsideas.entity.EntityClone;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderClone extends RenderBiped<EntityClone> {
    public RenderClone(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelPlayer(0.0F, false), 0F);
    }

    @Nonnull
    @Override
    protected ResourceLocation getEntityTexture(EntityClone entity) {
        return entity.getSkinResourceLocation();
    }
}
