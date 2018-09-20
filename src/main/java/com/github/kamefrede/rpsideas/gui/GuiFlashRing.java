package com.github.kamefrede.rpsideas.gui;

import com.github.kamefrede.rpsideas.network.MessageFlashSync;
import com.github.kamefrede.rpsideas.network.RPSPacketHandler;
import com.github.kamefrede.rpsideas.util.RPSClientMethodHandles;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.spell.SpellCompiler;

import java.util.UUID;

public class GuiFlashRing extends GuiProgrammer {
    public GuiFlashRing(EntityPlayer player, ItemStack stack) {
        super(new FlashRingProgrammingWrapper(player, stack));
    }

    //Work around private fields
    private GuiTextField getTextField() {
        return RPSClientMethodHandles.getSpellNameField(this);
    }

    private SpellCompiler getSpellCompiler() {
        return RPSClientMethodHandles.getSpellCompiler(this);
    }

    private void setSpellCompiler(SpellCompiler compiler) {
        RPSClientMethodHandles.setSpellCompiler(this, compiler);
    }

    @Override
    public void onSpellChanged(boolean nameOnly) {
        //A bunch of misc shxt
        programmer.spell.uuid = UUID.randomUUID();
        RPSPacketHandler.sendToServer(new MessageFlashSync(programmer.spell));
        onSelectedChanged();
        getTextField().setFocused(nameOnly);

        SpellCompiler compiler = getSpellCompiler();

        //This is... Very strange. But super does it too so ¯\_(ツ)_/¯
        if(!nameOnly || compiler.getError() != null && compiler.getError() == SpellCompilationException.NO_NAME || programmer.spell.name.isEmpty()) {
            setSpellCompiler(new SpellCompiler(programmer.spell));
        }
    }
}
