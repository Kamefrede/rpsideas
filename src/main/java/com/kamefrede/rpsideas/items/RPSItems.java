package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.blocks.RPSBlocks;
import com.kamefrede.rpsideas.compat.botania.BotaniaCompatItems;
import com.kamefrede.rpsideas.items.base.ItemModRod;
import com.kamefrede.rpsideas.items.components.*;
import com.kamefrede.rpsideas.items.flow.*;
import com.kamefrede.rpsideas.util.libs.RPSBlockNames;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.base.item.ItemModArmor;
import com.teamwizardry.librarianlib.features.base.item.ItemModSword;
import com.teamwizardry.librarianlib.features.base.item.ItemModTool;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSItems {
    public static final ItemMod wideBandSocket = new ItemWideCADSocket();
    public static final ItemMod flashRing = new ItemFlashRing();
    public static final ItemMod triggerSensor = new ItemTriggerSensor();
    public static final ItemMod bioticSensor = new ItemBioticSensor();
    public static final ItemMod unstableBattery = new ItemUnstableBattery();
    public static final ItemMod twinflowBattery = new ItemTwinflowBattery();
    public static final ItemMod shieldedBattery = new ItemShieldedBattery();
    public static final ItemMod creativeColorizer = new ItemCreativeSuspension();
    public static final ItemModRod psimetalRod = new ItemPsimetalRod(RPSItemNames.PSIMETAL_ROD);
    public static final ItemMod drainedColorizer = new ItemEmptyColorizer();
    public static final ItemMod liquidColorizer = new ItemLiquidColorizer();
    public static final ItemMod cyclicColorizer = new ItemCyclicColorizer();

    public static final ItemModTool ebonyPickaxe = new ItemFlowTool.Pickaxe(RPSItemNames.EBONY_PICKAXE, true);
    public static final ItemModTool ebonyShovel = new ItemFlowTool.Shovel(RPSItemNames.EBONY_SHOVEL, true);
    public static final ItemModTool ebonyAxe = new ItemFlowTool.Axe(RPSItemNames.EBONY_AXE, true);
    public static final ItemModSword ebonySword = new ItemFlowSword(RPSItemNames.EBONY_SWORD, true);
    public static final ItemModTool ivoryPickaxe = new ItemFlowTool.Pickaxe(RPSItemNames.IVORY_PICKAXE, false);
    public static final ItemModTool ivoryShovel = new ItemFlowTool.Shovel(RPSItemNames.IVORY_SHOVEL, false);
    public static final ItemModTool ivoryAxe = new ItemFlowTool.Axe(RPSItemNames.IVORY_AXE, false);
    public static final ItemModSword ivorySword = new ItemFlowSword(RPSItemNames.IVORY_SWORD, false);
    public static final ItemModArmor ebonyHelmet = new ItemFlowExosuit.Helmet(RPSItemNames.EBONY_HELMET, true);
    public static final ItemModArmor ebonyChest = new ItemFlowExosuit.Chestplate(RPSItemNames.EBONY_CHEST, true);
    public static final ItemModArmor ebonyLegs = new ItemFlowExosuit.Leggings(RPSItemNames.EBONY_LEGS, true);
    public static final ItemModArmor ebonyBoots = new ItemFlowExosuit.Boots(RPSItemNames.EBONY_BOOTS, true);
    public static final ItemModArmor ivoryHelmet = new ItemFlowExosuit.Helmet(RPSItemNames.IVORY_HELMET, false);
    public static final ItemModArmor ivoryChest = new ItemFlowExosuit.Chestplate(RPSItemNames.IVORY_CHEST, false);
    public static final ItemModArmor ivoryLegs = new ItemFlowExosuit.Leggings(RPSItemNames.IVORY_LEGS, false);
    public static final ItemModArmor ivoryBoots = new ItemFlowExosuit.Boots(RPSItemNames.IVORY_BOOTS, false);
    public static final ItemModRod ebonyRod = new ItemFlowRod(RPSItemNames.EBONY_ROD);
    public static final ItemModRod ivoryRod = new ItemFlowRod(RPSItemNames.IVORY_ROD);
    public static final ItemMod ebonyShears = new ItemFlowShears(RPSItemNames.EBONY_SHEARS);
    public static final ItemMod ivoryShears = new ItemFlowShears(RPSItemNames.IVORY_SHEARS);
    public static final ItemMod ebonyHoe = new ItemFlowHoe(RPSItemNames.EBONY_HOE);
    public static final ItemMod ivoryHoe = new ItemFlowHoe(RPSItemNames.IVORY_HOE);
    public static final ItemMod inlineCaster = new ItemInlineCaster();
    public static final ItemMod gaussRifle = new ItemGaussRifle();
    public static final ItemMod ivoryOverclocked = new ItemOverclockedIvoryAssembly();
    public static final ItemMod ivoryUnderclocked = new ItemUnderclockedIvoryAssembly();
    public static final ItemMod hyperthreadedCadCore = new ItemHyperthreadedCADCore();
    public static final ItemMod virtualthreadCadCore = new ItemVirtualThreadCADCore();
    public static final ItemMod optimizedCadCore = new ItemOptimizedCADCore();
    public static final ItemMod psiCuffs = new ItemPsiCuffs();
    public static final ItemMod psiCuffsKeys = new ItemPsiCuffKey();
    public static final ItemMod psimetalShield = new ItemPsimetalShield(RPSItemNames.PSIMETAL_SHIELD);
    // public static final ItemMod ivoryShield = new ItemFlowShield(RPSItemNames.IVORY_SHIELD);
    // public static final ItemMod ebonyShield = new ItemFlowShield(RPSItemNames.EBONY_SHIELD);

    public static final ItemMod psimetalHoe = new ItemPsimetalHoe(RPSItemNames.PSIMETAL_HOE);
    public static final ItemMod psimetalShears = new ItemPsimetalShears(RPSItemNames.PSIMETAL_SHEARS);
    public static final ItemMod gaussBullet = new ItemMod(RPSItemNames.ITEM_GAUSS_BULLET);
    public static final ItemMod cadMagazine = new ItemCADMagazine(RPSItemNames.SPELL_MAGAZINE);
    public static final ItemMod battlecaster = new ItemIntegratedBattlecaster();
    public static final ItemMod keypad = new ItemKeypad();
    //public static final ItemMod braceletCad = new ItemBraceletCAD();

    static {
        new BotaniaCompatItems().run();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void remapItem(RegistryEvent.MissingMappings<Item> e) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : e.getMappings())
            if (mapping.key.getPath().equals(RPSBlockNames.CAD_CASE))
                mapping.remap(Item.getItemFromBlock(RPSBlocks.cadCases[0]));
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void remapBlock(RegistryEvent.MissingMappings<Block> e) {
        for (RegistryEvent.MissingMappings.Mapping<Block> mapping : e.getMappings())
            if (mapping.key.getPath().equals(RPSBlockNames.CAD_CASE))
                mapping.remap(RPSBlocks.cadCases[0]);
    }
}
