package com.kamefrede.rpsideas.rules.ranges.base;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.rules.ranges.EnumRangeType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author WireSegal
 * Created at 10:09 AM on 12/27/18.
 */
public interface IRange extends INBTSerializable<NBTTagCompound> {

    String RANGE = RPSIdeas.MODID + ".param.range";
    String X_RANGE = RPSIdeas.MODID + ".param.range.x";
    String Y_RANGE = RPSIdeas.MODID + ".param.range.y";
    String Z_RANGE = RPSIdeas.MODID + ".param.range.z";
    String X_POS = RPSIdeas.MODID + ".param.relpos.x";
    String Y_POS = RPSIdeas.MODID + ".param.relpos.y";
    String Z_POS = RPSIdeas.MODID + ".param.relpos.z";
    String X_MIN = RPSIdeas.MODID + ".param.relpos.x.min";
    String Y_MIN = RPSIdeas.MODID + ".param.relpos.y.min";
    String Z_MIN = RPSIdeas.MODID + ".param.relpos.z.min";
    String X_MAX = RPSIdeas.MODID + ".param.relpos.x.max";
    String Y_MAX = RPSIdeas.MODID + ".param.relpos.y.max";
    String Z_MAX = RPSIdeas.MODID + ".param.relpos.z.max";

    @Nonnull
    EnumRangeType getRangeType();

    boolean isInRange(BlockPos pos, double x, double y, double z);

    @Nonnull
    Map<String, RelativePos> getInputMap();

    @Nonnull
    Map<String, Double> getNumericMap();

    default double getValue(String key) {
        Map<String, Double> inputMap = getNumericMap();
        if (!inputMap.containsKey(key))
            return 0.0;

        return inputMap.get(key);
    }

    default double getX(BlockPos pos) {
        return getTransformedValue(X_POS, pos.getX() + 0.5);
    }

    default double getY(BlockPos pos) {
        return getTransformedValue(Y_POS, pos.getY() + 0.5);
    }

    default double getZ(BlockPos pos) {
        return getTransformedValue(Z_POS, pos.getZ() + 0.5);
    }

    default double getTransformedValue(String key, double real) {
        Map<String, RelativePos> inputMap = getInputMap();
        if (!inputMap.containsKey(key))
            return real;

        return inputMap.get(key).getTrueValue(real);
    }

    default double getTransformedShiftedValue(String key, double real) {
        Map<String, RelativePos> inputMap = getInputMap();
        if (!inputMap.containsKey(key))
            return real;

        return inputMap.get(key).getTrueValueWithShift(real);
    }

    @Override
    default NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Type", getRangeType().getNBTKey());

        Map<String, RelativePos> inputMap = getInputMap();
        Map<String, Double> numericMap = getNumericMap();

        NBTTagCompound positions = new NBTTagCompound();
        for (Map.Entry<String, RelativePos> position : inputMap.entrySet())
            positions.setTag(position.getKey(), position.getValue().serializeNBT());
        for (Map.Entry<String, Double> numeric : numericMap.entrySet())
            positions.setDouble(numeric.getKey(), numeric.getValue());

        compound.setTag("Values", positions);

        return compound;
    }

    @Override
    default void deserializeNBT(NBTTagCompound nbt) {
        NBTTagCompound positions = nbt.getCompoundTag("Values");

        Map<String, RelativePos> inputMap = getInputMap();
        Map<String, Double> numericMap = getNumericMap();

        for (String name : inputMap.keySet())
            if (positions.hasKey(name, Constants.NBT.TAG_STRING))
                inputMap.get(name).deserializeNBT((NBTTagString) positions.getTag(name));
        for (String name : numericMap.keySet())
            if (positions.hasKey(name, Constants.NBT.TAG_ANY_NUMERIC))
                numericMap.put(name, positions.getDouble(name));
    }

    static IRange deserialize(NBTTagCompound nbt) {
        String typeKey = nbt.getString("Type");
        EnumRangeType type = EnumRangeType.byKey(typeKey);
        if (type == null)
            return null;

        IRange range = type.createNewRange();
        range.deserializeNBT(nbt);
        return range;
    }
}
