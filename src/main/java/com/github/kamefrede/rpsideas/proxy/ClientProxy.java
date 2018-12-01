package com.github.kamefrede.rpsideas.proxy;

import com.github.kamefrede.rpsideas.blocks.BlockCADCase;
import com.github.kamefrede.rpsideas.blocks.BlockConjuredGravityBlock;
import com.github.kamefrede.rpsideas.blocks.PsionicBlocksCompat;
import com.github.kamefrede.rpsideas.compat.botania.BotaniaCompatItems;
import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.items.components.ItemBioticSensor;
import com.github.kamefrede.rpsideas.items.components.botania.ItemBlasterAssembly;
import com.github.kamefrede.rpsideas.render.RenderTileCADCase;
import com.github.kamefrede.rpsideas.tiles.TileCADCase;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.helpers.ClientHelpers;
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

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e){
        super.preInit(e);
        OBJLoader.INSTANCE.addDomain(Reference.MODID);
        MinecraftForge.EVENT_BUS.register(HUDHandler.class);

    }

    @Override
    public void init(FMLInitializationEvent e){
        super.init(e);
        ClientRegistry.bindTileEntitySpecialRenderer(TileCADCase.class, new RenderTileCADCase());

    }

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
        if(Loader.isModLoaded("botania")) setDefaultModel(BotaniaCompatItems.blaster);
        ModelLoader.setCustomStateMapper(PsionicBlocksCompat.cadCase, new StateMap.Builder().ignore(BlockCADCase.COLOR).build());
        for(int i = 0; i < 16; i++) {
            setDefaultModel(ModItems.cadCaseItem, i);
        }

    }

    private static void setDefaultModel(Item i) {
        setDefaultModel(i, 0);
    }

    private static void setDefaultModel(Item i, int damage) {
        ModelResourceLocation mrl = new ModelResourceLocation(i.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(i, damage, mrl);
    }





}
