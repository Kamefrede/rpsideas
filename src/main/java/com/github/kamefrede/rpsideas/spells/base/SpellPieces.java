package com.github.kamefrede.rpsideas.spells.base;


import com.github.kamefrede.rpsideas.spells.selector.PieceSelectorNearbyPlayers;
import com.github.kamefrede.rpsideas.spells.trick.PieceTrickConjureEtherealBlock;
import com.github.kamefrede.rpsideas.spells.trick.PieceTrickConjureEtherealBlockSequence;
import com.github.kamefrede.rpsideas.spells.trick.PieceTrickDirectionPlaceBlock;
import com.github.kamefrede.rpsideas.util.LibPieces;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;

public class SpellPieces  {

    public static void init(){
        register(PieceTrickConjureEtherealBlock.class, LibPieces.TRICK_CONJURE_ETHEREAL_BLOCK, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickConjureEtherealBlockSequence.class, LibPieces.TRICK_CONJURE_ETHEREAL_BLOCK_SEQUENCE, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceSelectorNearbyPlayers.class, LibPieces.SELECTOR_NEARBY_PLAYERS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceTrickDirectionPlaceBlock.class, LibPieces.TRICK_DIRECTION_PLACE_BLOCK, LibPieceGroups.BLOCK_WORKS);
    }

    static void register(Class<? extends SpellPiece> pieceClass, String name, String group) {
        register(pieceClass, name, group, false);
    }

    static void register(Class<? extends SpellPiece> pieceClass, String name, String group, boolean main) {
        String key = Reference.MODID + "." + name;
        PsiAPI.registerSpellPiece(key, pieceClass);
        PsiAPI.simpleSpellTextures.put(key, new ResourceLocation(Reference.MODID, "textures/spell/" + name + ".png"));
        PsiAPI.addPieceToGroup(pieceClass, group, main);
    }
}
