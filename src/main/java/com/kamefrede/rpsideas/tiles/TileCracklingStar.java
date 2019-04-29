package com.kamefrede.rpsideas.tiles;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSBlockNames;
import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.Psi;

import java.util.HashSet;
import java.util.Set;

@TileRegister(RPSBlockNames.CONJURED_STAR_BLOCK)
public class TileCracklingStar extends TileModTickable {
    @Save
    private int time = -1;
    @Save
    private ItemStack colorizer = ItemStack.EMPTY;
    @Save
    private Set<Vec3d> rays = new HashSet<>();

    public void setTime(int time) {
        this.time = time;
    }

    public void setColorizer(ItemStack colorizer) {
        this.colorizer = colorizer;
    }

    public void addRay(Vec3d ray) {
        rays.add(ray);
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            int color = SpellHelpers.getColor(colorizer);

            float red = SpellHelpers.getR(color);
            float green = SpellHelpers.getG(color);
            float blue = SpellHelpers.getB(color);


            for (Vec3d ray : rays) {
                makeLine(ray, red, green, blue);
            }

            Psi.proxy.wispFX(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, red, green, blue, 0.25f);
        } else {
            if (time > 0) time--;
            else if (time == 0) world.setBlockToAir(pos);
        }
    }

    private void makeLine(Vec3d vec, float red, float green, float blue) {
        Vector3 start = Vector3.fromBlockPos(pos).add(.5 + (Math.random() - .5) * 0.05, .5 + (Math.random() - .5) * 0.05, .5 + (Math.random() - .5) * 0.05);
        double stepsPer = (Math.random() * 6d) + 0.0001;

        double length = Math.max(vec.length(), 0.0001);
        Vec3d ray = vec.scale(1 / length);
        int stepCount = (int) (length * stepsPer);

        for (int step = 0; step < stepCount; step++) {
            Vec3d ext = ray.scale(step / stepsPer);

            Psi.proxy.wispFX(world, start.x + ext.x, start.y + ext.y, start.z + ext.z, red, green, blue, 0.125f);
        }
    }
}
