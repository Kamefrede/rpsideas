package com.kamefrede.rpsideas.proxy;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.blocks.BlockCADCase;
import com.kamefrede.rpsideas.blocks.ModBlocks;
import com.kamefrede.rpsideas.compat.botania.BotaniaCompatItems;
import com.kamefrede.rpsideas.items.ItemFlowExosuit;
import com.kamefrede.rpsideas.items.ModItems;
import com.kamefrede.rpsideas.items.components.ItemBioticSensor;
import com.kamefrede.rpsideas.items.components.ItemLiquidColorizer;
import com.kamefrede.rpsideas.render.RenderTileCADCase;
import com.kamefrede.rpsideas.tiles.TileCADCase;
import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.psi.client.core.handler.HUDHandler;

import java.util.Objects;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {// TODO: 12/15/18 look at

    @SubscribeEvent
    public static void models(ModelRegistryEvent e) {
        setDefaultModel(ModItems.flashRing);
        setDefaultModel(ModItems.inlineCaster);
        setDefaultModel(ModItems.wideBandSocket);
        setDefaultModel(ModItems.unstableBattery);
        setDefaultModel(ModItems.twinflowBattery);
        setDefaultModel(ModItems.bioticSensor);
        setDefaultModel(ModItems.creativeBattery);
        setDefaultModel(ModItems.creativeCore);
        setDefaultModel(ModItems.creativeSocket);
        setDefaultModel(ModItems.psimetalRod);
        setDefaultModel(ModItems.drainedColorizer);
        setDefaultModel(ModItems.liquidColorizer);
        setDefaultModel(ModItems.ebonyPickaxe);
        setDefaultModel(ModItems.ebonyShovel);
        setDefaultModel(ModItems.ebonyAxe);
        setDefaultModel(ModItems.ebonySword);
        setDefaultModel(ModItems.ivoryPickaxe);
        setDefaultModel(ModItems.ivoryShovel);
        setDefaultModel(ModItems.ivoryAxe);
        setDefaultModel(ModItems.ivorySword);
        setDefaultModel(ModItems.ebonyHelmet);
        setDefaultModel(ModItems.ebonyChest);
        setDefaultModel(ModItems.ebonyLegs);
        setDefaultModel(ModItems.ebonyBoots);
        setDefaultModel(ModItems.ivoryHelmet);
        setDefaultModel(ModItems.ivoryChest);
        setDefaultModel(ModItems.ivoryLegs);
        setDefaultModel(ModItems.ivoryBoots);
        setDefaultModel(ModItems.ebonyRod);
        setDefaultModel(ModItems.ivoryRod);
        if (Loader.isModLoaded("botania")) setDefaultModel(BotaniaCompatItems.blaster);
        ModelLoader.setCustomStateMapper(ModBlocks.cadCase, new StateMap.Builder().ignore(BlockCADCase.COLOR).build());
        for (int i = 0; i < 16; i++) {
            setDefaultModel(ModItems.cadCaseItem, i);
        }

    }

    private static void setDefaultModel(Item i) {
        setDefaultModel(i, 0);
    }

    private static void setDefaultModel(Item i, int damage) {
        ModelResourceLocation mrl = new ModelResourceLocation(Objects.requireNonNull(i.getRegistryName()), "inventory");
        ModelLoader.setCustomModelResourceLocation(i, damage, mrl);
    }

    @SubscribeEvent
    public static void blockColors(ColorHandlerEvent.Block e) {
        BlockColors bc = e.getBlockColors();
        bc.registerBlockColorHandler((state, world, pos, layer) -> {
            if (layer == 1 && world != null && pos != null && state.getBlock() instanceof BlockCADCase) {
                return world.getBlockState(pos).getActualState(world, pos).getValue(BlockCADCase.COLOR).getColorValue();
            } else return 0xFFFFFF;
        }, ModBlocks.cadCase);
    }

    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item e) {
        ItemColors ic = e.getItemColors();
        ic.registerItemColorHandler((stack, layer) -> {
            if (layer == 1) {
                return ((ItemBioticSensor) ModItems.bioticSensor).getColor(stack);
            } else return 0xFFFFFF;
        }, ModItems.bioticSensor);
        ic.registerItemColorHandler((stack, layer) -> {
            if (layer == 1) {
                return EnumDyeColor.byMetadata(stack.getMetadata()).getColorValue();
            } else return 0xFFFFFF;
        }, ModItems.cadCaseItem);
        ic.registerItemColorHandler((stack, layer) -> {
            if (layer == 1) {
                return ((ItemLiquidColorizer) ModItems.liquidColorizer).getColor(stack);
            } else return 0xFFFFFF;
        }, ModItems.liquidColorizer);
        ic.registerItemColorHandler((stack, layer) -> {
                    if (layer == 1) {
                        return ClientHelpers.getFlowColor(stack);
                    } else return 0xFFFFFF;
                },
                ModItems.inlineCaster
        );
        ic.registerItemColorHandler((stack, layer) -> {
                    if (layer == 1) {
                        return ClientHelpers.getFlowColor(stack);
                    } else return 0xFFFFFF;
                },
                ModItems.inlineCaster,
                ModItems.ivorySword,
                ModItems.ivoryPickaxe,
                ModItems.ivoryAxe,
                ModItems.ivoryShovel,
                ModItems.ebonySword,
                ModItems.ebonyPickaxe,
                ModItems.ebonyAxe,
                ModItems.ebonyShovel
        );
        ic.registerItemColorHandler((stack, layer) -> ((ItemFlowExosuit) stack.getItem()).getColor(stack),
                ModItems.ivoryHelmet,
                ModItems.ebonyHelmet,
                ModItems.ivoryChest,
                ModItems.ebonyChest,
                ModItems.ivoryLegs,
                ModItems.ebonyLegs,
                ModItems.ivoryBoots,
                ModItems.ebonyBoots);
    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        OBJLoader.INSTANCE.addDomain(RPSIdeas.MODID);
        MinecraftForge.EVENT_BUS.register(HUDHandler.class);

    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ClientRegistry.bindTileEntitySpecialRenderer(TileCADCase.class, new RenderTileCADCase());

    }


}
