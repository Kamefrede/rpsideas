package xyz.kamefrede.rpsideas.spells.trick.entity;

import xyz.kamefrede.rpsideas.entity.EntityClone;
import xyz.kamefrede.rpsideas.spells.base.SpellParams;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSummonClone extends PieceTrick {

    private SpellParam position;
    private SpellParam look;
    private SpellParam maxAlive;

    public PieceTrickSummonClone(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
        addParam(look = new ParamVector(SpellParams.GENERIC_NAME_DIRECTION, SpellParam.CYAN, true, false));
        addParam(maxAlive = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, true, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);

        double tim = SpellHelpers.evaluateNumber(this, maxAlive, 100);

        if (tim <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

        meta.addStat(EnumSpellStat.POTENCY, (int) (tim * 3 / 100));
        meta.addStat(EnumSpellStat.COST, (int) (tim * 5 / 100));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 pos = SpellHelpers.getVector3(this, context, position, true, true, false);
        Vec3d casLook = context.caster.getLookVec();
        Vector3 dir = SpellHelpers.getDefaultedVector(this, context, look, false, false, new Vector3(casLook.x, casLook.y, casLook.z));
        double maxTimeAlive = SpellHelpers.getNumber(this, context, maxAlive, 100);


        EntityClone clone = new EntityClone(context.caster.world);
        clone.setInfo(context.caster, pos, dir, (int) maxTimeAlive, new GameProfile(context.caster.getUniqueID(), context.caster.getName()));
        clone.setHealth(0.1f);
        context.caster.world.spawnEntity(clone);
        return null;
    }
}
