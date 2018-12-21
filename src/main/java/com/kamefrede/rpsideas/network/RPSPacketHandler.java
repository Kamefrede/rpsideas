package com.kamefrede.rpsideas.network;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RPSPacketHandler {

    private static SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(RPSIdeas.MODID);
    private static int id;

    public static void initPackets() {
        register(MessageNoSpamChat.class, Side.CLIENT);
        register(MessageParticleTrail.class, Side.CLIENT);
        register(MessageFlashSync.class, Side.SERVER);
        register(MessageSparkleSphere.class, Side.CLIENT);
    }

    private static void register(Class<? extends PacketBase> packet, Side target) {
        if (target.isClient())
            NET.registerMessage(new ClientMessageHandler<>(), packet, id++, target);
        else
            NET.registerMessage(new MessageHandler<>(), packet, id++, target);
    }

    public static void sendToAllWithinRange(IMessage message, World world, BlockPos point, double range) {
        NET.sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), point.getX() + .5, point.getY() + .5, point.getZ() + .5, range));
    }

    public static void sendToAllNear(IMessage message, World world, BlockPos point) {
        sendToAllWithinRange(message, world, point, 128);
    }

    public static void sendTo(IMessage message, EntityPlayerMP pmp) {
        NET.sendTo(message, pmp);
    }

    public static void sendToDimension(IMessage message, int dimension) {
        NET.sendToDimension(message, dimension);
    }

    public static void sendToServer(IMessage message) {
        NET.sendToServer(message);
    }

    private static class ClientMessageHandler<REQ extends PacketBase> extends MessageHandler<REQ> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(REQ message, MessageContext ctx) {
            IThreadListener listener = Minecraft.getMinecraft();
            listener.addScheduledTask(() -> message.handle(ctx));
            return message.reply(ctx);
        }
    }

    private static class MessageHandler<REQ extends PacketBase> implements IMessageHandler<REQ, IMessage> {
        @Override
        public IMessage onMessage(REQ message, MessageContext ctx) {
            IThreadListener listener = ctx.getServerHandler().player.getServerWorld();
            listener.addScheduledTask(() -> message.handle(ctx));
            return message.reply(ctx);
        }
    }

}
