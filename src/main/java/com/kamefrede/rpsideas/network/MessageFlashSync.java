package com.kamefrede.rpsideas.network;

import com.kamefrede.rpsideas.items.ItemFlashRing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;

import javax.annotation.Nonnull;
import java.io.IOException;

public class MessageFlashSync extends PacketBase {

    private Spell spell;

    @SuppressWarnings("unused")
    public MessageFlashSync() {
        // NO-OP
    }

    public MessageFlashSync(Spell spell) {
        this.spell = spell;
    }

    private static ItemStack findFlashRing(EntityPlayer player) {
        ItemStack mainHeld = player.getHeldItemMainhand();
        if (isFlashRing(mainHeld)) return mainHeld;

        ItemStack offHeld = player.getHeldItemOffhand();
        if (isFlashRing(offHeld)) return offHeld;

        return ItemStack.EMPTY;
    }

    private static boolean isFlashRing(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemFlashRing;
    }

    @Override
    public void handle(@Nonnull MessageContext context) {
        ItemStack flashRing = findFlashRing(context.getServerHandler().player);
        if (flashRing.isEmpty()) return;

        ((ISpellContainer) flashRing.getItem()).setSpell(context.getServerHandler().player, flashRing, spell);
    }

    @Override
    public void read(@Nonnull PacketBuffer buf) throws IOException {
        NBTTagCompound tag = buf.readCompoundTag();
        if (tag != null)
            spell = Spell.createFromNBT(tag);
    }

    @Override
    public void write(@Nonnull PacketBuffer buf) {
        NBTTagCompound nbt = new NBTTagCompound();
        spell.writeToNBT(nbt);
        buf.writeCompoundTag(nbt);
    }
}
