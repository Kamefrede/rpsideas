package com.kamefrede.rpsideas.network;

import com.kamefrede.rpsideas.items.ItemFlashRing;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;

import javax.annotation.Nonnull;

import static com.teamwizardry.librarianlib.features.kotlin.CommonUtilMethods.readTag;
import static com.teamwizardry.librarianlib.features.kotlin.CommonUtilMethods.writeTag;

@PacketRegister(Side.SERVER)
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

        ISpellAcceptor.acceptor(flashRing).setSpell(context.getServerHandler().player, spell);
    }

    @Override
    public void readCustomBytes(@NotNull ByteBuf buf) {
        spell = Spell.createFromNBT(readTag(buf));
    }

    @Override
    public void writeCustomBytes(@NotNull ByteBuf buf) {
        NBTTagCompound nbt = new NBTTagCompound();
        spell.writeToNBT(nbt);
        writeTag(buf, nbt);
    }
}
