package xyz.kamefrede.rpsideas.tiles;

import xyz.kamefrede.rpsideas.blocks.RPSBlocks;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import xyz.kamefrede.rpsideas.util.libs.RPSBlockNames;
import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import vazkii.psi.common.Psi;

@TileRegister(RPSBlockNames.CONJURED_PULSAR_BLOCK)
public class TileConjuredPulsar extends TileModTickable {
    @Save
    private int time = -1;
    @Save
    private ItemStack colorizer = ItemStack.EMPTY;

    private int particleCounter = 0;

    public void setTime(int time) {
        this.time = time;
    }

    public void setColorizer(ItemStack colorizer) {
        this.colorizer = colorizer;
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            int color = SpellHelpers.getColor(colorizer);

            float red = SpellHelpers.getR(color);
            float green = SpellHelpers.getG(color);
            float blue = SpellHelpers.getB(color);

            IBlockState state = world.getBlockState(pos);
            state = state.getBlock().getActualState(state, world, pos);
            if (state.getBlock() != RPSBlocks.conjuredPulsarLight) return;

            if (Math.random() < 0.5)
                doNonSolidParticle(red, green, blue);

            if (particleCounter == 0)
                doNonSolidParticle(red, green, blue);

            particleCounter %= ++particleCounter % 10;

        } else {
            if (time > 0) time--;
            else if (time == 0) world.setBlockToAir(pos);
        }
    }

    private void doNonSolidParticle(float red, float green, float blue) {
        double w = 0.15f;
        double h = 0.05f;
        double x = pos.getX() + 0.5 + (Math.random() - 0.5) * w;
        double y = pos.getY() + 0.25 + (Math.random() - 0.5) * h;
        double z = pos.getZ() + 0.5 + (Math.random() - 0.5) * w;
        float s = 0.2f + (float) Math.random() * 0.1f;
        float m = 0.01f + (float) Math.random() * 0.015f;
        Psi.proxy.wispFX(this.world, x, y, z, red, green, blue, s, -m);
    }
}
