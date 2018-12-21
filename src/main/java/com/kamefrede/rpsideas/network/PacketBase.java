package com.kamefrede.rpsideas.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author WireSegal
 * Created at 4:18 PM on 12/20/18.
 */
public abstract class PacketBase implements IMessage {
    public static void writeVec(@Nonnull PacketBuffer buf, @Nullable Vec3d vec) {
        if (vec != null)
            buf.writeDouble(vec.x).writeDouble(vec.y).writeDouble(vec.z);
        else
            buf.writeDouble(0).writeDouble(0).writeDouble(0);
    }

    @Nonnull
    public static Vec3d readVec(@Nonnull PacketBuffer buf) {
        return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public static void writeEnum(@Nonnull PacketBuffer buf, @Nullable Enum<?> en) {
        if (en != null)
            buf.writeInt(en.ordinal());
        else
            buf.writeInt(0);
    }

    @Nonnull
    public static <T extends Enum<T>> T readEnum(@Nonnull PacketBuffer buf, @Nonnull Class<T> enumClazz) {
        T[] constants = enumClazz.getEnumConstants();
        return constants[buf.readInt() % constants.length];
    }

    public abstract void handle(@Nonnull MessageContext context);

    @Nullable
    public IMessage reply(@Nonnull MessageContext context) {
        return null;
    }

    @Override
    public final void toBytes(ByteBuf buf) {
        try {
            write(new PacketBuffer(buf));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void fromBytes(ByteBuf buf) {
        try {
            read(new PacketBuffer(buf));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void read(@Nonnull PacketBuffer buf) throws IOException;

    public abstract void write(@Nonnull PacketBuffer buf) throws IOException;
}
