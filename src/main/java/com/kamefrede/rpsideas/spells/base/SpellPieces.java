package com.kamefrede.rpsideas.spells.base;


import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.compat.botania.BotaniaCompatPieces;
import com.kamefrede.rpsideas.spells.constant.PieceConstantTau;
import com.kamefrede.rpsideas.spells.macro.PieceMacroCasterRaycast;
import com.kamefrede.rpsideas.spells.operator.PieceOperatorGetComment;
import com.kamefrede.rpsideas.spells.operator.PieceOperatorGetDamage;
import com.kamefrede.rpsideas.spells.operator.PieceOperatorGetMetadata;
import com.kamefrede.rpsideas.spells.operator.block.*;
import com.kamefrede.rpsideas.spells.operator.entity.OperatorDistanceFromGround;
import com.kamefrede.rpsideas.spells.operator.entity.PieceOperatorIsElytraFlying;
import com.kamefrede.rpsideas.spells.operator.entity.PieceOperatorVisibleToEntity;
import com.kamefrede.rpsideas.spells.operator.fe.*;
import com.kamefrede.rpsideas.spells.operator.list.PieceOperatorListExclusion;
import com.kamefrede.rpsideas.spells.operator.list.PieceOperatorListIntersection;
import com.kamefrede.rpsideas.spells.operator.list.PieceOperatorListSize;
import com.kamefrede.rpsideas.spells.operator.list.PieceOperatorListUnion;
import com.kamefrede.rpsideas.spells.operator.math.PieceOperatorExtractSign;
import com.kamefrede.rpsideas.spells.operator.math.PieceOperatorRoot;
import com.kamefrede.rpsideas.spells.operator.math.PieceOperatorToDegrees;
import com.kamefrede.rpsideas.spells.operator.math.PieceOperatorToRadians;
import com.kamefrede.rpsideas.spells.operator.vector.*;
import com.kamefrede.rpsideas.spells.selector.*;
import com.kamefrede.rpsideas.spells.trick.block.*;
import com.kamefrede.rpsideas.spells.trick.entity.PieceTrickCloseElytra;
import com.kamefrede.rpsideas.spells.trick.entity.PieceTrickDetonate;
import com.kamefrede.rpsideas.spells.trick.entity.PieceTrickNumBroadcast;
import com.kamefrede.rpsideas.spells.trick.entity.PieceTrickOpenElytra;
import com.kamefrede.rpsideas.spells.trick.misc.*;
import com.kamefrede.rpsideas.spells.trick.potion.PieceTrickBlindness;
import com.kamefrede.rpsideas.spells.trick.potion.PieceTrickNausea;
import com.kamefrede.rpsideas.util.libs.LibPieceNames;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;

public class SpellPieces {// TODO: 12/15/18 look at

    public static void init() {

        PsiAPI.setGroupRequirements(LibPieceNames.ALTERNATE_CONJURATION, 21, LibPieceGroups.BLOCK_CONJURATION);
        PsiAPI.setGroupRequirements(LibPieceNames.SECONDARY_VECTOR_OPERATORS, 21, LibPieceGroups.TRIGNOMETRY);
        PsiAPI.setGroupRequirements(LibPieceNames.BLOCK_PROPERTIES, 21, LibPieceGroups.BLOCK_CONJURATION);
        PsiAPI.setGroupRequirements(LibPieceNames.MACROS, 21, LibPieceGroups.BLOCK_WORKS);
        PsiAPI.setGroupRequirements(LibPieceNames.VISUAL_AUDITIVE, 21, LibPieceGroups.GREATER_INFUSION);
        PsiAPI.setGroupRequirements(LibPieceNames.ADVANCED_LOOPCAST_CONTROL, 21, LibPieceGroups.LOOPCASTING);
        PsiAPI.setGroupRequirements(LibPieceNames.INTER_CAD, 21, LibPieceGroups.MEMORY_MANAGEMENT);

        register(PieceTrickConjureEtherealBlock.class, LibPieceNames.TRICK_CONJURE_ETHEREAL_BLOCK, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickConjureEtherealBlockSequence.class, LibPieceNames.TRICK_CONJURE_ETHEREAL_BLOCK_SEQUENCE, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceSelectorNearbyPlayers.class, LibPieceNames.SELECTOR_NEARBY_PLAYERS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceTrickDirectionPlaceBlock.class, LibPieceNames.TRICK_DIRECTION_PLACE_BLOCK, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorExtractSign.class, LibPieceNames.OPERATOR_EXTRACT_SIGN, LibPieceGroups.TRIGNOMETRY);
        register(PieceOperatorToDegrees.class, LibPieceNames.OPERATOR_TO_DEGREES, LibPieceGroups.TRIGNOMETRY);
        register(PieceOperatorToRadians.class, LibPieceNames.OPERATOR_TO_RADIANS, LibPieceGroups.TRIGNOMETRY);
        register(PieceOperatorRoot.class, LibPieceNames.OPERATOR_ROOT, LibPieceGroups.TRIGNOMETRY);
        register(PieceTrickPlant.class, LibPieceNames.TRICK_PLANT, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickPlantSequence.class, LibPieceNames.TRICK_PLANT_SEQUENCE, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickTill.class, LibPieceNames.TRICK_TILL, LibPieceGroups.BLOCK_WORKS);
        register(PieceTrickTillSequence.class, LibPieceNames.TRICK_TILL_SEQUENCE, LibPieceGroups.BLOCK_WORKS);
        register(PieceOperatorListSize.class, LibPieceNames.OPERATOR_LIST_SIZE, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorGetBlockHardness.class, LibPieceNames.OPERATOR_GET_BLOCK_HARDNESS, LibPieceNames.BLOCK_PROPERTIES);
        register(PieceOperatorGetBlockLight.class, LibPieceNames.OPERATOR_GET_BLOCK_LIGHT, LibPieceNames.BLOCK_PROPERTIES, true);
        register(PieceOperatorGetBlockComparatorStrength.class, LibPieceNames.OPERATOR_GET_COMPARATOR_STRENGTH, LibPieceNames.BLOCK_PROPERTIES);
        register(PieceOperatorGetBlockSolidity.class, LibPieceNames.OPERATOR_GET_BLOCK_SOLIDITY, LibPieceNames.BLOCK_PROPERTIES);
        register(PieceOperatorVectorRotate.class, LibPieceNames.OPERATOR_VECTOR_ROTATE, LibPieceNames.SECONDARY_VECTOR_OPERATORS);
        register(PieceOperatorVectorFallback.class, LibPieceNames.OPERATOR_VECTOR_FALLBACK, LibPieceNames.SECONDARY_VECTOR_OPERATORS);
        register(PieceOperatorVectorStrongRaycast.class, LibPieceNames.OPERATOR_VECTOR_STRONG_RAYCAST, LibPieceNames.SECONDARY_VECTOR_OPERATORS);
        register(PiecePieceOperatorVectorStrongRaycastAxis.class, LibPieceNames.OPERATOR_STRONG_VECTOR_RAYCAST_AXIS, LibPieceNames.SECONDARY_VECTOR_OPERATORS);
        register(PieceOperatorPlanarNormalVector.class, LibPieceNames.OPERATOR_PLANAR_NORMAL_VECTOR, LibPieceNames.SECONDARY_VECTOR_OPERATORS, true);
        register(PieceTrickConjurePulsar.class, LibPieceNames.TRICK_PULSAR, LibPieceNames.ALTERNATE_CONJURATION, true);
        register(PieceTrickConjurePulsarSequence.class, LibPieceNames.TRICK_PULSAR_SEQUENCE, LibPieceNames.ALTERNATE_CONJURATION);
        register(PieceTrickPulsarLight.class, LibPieceNames.TRICK_PULSAR_LIGHT, LibPieceNames.ALTERNATE_CONJURATION);
        register(PieceTrickParticleTrail.class, LibPieceNames.TRICK_PARTICLE_TRAIL, LibPieceNames.ALTERNATE_CONJURATION);
        register(PieceTrickDebugSpamless.class, LibPieceNames.TRICK_DEBUG_SPAMLESS, LibPieceGroups.TUTORIAL_1);
        register(PieceTrickConjureStar.class, LibPieceNames.TRICK_CONJURE_STAR, LibPieceNames.ALTERNATE_CONJURATION);
        register(PieceTrickBreakLoop.class, LibPieceNames.TRICK_BREAK_LOOP, LibPieceGroups.FLOW_CONTROL);
        register(PieceSelectorCasterBattery.class, LibPieceNames.SELECTOR_CASTER_BATTERY, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorCasterEnergy.class, LibPieceNames.SELECTOR_CASTER_ENERGY, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorEmptyList.class, LibPieceNames.SELECTOR_EMPTY_LIST, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListUnion.class, LibPieceNames.OPERATOR_LIST_UNION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListIntersection.class, LibPieceNames.OPERATOR_LIST_INTERSECTION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListExclusion.class, LibPieceNames.OPERATOR_LIST_EXCLUSION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceTrickNausea.class, LibPieceNames.PIECE_TRICK_NAUSEA, LibPieceGroups.NEGATIVE_EFFECTS);
        register(PieceTrickBlindness.class, LibPieceNames.PIECE_TRICK_BLINDNESS, LibPieceGroups.NEGATIVE_EFFECTS);
        register(PieceSelectorEidosTimestamp.class, LibPieceNames.PIECE_SELECTOR_EIDOS_TIMESTAMP, LibPieceGroups.EIDOS_REVERSAL);
        register(PieceSelectorNearbyVehicles.class, LibPieceNames.SELECTOR_NEARBY_VECHICLES, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorFallingBlocks.class, LibPieceNames.SELECTOR_NEARBY_FALLING_BLOCKS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorGetSignText.class, LibPieceNames.OPERATOR_GET_SIGN_TEXT, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceTrickMoveBlockSequence.class, LibPieceNames.TRICK_MOVE_BLOCK_SEQUENCE, LibPieceGroups.BLOCK_MOVEMENT);
        register(PieceOperatorGetComment.class, LibPieceNames.OPERATOR_GET_COMMENT, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorGlowing.class, LibPieceNames.SELECTOR_GLOWING, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorListFilter.class, LibPieceNames.SELECTOR_LIST_FILTER, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorVisibleToEntity.class, LibPieceNames.OPERATOR_IS_VISIBLE, LibPieceGroups.DETECTION_DYNAMICS);
        register(PieceSelectorAffectedByPotions.class, LibPieceNames.SELECTOR_AFFECTED_BY_POTIONS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorVisibleEntities.class, LibPieceNames.SELECTOR_VISIBLE_ENTITIES, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorVectorAbsolute.class, LibPieceNames.OPERATOR_VECTOR_ABSOLUTE, LibPieceGroups.VECTORS_INTRO);
        register(OperatorDistanceFromGround.class, LibPieceNames.OPERATOR_GET_DISTANCE_FROM_GROUND, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceOperatorEntityRaycast.class, LibPieceNames.OPERATOR_ENTITY_RAYCAST, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceTrickConjureGravityBlock.class, LibPieceNames.TRICK_CONJURE_GRAVITY, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickConjureGravityBlockSequence.class, LibPieceNames.TRICK_CONJURE_GRAVITY_SEQUENCE, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickRotateBlock.class, LibPieceNames.TRICK_ROTATE_BLOCK, LibPieceGroups.BLOCK_MOVEMENT);
        register(OperatorCanExtractEnergy.class, LibPieceNames.OPERATOR_CAN_EXTRACT_ENERGY, LibPieceNames.BLOCK_PROPERTIES);
        register(OperatorCanReceiveEnergy.class, LibPieceNames.OPERATOR_CAN_RECEIVE_ENERGY, LibPieceNames.BLOCK_PROPERTIES);
        register(OperatorMaxEnergyStored.class, LibPieceNames.OPERATOR_MAX_ENERGY_STORED, LibPieceNames.BLOCK_PROPERTIES);
        register(OperatorMaxInput.class, LibPieceNames.OPERATOR_MAX_INPUT, LibPieceNames.BLOCK_PROPERTIES);
        register(OperatorMaxOutput.class, LibPieceNames.OPERATOR_MAX_OUTPUT, LibPieceNames.BLOCK_PROPERTIES);
        register(OperatorStoredEnergy.class, LibPieceNames.OPERATOR_STORED_ENERGY, LibPieceNames.BLOCK_PROPERTIES);
        register(PieceTrickCloseElytra.class, LibPieceNames.TRICK_CLOSE_ELYTRA, LibPieceGroups.MOVEMENT);
        register(PieceTrickOpenElytra.class, LibPieceNames.TRICK_OPEN_ELYTRA, LibPieceGroups.MOVEMENT);
        register(PieceOperatorIsElytraFlying.class, LibPieceNames.OPERATOR_IS_ELYTRA_FLYING, LibPieceGroups.MOVEMENT);
        register(PieceOperatorGetDamage.class, LibPieceNames.OPERATOR_GET_DAMAGE, LibPieceNames.BLOCK_PROPERTIES);
        register(PieceOperatorGetMetadata.class, LibPieceNames.OPERATOR_GET_METADATA, LibPieceNames.BLOCK_PROPERTIES);
        register(PieceTrickSmeltBlockSequence.class, LibPieceNames.TRICK_SMELT_BLOCK_SEQUENCE, LibPieceGroups.SMELTERY);
        register(PieceTrickRepair.class, LibPieceNames.TRICK_REPAIR, LibPieceGroups.SMELTERY);
        register(PieceMacroCasterRaycast.class, LibPieceNames.MACRO_CASTER_RAYCAST, LibPieceNames.MACROS, true);
        register(PieceConstantTau.class, LibPieceNames.CONSTANT_TAU, LibPieceGroups.TRIGNOMETRY);
        register(TrickSound.class, LibPieceNames.TRICK_SOUND, LibPieceNames.VISUAL_AUDITIVE, true);
        register(PieceSelectorSucessionCounter.class, LibPieceNames.SELECTOR_SUCESSION_COUNTER, LibPieceNames.ADVANCED_LOOPCAST_CONTROL, true);
        register(PieceTrickSupressNextTrick.class, LibPieceNames.TRICK_SUPRESS_NEXT_TRICK, LibPieceNames.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickDetonate.class, LibPieceNames.TRICK_DETONATE, LibPieceNames.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickSlotMatch.class, LibPieceNames.TRICK_SLOT_MATCH, LibPieceNames.ALTERNATE_CONJURATION);
        register(PieceSelectorNumberCharges.class, LibPieceNames.SELECTOR_NUMBER_CHARGES, LibPieceNames.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickSpinChamber.class, LibPieceNames.TRICK_SPIN_CHAMBER, LibPieceNames.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickFirework.class, LibPieceNames.TRICK_FIREWORK, LibPieceNames.VISUAL_AUDITIVE);
        register(PieceTrickNumBroadcast.class, LibPieceNames.TRICK_NUM_BROADCAST, LibPieceNames.INTER_CAD, true);
        register(PieceSelectorTransmission.class, LibPieceNames.SELECTOR_TRANSMISSION, LibPieceNames.INTER_CAD);
        register(PieceTrickFreezeBlock.class, LibPieceNames.TRICK_FREEZE_BLOCK, LibPieceNames.ALTERNATE_CONJURATION);
        if (Loader.isModLoaded("botania")) {
            BotaniaCompatPieces.init();
        }


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
