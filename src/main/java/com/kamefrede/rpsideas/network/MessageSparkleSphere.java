package com.kamefrede.rpsideas.network;

import com.kamefrede.rpsideas.entity.EntityGaussPulse;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.Psi;

import java.util.Random;

public class MessageSparkleSphere implements IMessage {// TODO: 12/15/18 look at

    Vec3d position;
    EntityGaussPulse.AmmoStatus status;

    @SuppressWarnings("unused")
    public MessageSparkleSphere() {
    }

    public MessageSparkleSphere(Vec3d position, EntityGaussPulse.AmmoStatus status) {
        this.position = position;
        this.status = status;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeInt(status.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        status = EntityGaussPulse.AmmoStatus.values()[buf.readInt()];
    }

    public static class Handler implements IMessageHandler<MessageSparkleSphere, IMessage> {
        @Override
        public IMessage onMessage(MessageSparkleSphere m, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                World world = Minecraft.getMinecraft().world;
                Random rand = world.rand;

                int color = m.status.color;
                float r = ((color & 0xFF0000) >> 16) / 255f;
                float g = ((color & 0x00FF00) >> 8) / 255f;
                float b = (color & 0x0000FF) / 255f;

                for (int thetaInitial = 0; thetaInitial < 360; thetaInitial += 10) {
                    for (int azimuthInitial = -90; azimuthInitial < 90; azimuthInitial += 10) {
                        double theta = thetaInitial + rand.nextInt(10);
                        double azimuth = azimuthInitial + rand.nextInt(10);
                        double dist = rand.nextDouble() * 2d + 3d;

                        double x = MathHelper.cos((float) (theta * Math.PI / 180)) * MathHelper.cos((float) (azimuth * Math.PI / 180)) * dist + m.position.x;
                        double y = MathHelper.sin((float) (azimuth * Math.PI / 180)) * dist + m.position.y;
                        double z = MathHelper.cos((float) (theta * Math.PI / 180)) * MathHelper.cos((float) (azimuth * Math.PI / 180)) * dist + m.position.z;

                        //TODO recheck
                        Vector3 direction = new Vector3(m.position).add(-x, -y, -z).normalize().multiply(-0.325 * dist / 5d);

                        Psi.proxy.sparkleFX(world, m.position.x, m.position.y, m.position.z, r, g, b, (float) direction.x, (float) direction.y, (float) direction.z, 1.2f, 12);
                    }
                }
            });

            return null;
        }
    }
}
