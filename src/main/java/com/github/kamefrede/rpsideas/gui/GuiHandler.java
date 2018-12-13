package com.github.kamefrede.rpsideas.gui;

import com.github.kamefrede.rpsideas.gui.cadcase.ContainerCADCase;
import com.github.kamefrede.rpsideas.gui.cadcase.GuiCADCase;
import com.github.kamefrede.rpsideas.gui.magazine.ContainerCADMagazine;
import com.github.kamefrede.rpsideas.gui.magazine.GUICADMagazine;
import com.github.kamefrede.rpsideas.items.ItemCADMagazine;
import com.github.kamefrede.rpsideas.items.blocks.ItemCADCase;
import com.github.kamefrede.rpsideas.items.ItemFlashRing;
import net.minecraftforge.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_CASE = 0;
    public static final int GUI_FLASH_RING = 1;
    public static final int GUI_MAGAZINE = 2;

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch(id) {
            case GUI_CASE: {
               ItemStack stack = getStack(player, ItemCADCase.class);
                if(!stack.isEmpty()) return new ContainerCADCase(player, stack);
                break;
            }
            case GUI_MAGAZINE: {
                ItemStack stack = getStack(player, ItemCADMagazine.class);
                if(!stack.isEmpty()) return new ContainerCADMagazine(player, stack);
            }

        }

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch(id) {
            case GUI_CASE: {
                ItemStack stack = getStack(player, ItemCADCase.class);
                if(!stack.isEmpty()) return new GuiCADCase(player, stack);
                break;
            }


            case GUI_FLASH_RING: {
                ItemStack stack = getStack(player, ItemFlashRing.class);
                if(!stack.isEmpty()) return new GuiFlashRing(player, stack);
            }
            case GUI_MAGAZINE: {
                ItemStack stack = getStack(player, ItemCADMagazine.class);
                if(!stack.isEmpty()) return new GUICADMagazine(player, stack);
            }
        }

        return null;
    }

    private static ItemStack getStack(EntityPlayer player, Class itemClass) {
        for(EnumHand hand : EnumHand.values()) {
            ItemStack heldStack = player.getHeldItem(hand);
            if(heldStack.isEmpty()) continue;

            Item heldItem = heldStack.getItem();

            if(itemClass.isInstance(heldItem)) return heldStack;

            if(heldItem instanceof ItemBlock) {
                ItemBlock ib = (ItemBlock) heldItem;
                if(itemClass.isInstance(ib)) return heldStack;
            }
        }

        return ItemStack.EMPTY;
    }
}
