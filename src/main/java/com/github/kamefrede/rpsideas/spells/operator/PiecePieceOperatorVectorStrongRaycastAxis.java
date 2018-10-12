package com.github.kamefrede.rpsideas.spells.operator;

import net.minecraft.util.math.RayTraceResult;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;

public class PiecePieceOperatorVectorStrongRaycastAxis extends PieceOperatorVectorStrongRaycast {

    public PiecePieceOperatorVectorStrongRaycastAxis(Spell spell){
        super(spell);
    }

    @Override
    protected Vector3 createResult(RayTraceResult res) {
        return new Vector3(res.sideHit.getXOffset(), res.sideHit.getYOffset(), res.sideHit.getZOffset());
    }
}
