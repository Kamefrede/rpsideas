package com.kamefrede.rpsideas.spells.base;


import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.compat.botania.BotaniaCompatPieces;
import com.kamefrede.rpsideas.spells.constant.PieceConstantTau;
import com.kamefrede.rpsideas.spells.operator.PieceOperatorGetDamage;
import com.kamefrede.rpsideas.spells.operator.PieceOperatorGetMetadata;
import com.kamefrede.rpsideas.spells.operator.block.*;
import com.kamefrede.rpsideas.spells.operator.energy.*;
import com.kamefrede.rpsideas.spells.operator.entity.*;
import com.kamefrede.rpsideas.spells.operator.list.PieceOperatorListExclusion;
import com.kamefrede.rpsideas.spells.operator.list.PieceOperatorListIntersection;
import com.kamefrede.rpsideas.spells.operator.list.PieceOperatorListSize;
import com.kamefrede.rpsideas.spells.operator.list.PieceOperatorListUnion;
import com.kamefrede.rpsideas.spells.operator.math.PieceOperatorRoot;
import com.kamefrede.rpsideas.spells.operator.math.PieceOperatorSignum;
import com.kamefrede.rpsideas.spells.operator.math.PieceOperatorToDegrees;
import com.kamefrede.rpsideas.spells.operator.math.PieceOperatorToRadians;
import com.kamefrede.rpsideas.spells.operator.string.PieceOperatorGetComment;
import com.kamefrede.rpsideas.spells.operator.string.PieceOperatorParseNumber;
import com.kamefrede.rpsideas.spells.operator.vector.*;
import com.kamefrede.rpsideas.spells.selector.*;
import com.kamefrede.rpsideas.spells.trick.block.*;
import com.kamefrede.rpsideas.spells.trick.entity.PieceTrickCloseElytra;
import com.kamefrede.rpsideas.spells.trick.entity.PieceTrickDetonate;
import com.kamefrede.rpsideas.spells.trick.entity.PieceTrickNumBroadcast;
import com.kamefrede.rpsideas.spells.trick.entity.PieceTrickOpenElytra;
import com.kamefrede.rpsideas.spells.trick.misc.*;
import com.kamefrede.rpsideas.util.libs.RPSPieceNames;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;

public class SpellPieces {

    public static void init() {

        PsiAPI.setGroupRequirements(RPSPieceNames.ALTERNATE_CONJURATION, 21, LibPieceGroups.BLOCK_CONJURATION);
        PsiAPI.setGroupRequirements(RPSPieceNames.SECONDARY_VECTOR_OPERATORS, 21, LibPieceGroups.TRIGNOMETRY);
        PsiAPI.setGroupRequirements(RPSPieceNames.BLOCK_PROPERTIES, 21, LibPieceGroups.BLOCK_CONJURATION);
        PsiAPI.setGroupRequirements(RPSPieceNames.MACROS, 21, LibPieceGroups.BLOCK_WORKS);
        PsiAPI.setGroupRequirements(RPSPieceNames.VISUAL_AUDITIVE, 21, LibPieceGroups.GREATER_INFUSION);
        PsiAPI.setGroupRequirements(RPSPieceNames.ADVANCED_LOOPCAST_CONTROL, 21, LibPieceGroups.LOOPCASTING);
        PsiAPI.setGroupRequirements(RPSPieceNames.INTER_CAD, 21, LibPieceGroups.MEMORY_MANAGEMENT);

        register(PieceTrickConjureEtherealBlock.class, RPSPieceNames.TRICK_CONJURE_ETHEREAL_BLOCK, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickConjureEtherealBlockSequence.class, RPSPieceNames.TRICK_CONJURE_ETHEREAL_BLOCK_SEQUENCE, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceSelectorNearbyPlayers.class, RPSPieceNames.SELECTOR_NEARBY_PLAYERS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceTrickDirectionPlaceBlock.class, RPSPieceNames.TRICK_DIRECTION_PLACE_BLOCK, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorSignum.class, RPSPieceNames.OPERATOR_EXTRACT_SIGN, LibPieceGroups.TRIGNOMETRY);
        register(PieceOperatorToDegrees.class, RPSPieceNames.OPERATOR_TO_DEGREES, LibPieceGroups.TRIGNOMETRY);
        register(PieceOperatorToRadians.class, RPSPieceNames.OPERATOR_TO_RADIANS, LibPieceGroups.TRIGNOMETRY);
        register(PieceOperatorRoot.class, RPSPieceNames.OPERATOR_ROOT, LibPieceGroups.TRIGNOMETRY);
        register(PieceTrickPlant.class, RPSPieceNames.TRICK_PLANT, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickPlantSequence.class, RPSPieceNames.TRICK_PLANT_SEQUENCE, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickTill.class, RPSPieceNames.TRICK_TILL, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickTillSequence.class, RPSPieceNames.TRICK_TILL_SEQUENCE, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorListSize.class, RPSPieceNames.OPERATOR_LIST_SIZE, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorGetBlockHardness.class, RPSPieceNames.OPERATOR_GET_BLOCK_HARDNESS, RPSPieceNames.BLOCK_PROPERTIES);
        register(PieceOperatorGetBlockLight.class, RPSPieceNames.OPERATOR_GET_BLOCK_LIGHT, RPSPieceNames.BLOCK_PROPERTIES, true);
        register(PieceOperatorGetBlockComparatorStrength.class, RPSPieceNames.OPERATOR_GET_COMPARATOR_STRENGTH, RPSPieceNames.BLOCK_PROPERTIES);
        register(PieceOperatorGetBlockSolidity.class, RPSPieceNames.OPERATOR_GET_BLOCK_SOLIDITY, RPSPieceNames.BLOCK_PROPERTIES);
        register(PieceOperatorVectorRotate.class, RPSPieceNames.OPERATOR_VECTOR_ROTATE, RPSPieceNames.SECONDARY_VECTOR_OPERATORS);
        register(PieceOperatorVectorFallback.class, RPSPieceNames.OPERATOR_VECTOR_FALLBACK, RPSPieceNames.SECONDARY_VECTOR_OPERATORS);
        register(PieceOperatorVectorStrongRaycast.class, RPSPieceNames.OPERATOR_VECTOR_STRONG_RAYCAST, RPSPieceNames.SECONDARY_VECTOR_OPERATORS);
        register(PiecePieceOperatorVectorStrongRaycastAxis.class, RPSPieceNames.OPERATOR_STRONG_VECTOR_RAYCAST_AXIS, RPSPieceNames.SECONDARY_VECTOR_OPERATORS);
        register(PieceOperatorPlanarNormalVector.class, RPSPieceNames.OPERATOR_PLANAR_NORMAL_VECTOR, RPSPieceNames.SECONDARY_VECTOR_OPERATORS, true);
        register(PieceTrickConjurePulsar.class, RPSPieceNames.TRICK_PULSAR, RPSPieceNames.ALTERNATE_CONJURATION, true);
        register(PieceTrickConjurePulsarSequence.class, RPSPieceNames.TRICK_PULSAR_SEQUENCE, RPSPieceNames.ALTERNATE_CONJURATION);
        register(PieceTrickPulsarLight.class, RPSPieceNames.TRICK_PULSAR_LIGHT, RPSPieceNames.ALTERNATE_CONJURATION);
        register(PieceTrickParticleTrail.class, RPSPieceNames.TRICK_PARTICLE_TRAIL, RPSPieceNames.ALTERNATE_CONJURATION);
        register(PieceTrickDebugSpamless.class, RPSPieceNames.TRICK_DEBUG_SPAMLESS, LibPieceGroups.TUTORIAL_1);
        register(PieceTrickConjureStar.class, RPSPieceNames.TRICK_CONJURE_STAR, RPSPieceNames.ALTERNATE_CONJURATION);
        register(PieceTrickBreakLoop.class, RPSPieceNames.TRICK_BREAK_LOOP, LibPieceGroups.FLOW_CONTROL);
        register(PieceSelectorCasterBattery.class, RPSPieceNames.SELECTOR_CASTER_BATTERY, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorCasterEnergy.class, RPSPieceNames.SELECTOR_CASTER_ENERGY, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorEmptyList.class, RPSPieceNames.SELECTOR_EMPTY_LIST, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListUnion.class, RPSPieceNames.OPERATOR_LIST_UNION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListIntersection.class, RPSPieceNames.OPERATOR_LIST_INTERSECTION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListExclusion.class, RPSPieceNames.OPERATOR_LIST_EXCLUSION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorEidosTimestamp.class, RPSPieceNames.PIECE_SELECTOR_EIDOS_TIMESTAMP, LibPieceGroups.EIDOS_REVERSAL);
        register(PieceSelectorNearbyVehicles.class, RPSPieceNames.SELECTOR_NEARBY_VECHICLES, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorFallingBlocks.class, RPSPieceNames.SELECTOR_NEARBY_FALLING_BLOCKS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorGetSignText.class, RPSPieceNames.OPERATOR_GET_SIGN_TEXT, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceTrickMoveBlockSequence.class, RPSPieceNames.TRICK_MOVE_BLOCK_SEQUENCE, LibPieceGroups.BLOCK_MOVEMENT);
        register(PieceOperatorGetComment.class, RPSPieceNames.OPERATOR_GET_COMMENT, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceOperatorParseNumber.class, RPSPieceNames.OPERATOR_PARSE_NUMBER, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorGlowing.class, RPSPieceNames.SELECTOR_GLOWING, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorListFilter.class, RPSPieceNames.SELECTOR_LIST_FILTER, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorVisibleToEntity.class, RPSPieceNames.OPERATOR_IS_VISIBLE, LibPieceGroups.DETECTION_DYNAMICS);
        register(PieceOperatorAffectedByPotions.class, RPSPieceNames.SELECTOR_AFFECTED_BY_POTIONS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorVisibleEntities.class, RPSPieceNames.SELECTOR_VISIBLE_ENTITIES, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorVectorAbsolute.class, RPSPieceNames.OPERATOR_VECTOR_ABSOLUTE, LibPieceGroups.VECTORS_INTRO);
        register(OperatorDistanceFromGround.class, RPSPieceNames.OPERATOR_GET_DISTANCE_FROM_GROUND, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceOperatorEntityRaycast.class, RPSPieceNames.OPERATOR_ENTITY_RAYCAST, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceTrickConjureGravityBlock.class, RPSPieceNames.TRICK_CONJURE_GRAVITY, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickConjureGravityBlockSequence.class, RPSPieceNames.TRICK_CONJURE_GRAVITY_SEQUENCE, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickRotateBlock.class, RPSPieceNames.TRICK_ROTATE_BLOCK, LibPieceGroups.BLOCK_MOVEMENT);
        register(OperatorCanExtractEnergy.class, RPSPieceNames.OPERATOR_CAN_EXTRACT_ENERGY, RPSPieceNames.BLOCK_PROPERTIES);
        register(OperatorCanReceiveEnergy.class, RPSPieceNames.OPERATOR_CAN_RECEIVE_ENERGY, RPSPieceNames.BLOCK_PROPERTIES);
        register(OperatorMaxEnergyStored.class, RPSPieceNames.OPERATOR_MAX_ENERGY_STORED, RPSPieceNames.BLOCK_PROPERTIES);
        register(OperatorMaxInput.class, RPSPieceNames.OPERATOR_MAX_INPUT, RPSPieceNames.BLOCK_PROPERTIES);
        register(OperatorMaxOutput.class, RPSPieceNames.OPERATOR_MAX_OUTPUT, RPSPieceNames.BLOCK_PROPERTIES);
        register(OperatorStoredEnergy.class, RPSPieceNames.OPERATOR_STORED_ENERGY, RPSPieceNames.BLOCK_PROPERTIES);
        register(PieceTrickCloseElytra.class, RPSPieceNames.TRICK_CLOSE_ELYTRA, LibPieceGroups.MOVEMENT);
        register(PieceTrickOpenElytra.class, RPSPieceNames.TRICK_OPEN_ELYTRA, LibPieceGroups.MOVEMENT);
        register(PieceOperatorIsElytraFlying.class, RPSPieceNames.OPERATOR_IS_ELYTRA_FLYING, LibPieceGroups.MOVEMENT);
        register(PieceOperatorGetDamage.class, RPSPieceNames.OPERATOR_GET_DAMAGE, RPSPieceNames.BLOCK_PROPERTIES);
        register(PieceOperatorGetMetadata.class, RPSPieceNames.OPERATOR_GET_METADATA, RPSPieceNames.BLOCK_PROPERTIES);
        register(PieceTrickSmeltBlockSequence.class, RPSPieceNames.TRICK_SMELT_BLOCK_SEQUENCE, LibPieceGroups.SMELTERY);
        register(PieceTrickRepair.class, RPSPieceNames.TRICK_REPAIR, LibPieceGroups.SMELTERY);
        register(PieceMacroCasterRaycast.class, RPSPieceNames.MACRO_CASTER_RAYCAST, RPSPieceNames.MACROS, true);
        register(PieceConstantTau.class, RPSPieceNames.CONSTANT_TAU, LibPieceGroups.TRIGNOMETRY);
        register(TrickSound.class, RPSPieceNames.TRICK_SOUND, RPSPieceNames.VISUAL_AUDITIVE, true);
        register(PieceSelectorSuccessCounter.class, RPSPieceNames.SELECTOR_SUCESSION_COUNTER, RPSPieceNames.ADVANCED_LOOPCAST_CONTROL, true);
        register(PieceTrickSupressNextTrick.class, RPSPieceNames.TRICK_SUPRESS_NEXT_TRICK, RPSPieceNames.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickDetonate.class, RPSPieceNames.TRICK_DETONATE, RPSPieceNames.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickSlotMatch.class, RPSPieceNames.TRICK_SLOT_MATCH, RPSPieceNames.ALTERNATE_CONJURATION);
        register(PieceSelectorNumberCharges.class, RPSPieceNames.SELECTOR_NUMBER_CHARGES, RPSPieceNames.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickSpinChamber.class, RPSPieceNames.TRICK_SPIN_CHAMBER, RPSPieceNames.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickFirework.class, RPSPieceNames.TRICK_FIREWORK, RPSPieceNames.VISUAL_AUDITIVE);
        register(PieceTrickNumBroadcast.class, RPSPieceNames.TRICK_NUM_BROADCAST, RPSPieceNames.INTER_CAD, true);
        register(PieceSelectorTransmission.class, RPSPieceNames.SELECTOR_TRANSMISSION, RPSPieceNames.INTER_CAD);
        register(PieceTrickFreezeBlock.class, RPSPieceNames.TRICK_FREEZE_BLOCK, RPSPieceNames.ALTERNATE_CONJURATION);
        register(PieceOperatorClosestToLine.class, RPSPieceNames.OPERATOR_CLOSEST_TO_LINE, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceTrickConjureText.class, RPSPieceNames.TRICK_CONJURE_TEXT, RPSPieceNames.VISUAL_AUDITIVE);
        if (Loader.isModLoaded("botania")) BotaniaCompatPieces.init();
    }

    public static void register(Class<? extends SpellPiece> pieceClass, String name, String group) {
        register(pieceClass, name, group, false);
    }

    public static void register(Class<? extends SpellPiece> pieceClass, String name, String group, boolean main) {
        String key = RPSIdeas.MODID + "." + name;
        PsiAPI.registerSpellPiece(key, pieceClass);
        PsiAPI.simpleSpellTextures.put(key, new ResourceLocation(RPSIdeas.MODID, "textures/spell/" + name + ".png"));
        PsiAPI.addPieceToGroup(pieceClass, group, main);
    }
}
