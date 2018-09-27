package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.blocks.PsionicBlocksCompat;
import com.github.kamefrede.rpsideas.compat.botania.BotaniaCompatItems;
import com.github.kamefrede.rpsideas.items.blocks.ItemCADCase;
import com.github.kamefrede.rpsideas.items.components.*;
import com.github.kamefrede.rpsideas.items.components.botania.ItemBlasterAssembly;
import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "wide_socket")
    public static final Item wideBandSocket = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "flash_ring")
    public static final Item flashRing = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "cad_case")
    public static final Item cadCaseItem = Items.AIR;


    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "inline_caster")
    public static final Item inlineCaster = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "liquid_colorizer")
    public static final Item liquidColorizer = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "empty_colorizer")
    public static final Item drainedColorizer = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "biotic_sensor")
    public static final Item bioticSensor = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "unstable_battery")
    public static final Item unstableBattery = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "twinflow_battery")
    public static final Item twinflowBattery = Items.AIR;








    public static void register(IForgeRegistry<Item> reg) {
        reg.register(createItem(new ItemFlashRing(), "flash_ring"));
        reg.register(createItem(new ItemInlineCaster(), "inline_caster"));
        reg.register(createItem(new ItemWideCADSocket(), "wide_socket"));
        reg.register(createItem(new ItemLiquidColorizer(), "liquid_colorizer"));
        reg.register(createItem(new ItemEmptyColorizer(), "empty_colorizer"));
        reg.register(createItem(new ItemBioticSensor(), "biotic_sensor"));
        reg.register(createItem(new ItemUnstableBattery(), "unstable_battery"));
        reg.register(createItem(new ItemTwinflowBattery(), "twinflow_battery"));

        reg.register(createItemBlock(new ItemCADCase(PsionicBlocksCompat.cadCase)));
        if(Loader.isModLoaded("botania")){ BotaniaCompatItems.register(reg);
        }
    }

    public static <I extends Item> I createItem(I item, String name) {
        return createItem(item, name, true);
    }

    public static <I extends Item> I createItem(I item, String name, boolean showInCreative) {
        ResourceLocation res = new ResourceLocation(Reference.MODID, name);

        item.setRegistryName(res);
        item.setUnlocalizedName(res.getResourceDomain() + "." + res.getResourcePath());

        if(showInCreative) item.setCreativeTab(RPSCreativeTab.INST);



        return item;
    }

    static <IB extends ItemBlock> IB createItemBlock(IB itemBlock) {
        return createItemBlock(itemBlock, true);
    }

    static <IB extends ItemBlock> IB createItemBlock(IB itemBlock, boolean showInCreative) {
        ResourceLocation res = itemBlock.getBlock().getRegistryName();

        itemBlock.setRegistryName(res);

        if(showInCreative) itemBlock.setCreativeTab(RPSCreativeTab.INST);


        return itemBlock;
    }
}
