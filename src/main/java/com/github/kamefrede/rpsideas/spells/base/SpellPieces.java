package com.github.kamefrede.rpsideas.spells.base;


import com.github.kamefrede.rpsideas.compat.botania.BotaniaCompatPieces;
import com.github.kamefrede.rpsideas.spells.constant.PieceConstantTau;
import com.github.kamefrede.rpsideas.spells.macro.PieceMacroCasterRaycast;
import com.github.kamefrede.rpsideas.spells.operator.*;
import com.github.kamefrede.rpsideas.spells.operator.block.*;
import com.github.kamefrede.rpsideas.spells.operator.entity.OperatorDistanceFromGround;
import com.github.kamefrede.rpsideas.spells.operator.entity.PieceOperatorIsElytraFlying;
import com.github.kamefrede.rpsideas.spells.operator.entity.PieceOperatorVisibleToEntity;
import com.github.kamefrede.rpsideas.spells.operator.fe.*;
import com.github.kamefrede.rpsideas.spells.operator.list.PieceOperatorListExclusion;
import com.github.kamefrede.rpsideas.spells.operator.list.PieceOperatorListIntersection;
import com.github.kamefrede.rpsideas.spells.operator.list.PieceOperatorListSize;
import com.github.kamefrede.rpsideas.spells.operator.list.PieceOperatorListUnion;
import com.github.kamefrede.rpsideas.spells.operator.math.PieceOperatorExtractSign;
import com.github.kamefrede.rpsideas.spells.operator.math.PieceOperatorRoot;
import com.github.kamefrede.rpsideas.spells.operator.math.PieceOperatorToDegrees;
import com.github.kamefrede.rpsideas.spells.operator.math.PieceOperatorToRadians;
import com.github.kamefrede.rpsideas.spells.operator.vector.*;
import com.github.kamefrede.rpsideas.spells.selector.*;
import com.github.kamefrede.rpsideas.spells.trick.block.*;
import com.github.kamefrede.rpsideas.spells.trick.entity.PieceTrickCloseElytra;
import com.github.kamefrede.rpsideas.spells.trick.entity.PieceTrickDetonate;
import com.github.kamefrede.rpsideas.spells.trick.entity.PieceTrickNumBroadcast;
import com.github.kamefrede.rpsideas.spells.trick.entity.PieceTrickOpenElytra;
import com.github.kamefrede.rpsideas.spells.trick.misc.*;
import com.github.kamefrede.rpsideas.spells.trick.potion.PieceTrickBlindness;
import com.github.kamefrede.rpsideas.spells.trick.potion.PieceTrickNausea;
import com.github.kamefrede.rpsideas.util.libs.LibPieces;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;

public class SpellPieces  {

    public static void init(){

        PsiAPI.setGroupRequirements(LibPieces.ALTERNATE_CONJURATION, 21, LibPieceGroups.BLOCK_CONJURATION);
        PsiAPI.setGroupRequirements(LibPieces.SECONDARY_VECTOR_OPERATORS, 21, LibPieceGroups.TRIGNOMETRY);
        PsiAPI.setGroupRequirements(LibPieces.BLOCK_PROPERTIES, 21, LibPieceGroups.BLOCK_CONJURATION);
        PsiAPI.setGroupRequirements(LibPieces.MACROS,21, LibPieceGroups.BLOCK_WORKS);
        PsiAPI.setGroupRequirements(LibPieces.VISUAL_AUDITIVE, 21, LibPieceGroups.GREATER_INFUSION);
        PsiAPI.setGroupRequirements(LibPieces.ADVANCED_LOOPCAST_CONTROL, 21, LibPieceGroups.LOOPCASTING);
        //PsiAPI.setGroupRequirements(LibPieces.INTER_CAD, 21, LibPieceGroups.MEMORY_MANAGEMENT);

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
        register(PieceOperatorGetBlockHardness.class, LibPieces.OPERATOR_GET_BLOCK_HARDNESS, LibPieces.BLOCK_PROPERTIES);
        register(PieceOperatorGetBlockLight.class, LibPieces.OPERATOR_GET_BLOCK_LIGHT, LibPieces.BLOCK_PROPERTIES, true);
        register(PieceOperatorGetBlockComparatorStrength.class, LibPieces.OPERATOR_GET_COMPARATOR_STRENGTH, LibPieces.BLOCK_PROPERTIES);
        register(PieceOperatorGetBlockSolidity.class, LibPieces.OPERATOR_GET_BLOCK_SOLIDITY, LibPieces.BLOCK_PROPERTIES);
        register(PieceOperatorVectorRotate.class, LibPieces.OPERATOR_VECTOR_ROTATE, LibPieces.SECONDARY_VECTOR_OPERATORS);
        register(PieceOperatorVectorFallback.class, LibPieces.OPERATOR_VECTOR_FALLBACK, LibPieces.SECONDARY_VECTOR_OPERATORS);
        register(PieceOperatorVectorStrongRaycast.class, LibPieces.OPERATOR_VECTOR_STRONG_RAYCAST, LibPieces.SECONDARY_VECTOR_OPERATORS);
        register(PiecePieceOperatorVectorStrongRaycastAxis.class, LibPieces.OPERATOR_STRONG_VECTOR_RAYCAST_AXIS, LibPieces.SECONDARY_VECTOR_OPERATORS);
        register(PieceOperatorPlanarNormalVector.class, LibPieces.OPERATOR_PLANAR_NORMAL_VECTOR, LibPieces.SECONDARY_VECTOR_OPERATORS, true);
        register(PieceTrickConjurePulsar.class, LibPieces.TRICK_PULSAR, LibPieces.ALTERNATE_CONJURATION, true);
        register(PieceTrickConjurePulsarSequence.class, LibPieces.TRICK_PULSAR_SEQUENCE, LibPieces.ALTERNATE_CONJURATION);
        register(PieceTrickPulsarLight.class, LibPieces.TRICK_PULSAR_LIGHT, LibPieces.ALTERNATE_CONJURATION);
        register(PieceTrickParticleTrail.class, LibPieces.TRICK_PARTICLE_TRAIL, LibPieces.ALTERNATE_CONJURATION);
        register(PieceTrickDebugSpamless.class, LibPieces.TRICK_DEBUG_SPAMLESS, LibPieceGroups.TUTORIAL_1);
        register(PieceTrickConjureStar.class, LibPieces.TRICK_CONJURE_STAR, LibPieces.ALTERNATE_CONJURATION);
        register(PieceTrickBreakLoop.class, LibPieces.TRICK_BREAK_LOOP, LibPieceGroups.FLOW_CONTROL);
        register(PieceSelectorCasterBattery.class, LibPieces.SELECTOR_CASTER_BATTERY, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorCasterEnergy.class, LibPieces.SELECTOR_CASTER_ENERGY, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorEmptyList.class, LibPieces.SELECTOR_EMPTY_LIST, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListUnion.class, LibPieces.OPERATOR_LIST_UNION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListIntersection.class, LibPieces.OPERATOR_LIST_INTERSECTION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListExclusion.class, LibPieces.OPERATOR_LIST_EXCLUSION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceTrickNausea.class, LibPieces.PIECE_TRICK_NAUSEA, LibPieceGroups.NEGATIVE_EFFECTS);
        register(PieceTrickBlindness.class, LibPieces.PIECE_TRICK_BLINDNESS, LibPieceGroups.NEGATIVE_EFFECTS);
        register(PieceSelectorEidosTimestamp.class, LibPieces.PIECE_SELECTOR_EIDOS_TIMESTAMP, LibPieceGroups.EIDOS_REVERSAL);
        register(PieceSelectorNearbyVehicles.class, LibPieces.SELECTOR_NEARBY_VECHICLES, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorFallingBlocks.class, LibPieces.SELECTOR_NEARBY_FALLING_BLOCKS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorGetSignText.class, LibPieces.OPERATOR_GET_SIGN_TEXT, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceTrickMoveBlockSequence.class, LibPieces.TRICK_MOVE_BLOCK_SEQUENCE, LibPieceGroups.BLOCK_MOVEMENT);
        register(PieceOperatorGetComment.class, LibPieces.OPERATOR_GET_COMMENT, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorGlowing.class, LibPieces.SELECTOR_GLOWING, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorListFilter.class, LibPieces.SELECTOR_LIST_FILTER, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorVisibleToEntity.class, LibPieces.OPERATOR_IS_VISIBLE, LibPieceGroups.DETECTION_DYNAMICS);
        register(PieceSelectorAffectedByPotions.class, LibPieces.SELECTOR_AFFECTED_BY_POTIONS, LibPieceGroups.ENTITIES_INTRO);
        register(PieceSelectorVisibleEntities.class, LibPieces.SELECTOR_VISIBLE_ENTITIES, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorVectorAbsolute.class, LibPieces.OPERATOR_VECTOR_ABSOLUTE, LibPieceGroups.VECTORS_INTRO);
        register(OperatorDistanceFromGround.class, LibPieces.OPERATOR_GET_DISTANCE_FROM_GROUND, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceOperatorEntityRaycast.class, LibPieces.OPERATOR_ENTITY_RAYCAST, LibPieceGroups.SECONDARY_OPERATORS);
        register(PieceTrickConjureGravityBlock.class, LibPieces.TRICK_CONJURE_GRAVITY, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickConjureGravityBlockSequence.class, LibPieces.TRICK_CONJURE_GRAVITY_SEQUENCE, LibPieceGroups.BLOCK_CONJURATION);
        register(PieceTrickRotateBlock.class, LibPieces.TRICK_ROTATE_BLOCK, LibPieceGroups.BLOCK_MOVEMENT);
        register(OperatorCanExtractEnergy.class, LibPieces.OPERATOR_CAN_EXTRACT_ENERGY, LibPieces.BLOCK_PROPERTIES);
        register(OperatorCanReceiveEnergy.class, LibPieces.OPERATOR_CAN_RECEIVE_ENERGY, LibPieces.BLOCK_PROPERTIES);
        register(OperatorMaxEnergyStored.class, LibPieces.OPERATOR_MAX_ENERGY_STORED, LibPieces.BLOCK_PROPERTIES);
        register(OperatorMaxInput.class, LibPieces.OPERATOR_MAX_INPUT, LibPieces.BLOCK_PROPERTIES);
        register(OperatorMaxOutput.class, LibPieces.OPERATOR_MAX_OUTPUT, LibPieces.BLOCK_PROPERTIES);
        register(OperatorStoredEnergy.class, LibPieces.OPERATOR_STORED_ENERGY, LibPieces.BLOCK_PROPERTIES);
        register(PieceTrickCloseElytra.class, LibPieces.TRICK_CLOSE_ELYTRA, LibPieceGroups.MOVEMENT);
        register(PieceTrickOpenElytra.class, LibPieces.TRICK_OPEN_ELYTRA, LibPieceGroups.MOVEMENT);
        register(PieceOperatorIsElytraFlying.class, LibPieces.OPERATOR_IS_ELYTRA_FLYING, LibPieceGroups.MOVEMENT);
        register(PieceOperatorGetDamage.class, LibPieces.OPERATOR_GET_DAMAGE, LibPieces.BLOCK_PROPERTIES);
        register(PieceOperatorGetMetadata.class, LibPieces.OPERATOR_GET_METADATA, LibPieces.BLOCK_PROPERTIES);
        register(PieceTrickSmeltBlockSequence.class, LibPieces.TRICK_SMELT_BLOCK_SEQUENCE, LibPieceGroups.SMELTERY);
        register(PieceTrickRepair.class, LibPieces.TRICK_REPAIR, LibPieceGroups.SMELTERY);
        register(PieceMacroCasterRaycast.class, LibPieces.MACRO_CASTER_RAYCAST, LibPieces.MACROS, true);
        register(PieceConstantTau.class, LibPieces.CONSTANT_TAU, LibPieceGroups.TRIGNOMETRY);
        register(TrickSound.class, LibPieces.TRICK_SOUND, LibPieces.VISUAL_AUDITIVE, true);
        register(PieceSelectorSucessionCounter.class, LibPieces.SELECTOR_SUCESSION_COUNTER, LibPieces.ADVANCED_LOOPCAST_CONTROL, true);
        register(PieceTrickSupressNextTrick.class, LibPieces.TRICK_SUPRESS_NEXT_TRICK, LibPieces.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickDetonate.class, LibPieces.TRICK_DETONATE, LibPieces.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickSlotMatch.class, LibPieces.TRICK_SLOT_MATCH, LibPieces.ALTERNATE_CONJURATION);
        register(PieceSelectorNumberCharges.class, LibPieces.SELECTOR_NUMBER_CHARGES, LibPieces.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickSpinChamber.class, LibPieces.TRICK_SPIN_CHAMBER, LibPieces.ADVANCED_LOOPCAST_CONTROL);
        register(PieceTrickFirework.class, LibPieces.TRICK_FIREWORK, LibPieces.VISUAL_AUDITIVE);
        //register(PieceTrickNumBroadcast.class, LibPieces.TRICK_NUM_BROADCAST, LibPieces.INTER_CAD, true);
        //register(PieceSelectorTransmission.class, LibPieces.SELECTOR_TRANSMISSION, LibPieces.INTER_CAD);
        register(PieceTrickFreezeBlock.class, LibPieces.TRICK_FREEZE_BLOCK, LibPieces.ALTERNATE_CONJURATION);
        if(Loader.isModLoaded("botania")) {
            BotaniaCompatPieces.init();
        }


    }

    public static void register(Class<? extends SpellPiece> pieceClass, String name, String group) {
        register(pieceClass, name, group, false);
    }

    public static void register(Class<? extends SpellPiece> pieceClass, String name, String group, boolean main) {
        String key = Reference.MODID + "." + name;
        PsiAPI.registerSpellPiece(key, pieceClass);
        PsiAPI.simpleSpellTextures.put(key, new ResourceLocation(Reference.MODID, "textures/spell/" + name + ".png"));
        PsiAPI.addPieceToGroup(pieceClass, group, main);
    }
}
