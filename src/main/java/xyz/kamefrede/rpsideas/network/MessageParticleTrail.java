package xyz.kamefrede.rpsideas.network;

import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.psi.common.Psi;

import javax.annotation.Nonnull;

@PacketRegister(Side.CLIENT)
public class MessageParticleTrail extends PacketBase {

    private static final int STEPS_PER_UNIT = 4;

    @Save
    private Vec3d position;
    @Save
    private Vec3d direction;
    @Save
    private double length;
    @Save
    private int time;
    @Save
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
    public void handle(@Nonnull MessageContext context) {
        World world = LibrarianLib.PROXY.getClientPlayer().world;

        int color = SpellHelpers.getCADColor(cad);

        float red = SpellHelpers.getR(color);
        float green = SpellHelpers.getG(color);
        float blue = SpellHelpers.getB(color);

        Vec3d ray = direction.normalize().scale(1f / STEPS_PER_UNIT);
        int steps = (int) (length * STEPS_PER_UNIT);

        for (int i = 0; i < steps; i++) {
            double x = position.x + ray.x * i;
            double y = position.y + ray.y * i;
            double z = position.z + ray.z * i;

            Psi.proxy.sparkleFX(world, x, y, z, red, green, blue, 0, 0, 0, 1f, time);
        }
    }

}
