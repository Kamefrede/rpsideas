package com.kamefrede.rpsideas.gui;

import com.kamefrede.rpsideas.network.MessageFlashSync;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import com.kamefrede.rpsideas.util.RPSProgrammerMethodHandles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.spell.SpellCompiler;

import java.util.UUID;

public class GuiFlashRing extends GuiProgrammer {// TODO: 12/15/18 look at

    public GuiFlashRing(EntityPlayer player, ItemStack stack) {
        super(new FlashRingProgrammingWrapper(player, stack));
    }

    @Override
    public void onSpellChanged(boolean nameOnly) {
        programmer.spell.uuid = UUID.randomUUID();
        RPSPacketHandler.sendToServer(new MessageFlashSync(programmer.spell));
        onSelectedChanged();
        RPSProgrammerMethodHandles.getSpellNameField(this).setFocused(nameOnly);

        SpellCompiler compiler = RPSProgrammerMethodHandles.getSpellCompiler(this);

        if (!nameOnly ||
                (compiler.getError() != null && compiler.getError().equals(SpellCompilationException.NO_NAME)) ||
                programmer.spell.name.isEmpty()) {
            RPSProgrammerMethodHandles.setSpellCompiler(this, new SpellCompiler(programmer.spell));
        }
    }
}
