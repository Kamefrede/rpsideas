package com.kamefrede.rpsideas.network;


import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSpamlessChat implements IMessage {// TODO: 12/15/18 look at

    private ITextComponent message;

    public MessageSpamlessChat() {
        message = new TextComponentString("");
    }

    public MessageSpamlessChat(ITextComponent message) {
        this.message = message;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(message));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf));
    }

    public static class Handler implements IMessageHandler<MessageSpamlessChat, IMessage> {
        private static int MAGIC = "rps spamless".hashCode();

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
