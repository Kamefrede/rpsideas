package xyz.kamefrede.rpsideas.asm;

import xyz.kamefrede.rpsideas.render.elytra.ICustomElytra;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * @author WireSegal
 * Created at 2:14 PM on 4/3/19.
 */
@SuppressWarnings("unused")
public class RPSAsmHooks {
    public static Item fakeElytra(Item item) {
        if (item instanceof ICustomElytra)
            return Items.ELYTRA;
        return item;
    }
}
