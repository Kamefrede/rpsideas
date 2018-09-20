package com.github.kamefrede.rpsideas.util.helpers;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;

public class NetworkHelpers {
    private NetworkHelpers() {}

    public static void writeVec3d(ByteBuf buf, Vec3d vec) {
        buf.writeDouble(vec.x);
        buf.writeDouble(vec.y);
        buf.writeDouble(vec.z);
    }

    public static Vec3d readVec3d(ByteBuf buf) {
        return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }}
