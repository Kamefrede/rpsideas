package com.github.kamefrede.psiaddon.spells.base;


import com.github.kamefrede.psiaddon.spells.trick.PieceTrickConjureCobble;
import com.github.kamefrede.psiaddon.spells.trick.PieceTrickDirectionPlaceBlock;
import com.github.kamefrede.psiaddon.spells.trick.PieceTrickEidosSuspend;
import com.github.kamefrede.psiaddon.util.LibPieces;
import com.github.kamefrede.psiaddon.util.Reference;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;

public class SpellPieces  {

    public static void init(){
        register(PieceTrickConjureCobble.class, LibPieces.TRICK_CONJURE_COBBLE, LibPieceGroups.BLOCK_WORKS, true);
        register(PieceTrickDirectionPlaceBlock.class, LibPieces.TRICK_DIRECTION_PLACE_BLOCK, LibPieceGroups.BLOCK_CONJURATION, true);
        register(PieceTrickEidosSuspend.class, LibPieces.TRICK_EIDOS_SUSPEND, LibPieceGroups.EIDOS_REVERSAL, true);
    }


    static void register(Class<? extends  SpellPiece> piececlass, String name, String group){
        register(piececlass, name, group, false);
    }

    static void register(Class<? extends SpellPiece> piececlass, String name, String group, boolean main){
        PsiAPI.registerSpellPieceAndTexture(Reference.MODID + "." +name, piececlass);
        PsiAPI.addPieceToGroup(piececlass, group, main);
    }

}
