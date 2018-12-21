package com.kamefrede.rpsideas.network;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;

public class MessageParticleTrail extends PacketBase {

    private static final int STEPS_PER_UNIT = 4;

    private Vec3d position;
    private Vec3d direction;
    private double length;
    private int time;
    private ItemStack cad;

    @SuppressWarnings("unused")
    public MessageParticleTrail() {
        // NO-OP
    }

    public MessageParticleTrail(Vec3d position, Vec3d direction, double length, int time, ItemStack cad) {
        this.position = position;
        this.direction = direction;
        this.length = length;
        this.time = time;
        this.cad = cad;
    }

    @Override
    public void write(@Nonnull PacketBuffer buf) {
        writeVec(buf, position);
        writeVec(buf, direction);
        buf.writeDouble(length);
        buf.writeInt(time);
        buf.writeItemStack(cad);
    }

    @Override
    public void read(@Nonnull PacketBuffer buf) throws IOException {
        position = readVec(buf);
        direction = readVec(buf);
        length = buf.readDouble();
        time = buf.readInt();
        cad = buf.readItemStack();
    }

    @Override
    public void handle(@Nonnull MessageContext context) {
        World world = Minecraft.getMinecraft().world;

        Color color = new Color(ICADColorizer.DEFAULT_SPELL_COLOR);
        if (!cad.isEmpty()) Psi.proxy.getCADColor(cad);

        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;

        Vec3d ray = direction.normalize().scale(1f / STEPS_PER_UNIT);
        int steps = (int) (length * STEPS_PER_UNIT);

        for (int i = 0; i < steps; i++) {
            double x = position.x + ray.x * i;
            double y = position.y + ray.y * i;
            double z = position.z + ray.z * i;

            Psi.proxy.sparkleFX(world, x, y, z, red, green, blue, 0, 0, 0, 0.4f, time);
        }
    }

}
