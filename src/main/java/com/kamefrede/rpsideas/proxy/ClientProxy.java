package com.kamefrede.rpsideas.proxy;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.render.RenderTileCADCase;
import com.kamefrede.rpsideas.tiles.TileCADCase;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = RPSIdeas.MODID)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ClientRegistry.bindTileEntitySpecialRenderer(TileCADCase.class, new RenderTileCADCase());
    }


}
