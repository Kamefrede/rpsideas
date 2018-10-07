package com.github.kamefrede.rpsideas.network;

//taken from psionic upgrades like most of their features merged into psideas
import com.github.kamefrede.rpsideas.Psiam;
import com.github.kamefrede.rpsideas.gui.GuiHandler;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.network.NetworkMessage;

public class RPSPacketHandler {


    public static SimpleNetworkWrapper NET;


    public static void initPackets() {
        NET = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
        int id = 0;
        RPSPacketHandler.NET.registerMessage(MessageSpamlessChat.Handler.class, MessageSpamlessChat.class, id++, Side.CLIENT);
        RPSPacketHandler.NET.registerMessage(MessageParticleTrail.Handler.class, MessageParticleTrail.class, id++, Side.CLIENT);

       NET.registerMessage(MessageFlashSync.Handler.class, MessageFlashSync.class, id++, Side.SERVER);

       //NET.registerMessage(MessageSparkleSphere.Handler.class, MessageSparkleSphere.class, id++, Side.CLIENT);


        NetworkRegistry.INSTANCE.registerGuiHandler(Psiam.INSTANCE, new GuiHandler());
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

    public static void sendToServer(IMessage message) {
        NET.sendToServer(message);
    }

}
