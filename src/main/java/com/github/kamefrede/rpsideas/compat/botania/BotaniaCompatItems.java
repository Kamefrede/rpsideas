package com.github.kamefrede.rpsideas.compat.botania;

import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.items.components.botania.ItemBlasterAssembly;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class BotaniaCompatItems {
    public static ItemBlasterAssembly blaster;

    public static void register(IForgeRegistry<Item> reg) {
        blaster = ModItems.createItem(new ItemBlasterAssembly(), "cad_blaster");
        reg.register(blaster);
    }
}

