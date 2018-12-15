package com.kamefrede.rpsideas.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;

import java.awt.*;

public class MessageParticleTrail implements IMessage {// TODO: 12/15/18 look at

    Vec3d position;
    Vec3d direction;
    double length;
    int time;
    ItemStack cad;
    @SuppressWarnings("unused")
    public MessageParticleTrail() {
    }
    public MessageParticleTrail(Vec3d position, Vec3d direction, double length, int time, ItemStack cad) {
        this.position = position;
        this.direction = direction;
        this.length = length;
        this.time = time;
        this.cad = cad;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeDouble(direction.x);
        buf.writeDouble(direction.y);
        buf.writeDouble(direction.z);
        buf.writeDouble(length);
        buf.writeInt(time);
        ByteBufUtils.writeItemStack(buf, cad);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        direction = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        length = buf.readDouble();
        time = buf.readInt();
        cad = ByteBufUtils.readItemStack(buf);
    }

    public static class Handler implements IMessageHandler<MessageParticleTrail, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(MessageParticleTrail m, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                World world = Minecraft.getMinecraft().world;

                Color color = new Color(ICADColorizer.DEFAULT_SPELL_COLOR);
                if (!m.cad.isEmpty()) Psi.proxy.getCADColor(m.cad);

                float red = color.getRed() / 255f;
                float green = color.getGreen() / 255f;
                float blue = color.getBlue() / 255f;

                Vec3d ray = m.direction.normalize();
                int steps = (int) m.length * 4;

                for (int i = 0; i < steps; i++) {
                    Vec3d extended = ray.scale(i / 4f);
                    double x = m.position.x + extended.x;
                    double y = m.position.y + extended.y;
                    double z = m.position.z + extended.z;

                    Psi.proxy.sparkleFX(world, x, y, z, red, green, blue, 0, 0, 0, .40f, m.time);
                }
            });

            return null;
        }
    }

}
