package com.kamefrede.rpsideas.gui;

import com.kamefrede.rpsideas.network.MessageFlashSync;
import com.kamefrede.rpsideas.util.RPSMethodHandles;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
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

    @Override
    public void onSpellChanged(boolean nameOnly) {
        programmer.spell.uuid = UUID.randomUUID();
        PacketHandler.NETWORK.sendToServer(new MessageFlashSync(programmer.spell));
        onSelectedChanged();
        RPSMethodHandles.getSpellNameField(this).setFocused(nameOnly);

        SpellCompiler compiler = RPSMethodHandles.getSpellCompiler(this);

        if (!nameOnly ||
                (compiler.getError() != null && compiler.getError().equals(SpellCompilationException.NO_NAME)) ||
                programmer.spell.name.isEmpty()) {
            RPSMethodHandles.setSpellCompiler(this, new SpellCompiler(programmer.spell));
        }
    }
}
