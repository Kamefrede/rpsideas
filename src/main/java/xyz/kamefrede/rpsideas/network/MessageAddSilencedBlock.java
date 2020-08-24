package xyz.kamefrede.rpsideas.network;

import xyz.kamefrede.rpsideas.util.RPSSilencerHandler;
import xyz.kamefrede.rpsideas.util.SilencedPosition;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@PacketRegister(Side.CLIENT)
public class MessageAddSilencedBlock extends PacketBase {

    public MessageAddSilencedBlock() {
        //NO-OP
    }

    @Save
    private int time;

    @Save
    private long timestamp;

    @Save
    private BlockPos pos;

    @Save
    private int radius;

    @Save
    private float volume;

    @Save
    private int dimension;

    public MessageAddSilencedBlock(int time, long timestamp, BlockPos pos, int radius, float volume, int dimension) {
        this.time = time;
        this.timestamp = timestamp;
        this.pos = pos;
        this.radius = radius;
        this.volume = volume;
        this.dimension = dimension;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handle(@NotNull MessageContext ctx) {
        RPSSilencerHandler.checkAndAdd(new SilencedPosition(time, timestamp, pos, volume, radius, dimension));
    }
}
