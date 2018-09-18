package com.github.kamefrede.rpsideas.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;
import com.github.kamefrede.rpsideas.util.NetworkHelpers;

import java.awt.*;

public class MessageParticleTrail implements IMessage {
    @SuppressWarnings("unused")
    public MessageParticleTrail() {}

    public MessageParticleTrail(Vec3d position, Vec3d direction, double length, int time, ItemStack cad) {
        this.position = position;
        this.direction = direction;
        this.length = length;
        this.time = time;
        this.cad = cad;
    }

    Vec3d position;
    Vec3d direction;
    double length;
    int time;
    ItemStack cad;

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkHelpers.writeVec3d(buf, position);
        NetworkHelpers.writeVec3d(buf, direction);
        buf.writeDouble(length);
        buf.writeInt(time);
        ByteBufUtils.writeItemStack(buf, cad);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        position = NetworkHelpers.readVec3d(buf);
        direction = NetworkHelpers.readVec3d(buf);
        length = buf.readDouble();
        time = buf.readInt();
        cad = ByteBufUtils.readItemStack(buf);
    }

    public static class Handler implements IMessageHandler<MessageParticleTrail, IMessage> {
        @Override
        public IMessage onMessage(MessageParticleTrail m, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                World world = Minecraft.getMinecraft().world;

                Color color = new Color(ICADColorizer.DEFAULT_SPELL_COLOR);
                if(!m.cad.isEmpty()) Psi.proxy.getCADColor(m.cad);

                float red = color.getRed() / 255f;
                float green = color.getGreen() / 255f;
                float blue = color.getBlue() / 255f;

                Vec3d ray = m.direction.normalize();
                int steps = (int) m.length * 4;

                for(int i = 0; i < steps; i++) {
                    Vec3d extended = ray.scale(i / 4f);
                    double x = m.position.x + extended.x;
                    double y = m.position.y + extended.y;
                    double z = m.position.z + extended.z;

                    Psi.proxy.sparkleFX(world, x, y, z, red, green, blue, 0, 0, 0, .25f, m.time);
                }
            });

            return null;
        }
    }
}