package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.blocks.BlockCADCase;
import com.kamefrede.rpsideas.blocks.RPSBlocks;
import com.kamefrede.rpsideas.items.base.ItemModRod;
import com.kamefrede.rpsideas.items.base.RPSItem;
import com.kamefrede.rpsideas.items.components.*;
import com.kamefrede.rpsideas.items.flow.*;
import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import com.kamefrede.rpsideas.util.libs.LibItemNames;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.arl.item.ItemModArmor;
import vazkii.arl.item.ItemModSword;
import vazkii.arl.item.ItemModTool;

@Mod.EventBusSubscriber
public class RPSItems {
    public static final ItemMod wideBandSocket = new ItemWideCADSocket();
    public static final ItemMod flashRing = new ItemFlashRing();
    public static final ItemMod bioticSensor = new ItemBioticSensor();
    public static final ItemMod unstableBattery = new ItemUnstableBattery();
    public static final ItemMod twinflowBattery = new ItemShieldedBattery();
    public static final ItemMod creativeBattery = new ItemCreativeBattery();
    public static final ItemMod creativeCore = new ItemCreativeCore();
    public static final ItemMod creativeSocket = new ItemCreativeSocket();
    public static final ItemModRod psimetalRod = new ItemPsimetalRod(LibItemNames.PSIMETAL_ROD);
    public static final ItemMod drainedColorizer = new ItemEmptyColorizer();
    public static final ItemMod liquidColorizer = new ItemLiquidColorizer();

    // TODO: 12/16/18 glow
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

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void blockColors(ColorHandlerEvent.Block e) {
        BlockColors bc = e.getBlockColors();
        bc.registerBlockColorHandler((state, world, pos, layer) -> {
            if (layer == 1 && world != null && pos != null && state.getBlock() instanceof BlockCADCase) {
                return world.getBlockState(pos).getActualState(world, pos).getValue(BlockCADCase.COLOR).getColorValue();
            } else return 0xFFFFFF;
        }, RPSBlocks.cadCase);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void itemColors(ColorHandlerEvent.Item e) {
        ItemColors ic = e.getItemColors();
        ic.registerItemColorHandler((stack, layer) -> {
            if (layer == 1) {
                return ((ItemBioticSensor) bioticSensor).getColor(stack);
            } else return 0xFFFFFF;
        }, bioticSensor);

        ic.registerItemColorHandler((stack, layer) -> {
            if (layer == 1) {
                return EnumDyeColor.byMetadata(stack.getMetadata()).getColorValue();
            } else return 0xFFFFFF;
        }, Item.getItemFromBlock(RPSBlocks.cadCase));

        ic.registerItemColorHandler((stack, layer) -> {
            if (layer == 1) {
                return ((ItemLiquidColorizer) liquidColorizer).getColor(stack);
            } else return 0xFFFFFF;
        }, liquidColorizer);

        ic.registerItemColorHandler((stack, layer) -> {
                    if (layer == 1) {
                        return ClientHelpers.getFlowColor(stack);
                    } else return 0xFFFFFF;
                }, inlineCaster,
                ivorySword,
                ivoryPickaxe,
                ivoryAxe,
                ivoryShovel,
                ebonySword,
                ebonyPickaxe,
                ebonyAxe,
                ebonyShovel
        );
        ic.registerItemColorHandler((stack, layer) -> ((ItemFlowExosuit) stack.getItem()).getColor(stack),
                ivoryHelmet,
                ebonyHelmet,
                ivoryChest,
                ebonyChest,
                ivoryLegs,
                ebonyLegs,
                ivoryBoots,
                ebonyBoots);
    }
}
