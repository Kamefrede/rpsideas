package com.github.kamefrede.rpsideas.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.item.ItemSpellDrive;

import javax.annotation.Nullable;

public class FlashRingProgrammingWrapper extends TileProgrammer {
    public FlashRingProgrammingWrapper(EntityPlayer player, ItemStack stack) {
        super();

        this.player = player;
        this.stack = stack;

        spell = ItemSpellDrive.getSpell(stack);
        enabled = true;
    }

    EntityPlayer player;
    ItemStack stack;

    @Override
    public void onSpellChanged() {
        //No-op
    }

    @Override
    public boolean canPlayerInteract(EntityPlayer player) {
        return true;
    }

    //TODO What in the fuck is this hack for
    @Override
    public World getWorld() {
        return new World(null, null, player.world.provider, null, true) {
            @Override
            protected IChunkProvider createChunkProvider() {
                return null;
            }

            @Override
            protected boolean isChunkLoaded(int i, int i1, boolean b) {
                return false;
            }

            @Nullable
            @Override
            public TileEntity getTileEntity(BlockPos pos) {
                return FlashRingProgrammingWrapper.this;
            }
        };
    }
}
