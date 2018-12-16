package com.kamefrede.rpsideas.compat.botania;

import com.kamefrede.rpsideas.spells.base.SpellPieces;
import com.kamefrede.rpsideas.spells.trick.botania.PieceTrickBotaniaDrum;
import com.kamefrede.rpsideas.spells.trick.botania.PieceTrickFormBurst;
import com.kamefrede.rpsideas.util.libs.LibPieceNames;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibPieceGroups;

public class BotaniaCompatPieces {

    public static void init() {
        PsiAPI.setGroupRequirements(LibPieceNames.MANA_PSIONICS, 16, LibPieceGroups.GREATER_INFUSION, LibPieceGroups.ELEMENTAL_ARTS);

        SpellPieces.register(PieceTrickBotaniaDrum.DootGrass.class, LibPieceNames.WILD_DRUM, LibPieceNames.MANA_PSIONICS);
        SpellPieces.register(PieceTrickBotaniaDrum.DootLeaves.class, LibPieceNames.CANOPY_DRUM, LibPieceNames.MANA_PSIONICS);
        SpellPieces.register(PieceTrickBotaniaDrum.DootSnow.class, LibPieceNames.COVERING_HORN, LibPieceNames.MANA_PSIONICS);
        SpellPieces.register(PieceTrickBotaniaDrum.ShearDrum.class, LibPieceNames.GATHERING_DRUM, LibPieceNames.MANA_PSIONICS);

        SpellPieces.register(PieceTrickFormBurst.class, LibPieceNames.MAKE_BURST, LibPieceNames.MANA_PSIONICS, true);
    }

}
