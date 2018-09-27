package com.github.kamefrede.rpsideas.compat.botania;

import com.github.kamefrede.rpsideas.spells.base.SpellPieces;
import com.github.kamefrede.rpsideas.spells.trick.botania.PieceTrickBotaniaDrum;
import com.github.kamefrede.rpsideas.spells.trick.botania.PieceTrickFormBurst;
import com.github.kamefrede.rpsideas.util.libs.LibPieces;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibPieceGroups;

public class BotaniaCompatPieces {
    public static void init(){
        PsiAPI.setGroupRequirements(LibPieces.MANA_PSIONICS, 16, LibPieceGroups.GREATER_INFUSION, LibPieceGroups.ELEMENTAL_ARTS);

        SpellPieces.register(PieceTrickBotaniaDrum.DootGrass.class, LibPieces.WILD_DRUM, LibPieces.MANA_PSIONICS);
        SpellPieces.register(PieceTrickBotaniaDrum.DootLeaves.class, LibPieces.CANOPY_DRUM, LibPieces.MANA_PSIONICS);
        SpellPieces.register(PieceTrickBotaniaDrum.DootSnow.class, LibPieces.COVERING_HORN, LibPieces.MANA_PSIONICS);
        SpellPieces.register(PieceTrickBotaniaDrum.ShearDrum.class, LibPieces.GATHERING_DRUM, LibPieces.MANA_PSIONICS);

        SpellPieces.register(PieceTrickFormBurst.class, LibPieces.MAKE_BURST, LibPieces.MANA_PSIONICS, true);
    }

}
