package com.kamefrede.rpsideas.tiles;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.libs.RPSBlockNames;
import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileMod;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.spell.SpellCastEvent;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
@TileRegister(RPSBlockNames.TILE_PSI_DAMPENER)
public class TilePsiDampener extends TileMod {


    @Save
    private String minX;

    @Save
    private String minY;

    @Save
    private String minZ;

    @Save
    private String maxX;

    @Save
    private String maxY;

    @Save
    private String maxZ;


    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent ev) {
        World world = ev.context.caster.world;
        for (TileEntity te : world.loadedTileEntityList) {
            if (te instanceof TilePsiDampener) {
                //AxisAlignedBB axis = AxisAlignedBB
            }
            break;
        }


    }
}
