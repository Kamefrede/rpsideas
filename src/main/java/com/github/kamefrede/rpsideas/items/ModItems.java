package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.blocks.PsionicBlocksCompat;
import com.github.kamefrede.rpsideas.compat.botania.BotaniaCompatItems;
import com.github.kamefrede.rpsideas.items.blocks.ItemCADCase;
import com.github.kamefrede.rpsideas.items.components.*;
import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.arl.item.ItemMod;
import vazkii.arl.item.ItemModTool;

public class ModItems {

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.WIDE_SOCKET)
    public static final Item wideBandSocket = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.FLASH_RING)
    public static final Item flashRing = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.CAD_CASE)
    public static final Item cadCaseItem = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.INLINE_CASTER)
    public static final Item inlineCaster = Items.AIR;


    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.BIOTIC_SENSOR)
    public static final Item bioticSensor = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.UNSTABLE_BATTERY)
    public static final Item unstableBattery = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.TWINFLOW_BATTERY)
    public static final Item twinflowBattery = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.CREATIVE_BATTERY)
    public static final Item creativeBattery = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.CREATIVE_CORE)
    public static final Item creativeCore = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.CREATIVE_SOCKET)
    public static final Item creativeSocket = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.PSIMETAL_ROD)
    public static final Item psimetalRod = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.EMPTY_COLORIZER)
    public static final Item drainedColorizer = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.LIQUID_COLORIZER)
    public static final Item liquidColorizer = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID+ ":" + LibItems.EBONY_PICKAXE)
    public static final Item ebonyPickaxe = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.EBONY_SHOVEL)
    public static final Item ebonyShovel = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.EBONY_AXE)
    public static final Item ebonyAxe = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.EBONY_SWORD)
    public static final Item ebonySword = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID+ ":" + LibItems.IVORY_PICKAXE)
    public static final Item ivoryPickaxe = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.IVORY_SHOVEL)
    public static final Item ivoryShovel = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.IVORY_AXE)
    public static final Item ivoryAxe = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.IVORY_SWORD)
    public static final Item ivorySword = Items.AIR;


    public static ItemMod sniperBullet;

    public static ItemMod psimetalHoe;

    public static ItemMod psimetalShears;



    public static void preInit(){
        sniperBullet = new ItemSniperSpellBullet();
        psimetalHoe = new ItemPsionicHoe();
        psimetalShears = new ItemPsimetalShears();

    }




    public static void register(IForgeRegistry<Item> reg) {
        reg.register(createItem(new ItemFlashRing(), LibItems.FLASH_RING));
        reg.register(createItem(new ItemInlineCaster(), LibItems.INLINE_CASTER));
        reg.register(createItem(new ItemWideCADSocket(), LibItems.WIDE_SOCKET));
        reg.register(createItem(new ItemBioticSensor(), LibItems.BIOTIC_SENSOR));
        reg.register(createItem(new ItemUnstableBattery(), LibItems.UNSTABLE_BATTERY));
        reg.register(createItem(new ItemTwinflowBattery(), LibItems.TWINFLOW_BATTERY));
        reg.register(createItem(new ItemCreativeBattery(), LibItems.CREATIVE_BATTERY));
        reg.register(createItem(new ItemCreativeCore(), LibItems.CREATIVE_CORE));
        reg.register(createItem(new ItemCreativeSocket(), LibItems.CREATIVE_SOCKET));
        reg.register(createItem(new ItemPsimetalRod(), LibItems.PSIMETAL_ROD));
        reg.register(createItem(new ItemLiquidColorizer(), LibItems.LIQUID_COLORIZER));
        reg.register(createItem(new ItemEmptyColorizer(), LibItems.EMPTY_COLORIZER));
        reg.register(createItem(new ItemFlowTool.Pickaxe(true), LibItems.EBONY_PICKAXE));
        reg.register(createItem(new ItemFlowTool.Shovel(true), LibItems.EBONY_SHOVEL));
        reg.register(createItem(new ItemFlowTool.Axe(true), LibItems.EBONY_AXE));
        reg.register(createItem(new ItemFlowSword(true), LibItems.EBONY_SWORD));

        reg.register(createItem(new ItemFlowTool.Pickaxe(false), LibItems.IVORY_PICKAXE));
        reg.register(createItem(new ItemFlowTool.Shovel(false), LibItems.IVORY_SHOVEL));
        reg.register(createItem(new ItemFlowTool.Axe(false), LibItems.IVORY_AXE));
        reg.register(createItem(new ItemFlowSword(false), LibItems.IVORY_SWORD));

        reg.register(createItemBlock(new ItemCADCase(PsionicBlocksCompat.cadCase)));
        if(Loader.isModLoaded("botania")){
            BotaniaCompatItems.botaniaPreInit();
        }
    }

    public static <I extends Item> I createItem(I item, String name) {
        return createItem(item, name, true);
    }

    public static <I extends Item> I createItem(I item, String name, boolean showInCreative) {
        ResourceLocation res = new ResourceLocation(Reference.MODID, name);

        item.setRegistryName(res);
        item.setTranslationKey(res.getNamespace() + "." + res.getPath());

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
