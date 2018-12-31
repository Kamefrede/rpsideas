package com.kamefrede.rpsideas.compat.botania;

import com.kamefrede.rpsideas.spells.base.SpellPieces;
import com.kamefrede.rpsideas.spells.trick.botania.PieceTrickBotaniaDrum;
import com.kamefrede.rpsideas.spells.trick.botania.PieceTrickFormBurst;
import com.kamefrede.rpsideas.util.libs.RPSPieceNames;
import net.minecraftforge.fml.common.Optional;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibPieceGroups;

public class BotaniaCompatPieces extends NoopRunnable {

    @Override
    @Optional.Method(modid = "botania")
    public void run() {
        PsiAPI.setGroupRequirements(RPSPieceNames.MANA_PSIONICS, 16, LibPieceGroups.GREATER_INFUSION, LibPieceGroups.ELEMENTAL_ARTS);

        SpellPieces.register(PieceTrickBotaniaDrum.DootGrass.class, RPSPieceNames.WILD_DRUM, RPSPieceNames.MANA_PSIONICS);
        SpellPieces.register(PieceTrickBotaniaDrum.DootLeaves.class, RPSPieceNames.CANOPY_DRUM, RPSPieceNames.MANA_PSIONICS);
        SpellPieces.register(PieceTrickBotaniaDrum.DootSnow.class, RPSPieceNames.COVERING_HORN, RPSPieceNames.MANA_PSIONICS);
        SpellPieces.register(PieceTrickBotaniaDrum.ShearDrum.class, RPSPieceNames.GATHERING_DRUM, RPSPieceNames.MANA_PSIONICS);

        SpellPieces.register(PieceTrickFormBurst.class, RPSPieceNames.MAKE_BURST, RPSPieceNames.MANA_PSIONICS, true);
    }

}
