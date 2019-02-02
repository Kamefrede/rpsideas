package com.kamefrede.rpsideas.spells.serious;

import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.RPSSoundHandler;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

public class PieceTrickSansUndertale extends PieceTrick {

    private static final String TAG_SANS_UNDERTALE = "rpsideas:SansUndertale";

    private SpellParam volume;

    public PieceTrickSansUndertale(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(volume = new ParamNumber(SpellParams.GENERIC_NAME_VOLUME, SpellParam.YELLOW, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        double dVol = SpellHelpers.ensurePositiveOrZero(this, volume);

        if (dVol < 0 || dVol > 1)
            throw new SpellCompilationException(SpellCompilationExceptions.VOLUME, x, y);
        meta.addStat(EnumSpellStat.POTENCY, 5);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {

        double volVal = SpellHelpers.getNumber(this, context, volume, 0);
        BlockPos pos = context.caster.getPosition();

        context.caster.world.playSound(null, pos, RPSSoundHandler.MEGALOVANIA, SoundCategory.RECORDS, (float) volVal, 1f);
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
        if (data.getCustomData() != null) {
            data.getCustomData().setInteger(TAG_SANS_UNDERTALE, 680);
            data.save();
            if (context.caster instanceof EntityPlayerMP)
                NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) context.caster);
        }

        return null;
    }
}
