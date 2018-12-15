package com.kamefrede.rpsideas.network;

import com.kamefrede.rpsideas.items.ItemFlashRing;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;

public class MessageFlashSync implements IMessage {// TODO: 12/15/18 look at

    Spell spell;

    @SuppressWarnings("unused")
    public MessageFlashSync() {
    }

    public MessageFlashSync(Spell spell) {
        this.spell = spell;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound nbt = new NBTTagCompound();
        spell.writeToNBT(nbt);
        ByteBufUtils.writeTag(buf, nbt);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        spell = Spell.createFromNBT(ByteBufUtils.readTag(buf));
    }

    public static class Handler implements IMessageHandler<MessageFlashSync, IMessage> {
        //TODO factor into general purpose utility method?
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
        public IMessage onMessage(MessageFlashSync m, MessageContext ctx) {
            EntityPlayerMP sender = ctx.getServerHandler().player;
            sender.getServerWorld().addScheduledTask(() -> {
                ItemStack flashRing = findFlashRing(sender);
                if (flashRing.isEmpty()) return;

                ((ISpellContainer) flashRing.getItem()).setSpell(sender, flashRing, m.spell);
            });

            return null;
        }
    }
}
