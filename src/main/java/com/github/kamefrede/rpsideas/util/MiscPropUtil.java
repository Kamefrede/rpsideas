package com.github.kamefrede.rpsideas.util;

import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.function.Function;

public class MiscPropUtil {

    static Function<Pair<BlockProperties, EnumFacing>, Integer> internalPropertyComparator;

    public static void setInternalPropertyComparator(Function<Pair<BlockProperties, EnumFacing>, Integer> f) {
        internalPropertyComparator = f;
    }
}
