package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.blocks.ModBlocks;
import com.kamefrede.rpsideas.compat.botania.BotaniaCompatItems;
import com.kamefrede.rpsideas.items.blocks.ItemCADCase;
import com.kamefrede.rpsideas.items.components.*;
import com.kamefrede.rpsideas.util.RPSCreativeTab;
import com.kamefrede.rpsideas.util.libs.LibItemNames;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.arl.item.ItemMod;

public class ModItems {// TODO: 12/15/18 look at

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.WIDE_SOCKET)
    public static final Item wideBandSocket = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.FLASH_RING)
    public static final Item flashRing = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.CAD_CASE)
    public static final Item cadCaseItem = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.INLINE_CASTER)
    public static final Item inlineCaster = Items.AIR;


    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.BIOTIC_SENSOR)
    public static final Item bioticSensor = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.UNSTABLE_BATTERY)
    public static final Item unstableBattery = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.TWINFLOW_BATTERY)
    public static final Item twinflowBattery = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.CREATIVE_BATTERY)
    public static final Item creativeBattery = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.CREATIVE_CORE)
    public static final Item creativeCore = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.CREATIVE_SOCKET)
    public static final Item creativeSocket = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.PSIMETAL_ROD)
    public static final Item psimetalRod = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EMPTY_COLORIZER)
    public static final Item drainedColorizer = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.LIQUID_COLORIZER)
    public static final Item liquidColorizer = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EBONY_PICKAXE)
    public static final Item ebonyPickaxe = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EBONY_SHOVEL)
    public static final Item ebonyShovel = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EBONY_AXE)
    public static final Item ebonyAxe = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EBONY_SWORD)
    public static final Item ebonySword = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.IVORY_PICKAXE)
    public static final Item ivoryPickaxe = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.IVORY_SHOVEL)
    public static final Item ivoryShovel = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.IVORY_AXE)
    public static final Item ivoryAxe = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.IVORY_SWORD)
    public static final Item ivorySword = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EBONY_HELMET)
    public static final Item ebonyHelmet = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EBONY_CHEST)
    public static final Item ebonyChest = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EBONY_LEGS)
    public static final Item ebonyLegs = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EBONY_BOOTS)
    public static final Item ebonyBoots = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.IVORY_HELMET)
    public static final Item ivoryHelmet = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.IVORY_CHEST)
    public static final Item ivoryChest = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.IVORY_LEGS)
    public static final Item ivoryLegs = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.IVORY_BOOTS)
    public static final Item ivoryBoots = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.EBONY_ROD)
    public static final Item ebonyRod = Items.AIR;

    @GameRegistry.ObjectHolder(RPSIdeas.MODID + ":" + LibItemNames.IVORY_ROD)
    public static final Item ivoryRod = Items.AIR;


    public static ItemMod sniperBullet;
    public static ItemMod psimetalHoe;
    public static ItemMod psimetalShears;
    public static ItemMod gaussRifle;
    public static ItemMod gaussBullet;
    public static ItemMod cadMagazine;
    public static ItemMod ebonyHoe;
    public static ItemMod ebonyShears;
    public static ItemMod ivoryHoe;
    public static ItemMod ivoryShears;
    public static ItemMod bracelet_cad;


    public static void preInit() {
        sniperBullet = new ItemSniperSpellBullet();
        psimetalHoe = new ItemPsionicHoe(LibItemNames.PSIMETAL_HOE);
        psimetalShears = new ItemPsimetalShears(LibItemNames.PSIMETAL_SHEARS);
        gaussRifle = new ItemGaussRifle();
        gaussBullet = new ItemGaussBullet();
        cadMagazine = new ItemCADMagazine(LibItemNames.SPELL_MAGAZINE);
        ivoryShears = new ItemPsimetalShears(LibItemNames.IVORY_SHEARS);
        ivoryHoe = new ItemPsionicHoe(LibItemNames.IVORY_HOE);
        ebonyHoe = new ItemPsionicHoe(LibItemNames.EBONY_HOE);
        ebonyShears = new ItemPsimetalShears(LibItemNames.EBONY_SHEARS);
        bracelet_cad = new ItemBraceletCAD();

    }


    public static void register(IForgeRegistry<Item> reg) {
        reg.register(createItem(new ItemFlashRing(), LibItemNames.FLASH_RING));
        reg.register(createItem(new ItemInlineCaster(), LibItemNames.INLINE_CASTER));
        reg.register(createItem(new ItemWideCADSocket(), LibItemNames.WIDE_SOCKET));
        reg.register(createItem(new ItemBioticSensor(), LibItemNames.BIOTIC_SENSOR));
        reg.register(createItem(new ItemUnstableBattery(), LibItemNames.UNSTABLE_BATTERY));
        reg.register(createItem(new ItemTwinflowBattery(), LibItemNames.TWINFLOW_BATTERY));
        reg.register(createItem(new ItemCreativeBattery(), LibItemNames.CREATIVE_BATTERY));
        reg.register(createItem(new ItemCreativeCore(), LibItemNames.CREATIVE_CORE));
        reg.register(createItem(new ItemCreativeSocket(), LibItemNames.CREATIVE_SOCKET));
        reg.register(createItem(new ItemPsimetalRod(), LibItemNames.PSIMETAL_ROD));
        reg.register(createItem(new ItemLiquidColorizer(), LibItemNames.LIQUID_COLORIZER));
        reg.register(createItem(new ItemEmptyColorizer(), LibItemNames.EMPTY_COLORIZER));
        reg.register(createItem(new ItemFlowTool.Pickaxe(true), LibItemNames.EBONY_PICKAXE));
        reg.register(createItem(new ItemFlowTool.Shovel(true), LibItemNames.EBONY_SHOVEL));
        reg.register(createItem(new ItemFlowTool.Axe(true), LibItemNames.EBONY_AXE));
        reg.register(createItem(new ItemFlowSword(true), LibItemNames.EBONY_SWORD));
        reg.register(createItem(new ItemPsimetalRod(), LibItemNames.EBONY_ROD));
        reg.register(createItem(new ItemPsimetalRod(), LibItemNames.IVORY_ROD));

        reg.register(createItem(new ItemFlowTool.Pickaxe(false), LibItemNames.IVORY_PICKAXE));
        reg.register(createItem(new ItemFlowTool.Shovel(false), LibItemNames.IVORY_SHOVEL));
        reg.register(createItem(new ItemFlowTool.Axe(false), LibItemNames.IVORY_AXE));
        reg.register(createItem(new ItemFlowSword(false), LibItemNames.IVORY_SWORD));

        reg.register(createItem(new ItemFlowExosuit.Helmet(true), LibItemNames.EBONY_HELMET));
        reg.register(createItem(new ItemFlowExosuit.Chestplate(true), LibItemNames.EBONY_CHEST));
        reg.register(createItem(new ItemFlowExosuit.Leggings(true), LibItemNames.EBONY_LEGS));
        reg.register(createItem(new ItemFlowExosuit.Boots(true), LibItemNames.EBONY_BOOTS));

        reg.register(createItem(new ItemFlowExosuit.Helmet(false), LibItemNames.IVORY_HELMET));
        reg.register(createItem(new ItemFlowExosuit.Chestplate(false), LibItemNames.IVORY_CHEST));
        reg.register(createItem(new ItemFlowExosuit.Leggings(false), LibItemNames.IVORY_LEGS));
        reg.register(createItem(new ItemFlowExosuit.Boots(false), LibItemNames.IVORY_BOOTS));

        reg.register(createItemBlock(new ItemCADCase(ModBlocks.cadCase)));
        if (Loader.isModLoaded("botania")) {
            BotaniaCompatItems.botaniaPreInit();
        }
    }

    public static <I extends Item> I createItem(I item, String name) {
        return createItem(item, name, true);
    }

    public static <I extends Item> I createItem(I item, String name, boolean showInCreative) {
        ResourceLocation res = new ResourceLocation(RPSIdeas.MODID, name);

        item.setRegistryName(res);
        item.setTranslationKey(res.getNamespace() + "." + res.getPath());

        if (showInCreative) item.setCreativeTab(RPSCreativeTab.INSTANCE);


        return item;
    }

    static <IB extends ItemBlock> IB createItemBlock(IB itemBlock) {
        return createItemBlock(itemBlock, true);
    }

    static <IB extends ItemBlock> IB createItemBlock(IB itemBlock, boolean showInCreative) {
        ResourceLocation res = itemBlock.getBlock().getRegistryName();

        itemBlock.setRegistryName(res);

        if (showInCreative) itemBlock.setCreativeTab(RPSCreativeTab.INSTANCE);


        return itemBlock;
    }
}
