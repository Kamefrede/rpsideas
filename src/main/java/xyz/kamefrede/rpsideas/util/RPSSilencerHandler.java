package xyz.kamefrede.rpsideas.util;

import xyz.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSSilencerHandler {
    private static final Set<SilencedPosition> silencers = new HashSet<>();

    @SideOnly(Side.CLIENT)
    public static void checkAndAdd(SilencedPosition toAdd) {
        long worldTime = Minecraft.getMinecraft().world.getWorldTime();
        silencers.removeIf(silencer -> silencer.expiryDate() < worldTime);

        for (SilencedPosition silencer : silencers) {
            if (silencer.getPosition() == toAdd.getPosition()) {
                silencer.renew(toAdd.getTimestamp(), toAdd.getTime(), toAdd.getVolume(), toAdd.getRadius());
                return;
            }
        }
        silencers.add(toAdd);
    }

    @SideOnly(Side.CLIENT)
    public static void doSilenceItPlz(PlaySoundEvent event, Vec3d pos) {
        long worldTime = Minecraft.getMinecraft().world.getWorldTime();
        silencers.removeIf(silencer -> silencer.expiryDate() < worldTime);

        int dimension = Minecraft.getMinecraft().world.provider.getDimension();

        for (SilencedPosition silencer : silencers) {
            BlockPos silencerpos = silencer.getPosition();
            int radius = silencer.getRadius();
            AxisAlignedBB aabb = new AxisAlignedBB(silencerpos.getX() - radius, silencerpos.getY() - radius, silencerpos.getZ() - radius,
                    silencerpos.getX() + radius, silencerpos.getY() + radius, silencerpos.getZ() + radius);
            if (aabb.contains(pos) && silencer.getVolume() != 1 && dimension == silencer.getDimension()) {
                ISound sound = event.getSound();
                event.setResultSound(new SilencedSound(sound, silencer.getVolume()));
            }

        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void muteSounds(PlaySoundEvent event) {
        if (Minecraft.getMinecraft().world != null && event.getResultSound() != null) {
            Vec3d pos = new Vec3d(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF());
            doSilenceItPlz(event, pos);
        }
    }
}
