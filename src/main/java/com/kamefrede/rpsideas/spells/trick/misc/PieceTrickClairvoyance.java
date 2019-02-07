package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.network.MessageClairvoyance;
import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickClairvoyance extends PieceTrick {

    private SpellParam time;
    private SpellParam ray;
    private SpellParam distance;

    public PieceTrickClairvoyance(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.GREEN, false, true));
        addParam(ray = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.YELLOW, false, false));
        addParam(distance = new ParamNumber(SpellParam.GENERIC_NAME_DISTANCE, SpellParam.BLUE, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        double time = SpellHelpers.ensurePositiveAndNonzero(this, this.time, 0);
        double distance = SpellHelpers.ensurePositiveAndNonzero(this, this.distance, 0);
        if (distance >= 32)
            throw new SpellCompilationException(SpellCompilationExceptions.RADIUS);
        meta.addStat(EnumSpellStat.COST, (int) (Math.pow(distance, 3) * ((time / 20) * 150)));
        meta.addStat(EnumSpellStat.POTENCY, (int) (Math.pow(distance, 2) * ((time / 20) * 15)));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        int time = (int) SpellHelpers.getNumber(this, context, this.time, 0);
        Vector3 ray = SpellHelpers.getVector3(this, context, this.ray);
        Vec3d trueRay = ray.multiply(SpellHelpers.getBoundedNumber(this, context, distance, SpellContext.MAX_DISTANCE)).toVec3D();

        PacketHandler.NETWORK.sendTo(new MessageClairvoyance(time, trueRay), (EntityPlayerMP) context.caster);

        return null;
    }
}
