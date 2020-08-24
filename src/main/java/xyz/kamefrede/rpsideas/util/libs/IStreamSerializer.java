package com.kamefrede.rpsideas.util.libs;

import net.minecraft.network.PacketBuffer;

import java.io.IOException;

//https://github.com/OpenMods/OpenModsLib/blob/master/src/main/java/openmods/utils/io/IStreamSerializer.java

public interface IStreamSerializer<T> {

    public T readFromStream(PacketBuffer input) throws IOException;

    public void writeToStream(T o, PacketBuffer output) throws IOException;
}
