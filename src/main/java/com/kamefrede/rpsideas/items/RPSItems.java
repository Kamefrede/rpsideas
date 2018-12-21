package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.ItemModRod;
import com.kamefrede.rpsideas.items.base.RPSItem;
import com.kamefrede.rpsideas.items.components.*;
import com.kamefrede.rpsideas.items.flow.*;
import com.kamefrede.rpsideas.util.libs.LibItemNames;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.arl.item.ItemMod;
import vazkii.arl.item.ItemModArmor;
import vazkii.arl.item.ItemModSword;
import vazkii.arl.item.ItemModTool;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSItems {
    public static final ItemMod wideBandSocket = new ItemWideCADSocket();
    public static final ItemMod flashRing = new ItemFlashRing();
    public static final ItemMod bioticSensor = new ItemBioticSensor();
    public static final ItemMod unstableBattery = new ItemUnstableBattery();
    public static final ItemMod twinflowBattery = new ItemTwinflowBattery();
    public static final ItemMod shieldedBattery = new ItemShieldedBattery();
    public static final ItemMod creativeBattery = new ItemCreativeBattery();
    public static final ItemMod creativeCore = new ItemCreativeCore();
    public static final ItemMod creativeSocket = new ItemCreativeSocket();
    public static final ItemModRod psimetalRod = new ItemPsimetalRod(LibItemNames.PSIMETAL_ROD);
    public static final ItemMod drainedColorizer = new ItemEmptyColorizer();
    public static final ItemMod liquidColorizer = new ItemLiquidColorizer();

    public static final ItemModTool ebonyPickaxe = new ItemFlowTool.Pickaxe(LibItemNames.EBONY_PICKAXE, true);
    public static final ItemModTool ebonyShovel = new ItemFlowTool.Shovel(LibItemNames.EBONY_SHOVEL, true);
    public static final ItemModTool ebonyAxe = new ItemFlowTool.Axe(LibItemNames.EBONY_AXE, true);
    public static final ItemModSword ebonySword = new ItemFlowSword(LibItemNames.EBONY_SWORD, true);
    public static final ItemModTool ivoryPickaxe = new ItemFlowTool.Pickaxe(LibItemNames.IVORY_PICKAXE, false);
    public static final ItemModTool ivoryShovel = new ItemFlowTool.Shovel(LibItemNames.IVORY_SHOVEL, false);
    public static final ItemModTool ivoryAxe = new ItemFlowTool.Axe(LibItemNames.IVORY_AXE, false);
    public static final ItemModSword ivorySword = new ItemFlowSword(LibItemNames.IVORY_SWORD, false);
    public static final ItemModArmor ebonyHelmet = new ItemFlowExosuit.Helmet(LibItemNames.EBONY_HELMET, true);
    public static final ItemModArmor ebonyChest = new ItemFlowExosuit.Chestplate(LibItemNames.EBONY_CHEST, true);
    public static final ItemModArmor ebonyLegs = new ItemFlowExosuit.Leggings(LibItemNames.EBONY_LEGS, true);
    public static final ItemModArmor ebonyBoots = new ItemFlowExosuit.Boots(LibItemNames.EBONY_BOOTS, true);
    public static final ItemModArmor ivoryHelmet = new ItemFlowExosuit.Helmet(LibItemNames.IVORY_HELMET, false);
    public static final ItemModArmor ivoryChest = new ItemFlowExosuit.Chestplate(LibItemNames.IVORY_CHEST, false);
    public static final ItemModArmor ivoryLegs = new ItemFlowExosuit.Leggings(LibItemNames.IVORY_LEGS, false);
    public static final ItemModArmor ivoryBoots = new ItemFlowExosuit.Boots(LibItemNames.IVORY_BOOTS, false);
    public static final ItemModRod ebonyRod = new ItemFlowRod(LibItemNames.EBONY_ROD);
    public static final ItemModRod ivoryRod = new ItemFlowRod(LibItemNames.IVORY_ROD);
    public static final ItemMod ebonyShears = new ItemFlowShears(LibItemNames.EBONY_SHEARS);
    public static final ItemMod ivoryShears = new ItemFlowShears(LibItemNames.IVORY_SHEARS);
    public static final ItemMod ebonyHoe = new ItemFlowHoe(LibItemNames.EBONY_HOE);
    public static final ItemMod ivoryHoe = new ItemFlowHoe(LibItemNames.IVORY_HOE);
    public static final ItemMod inlineCaster = new ItemInlineCaster();
    public static final ItemMod gaussRifle = new ItemGaussRifle();

    public static final ItemMod sniperBullet = new ItemSniperSpellBullet();
    public static final ItemMod psimetalHoe = new ItemPsimetalHoe(LibItemNames.PSIMETAL_HOE);
    public static final ItemMod psimetalShears = new ItemPsimetalShears(LibItemNames.PSIMETAL_SHEARS);
    public static final ItemMod gaussBullet = new RPSItem(LibItemNames.ITEM_GAUSS_BULLET);
    public static final ItemMod cadMagazine = new ItemCADMagazine(LibItemNames.SPELL_MAGAZINE);
    public static final ItemMod braceletCad = new ItemBraceletCAD();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerItems(RegistryEvent.Register<Item> e) {
        // NO-OP this is a hack
    }
}