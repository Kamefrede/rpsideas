package com.github.kamefrede.rpsideas.spells.base;


import com.github.kamefrede.rpsideas.spells.operator.*;
import com.github.kamefrede.rpsideas.spells.selector.PieceSelectorNearbyPlayers;
import com.github.kamefrede.rpsideas.spells.trick.*;
import com.github.kamefrede.rpsideas.util.LibPieces;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.spell.trick.block.PieceTrickConjureLight;

public class SpellPieces  {

    public static void init(){
        register(PieceTrickConjureEtherealBlock.class, LibPieces.TRICK_CONJURE_ETHEREAL_BLOCK, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickConjureEtherealBlockSequence.class, LibPieces.TRICK_CONJURE_ETHEREAL_BLOCK_SEQUENCE, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceSelectorNearbyPlayers.class, LibPieces.SELECTOR_NEARBY_PLAYERS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceTrickDirectionPlaceBlock.class, LibPieces.TRICK_DIRECTION_PLACE_BLOCK, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorExtractSign.class, LibPieces.OPERATOR_EXTRACT_SIGN, LibPieceGroups.TRIGNOMETRY);
        register(PieceOperatorToDegrees.class, LibPieces.OPERATOR_TO_DEGREES, LibPieceGroups.TRIGNOMETRY);
        register(PieceOperatorToRadians.class, LibPieces.OPERATOR_TO_RADIANS, LibPieceGroups.TRIGNOMETRY);
        register(PieceOperatorRoot.class, LibPieces.OPERATOR_ROOT, LibPieceGroups.TRIGNOMETRY);
        register(PieceTrickPlant.class, LibPieces.TRICK_PLANT, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickPlantSequence.class, LibPieces.TRICK_PLANT_SEQUENCE, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickTill.class, LibPieces.TRICK_TILL, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickTillSequence.class, LibPieces.TRICK_TILL_SEQUENCE, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorListSize.class, LibPieces.OPERATOR_LIST_SIZE, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorGetBlockHardness.class, LibPieces.OPERATOR_GET_BLOCK_HARDNESS, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorGetBlockLight.class, LibPieces.OPERATOR_GET_BLOCK_LIGHT, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorGetBlockComparatorStrength.class, LibPieces.OPERATOR_GET_COMPARATOR_STRENGTH, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorGetBlockSolidity.class, LibPieces.OPERATOR_GET_BLOCK_SOLIDITY, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorGetBlockProperties.class, LibPieces.OPERATOR_GET_BLOCK_PROPERTIES, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorVectorRotate.class, LibPieces.OPERATOR_VECTOR_ROTATE, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceOperatorVectorFallback.class, LibPieces.OPERATOR_VECTOR_FALLBACK, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceOperatorVectorStrongRaycast.class, LibPieces.OPERATOR_VECTOR_STRONG_RAYCAST, LibPieceGroups.SECONDARY_OPERATORS);
        register(PiecePieceOperatorVectorStrongRaycastAxis.class, LibPieces.OPERATOR_STRONG_VECTOR_RAYCAST_AXIS, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceOperatorPlanarNormalVector.class, LibPieces.OPERATOR_PLANAR_NORMAL_VECTOR, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceTrickConjurePulsar.class, LibPieces.TRICK_PULSAR, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickConjurePulsarSequence.class, LibPieces.TRICK_PULSAR_SEQUENCE, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickPulsarLight.class, LibPieces.TRICK_PULSAR_LIGHT, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickParticleTrail.class, LibPieces.TRICK_PARTICLE_TRAIL, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickDebugSpamless.class, LibPieces.TRICK_DEBUG_SPAMLESS, LibPieceGroups.TUTORIAL_1);
        register(PieceTrickConjureStar.class, LibPieces.TRICK_CONJURE_STAR, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickBreakLoop.class, LibPieces.TRICK_BREAK_LOOP, LibPieceGroups.FLOW_CONTROL);


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
