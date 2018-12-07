package com.github.kamefrede.rpsideas.network;


import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSpamlessChat implements IMessage {
    public MessageSpamlessChat() {
        message = new TextComponentString("");
    }

    public MessageSpamlessChat(ITextComponent message) {
        this.message = message;
    }

    private ITextComponent message;

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(message));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf));
    }

    public static class Handler implements IMessageHandler<MessageSpamlessChat, IMessage> {
        private static int MAGIC = 69696969;

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(MessageSpamlessChat m, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                chat.deleteChatLine(MAGIC);
                chat.printChatMessageWithOptionalDeletion(m.message, MAGIC);
            });

            return null;
        }
    }

}