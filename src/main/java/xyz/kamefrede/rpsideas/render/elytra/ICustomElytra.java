package xyz.kamefrede.rpsideas.render.elytra;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author WireSegal
 * Created at 11:04 AM on 4/3/19.
 */
public interface ICustomElytra {
    @SideOnly(Side.CLIENT)
    ResourceLocation getElytraTexture(ItemStack stack);

    @SideOnly(Side.CLIENT)
    ModelCustomElytra getElytraModel(ItemStack stack);
}
