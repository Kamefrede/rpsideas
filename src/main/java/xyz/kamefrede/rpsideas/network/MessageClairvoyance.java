package xyz.kamefrede.rpsideas.network;

import xyz.kamefrede.rpsideas.render.EntityPlayerSeer;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@PacketRegister(Side.CLIENT)
public class MessageClairvoyance extends PacketBase {

    @Save
    public int time;

    @Save
    public Vec3d offset;

    public MessageClairvoyance() {
        //NO-OP
    }

    public MessageClairvoyance(int time, Vec3d offset) {
        this.time = time;
        this.offset = offset;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handle(@Nonnull MessageContext context) {
        EntityPlayerSeer.timeLeft = time;
        EntityPlayerSeer.offsetVector = offset;
    }
}
