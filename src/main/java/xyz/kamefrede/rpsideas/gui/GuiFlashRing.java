package xyz.kamefrede.rpsideas.gui;

import xyz.kamefrede.rpsideas.network.MessageFlashSync;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.spell.SpellCompiler;

import java.util.UUID;

public class GuiFlashRing extends GuiProgrammer {

    public GuiFlashRing(ItemStack stack) {
        super(null, ItemSpellDrive.getSpell(stack));
    }

    @Override
    public void onSpellChanged(boolean nameOnly) {
        spell.uuid = UUID.randomUUID();
        PacketHandler.NETWORK.sendToServer(new MessageFlashSync(spell));
        onSelectedChanged();
        spellNameField.setFocused(nameOnly);

        if (!nameOnly ||
                (compiler.getError() != null && compiler.getError().equals(SpellCompilationException.NO_NAME)) ||
                spell.name.isEmpty()) {
            compiler = new SpellCompiler(spell);
        }
    }
}
