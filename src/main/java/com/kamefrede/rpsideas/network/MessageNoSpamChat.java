package com.kamefrede.rpsideas.network;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;
import java.io.IOException;

public class MessageNoSpamChat extends PacketBase {
    private static int MAGIC = "rps spamless".hashCode();

    private ITextComponent message;

    @SuppressWarnings("unused")
    public MessageNoSpamChat() {
        // NO-OP
    }

    public MessageNoSpamChat(ITextComponent message) {
        this.message = message;
    }

    @Override
    public void write(@Nonnull PacketBuffer buf) {
        buf.writeTextComponent(message);
    }

    @Override
    public void read(@Nonnull PacketBuffer buf) throws IOException {
        message = buf.readTextComponent();
    }

    @Override
    public void handle(@Nonnull MessageContext context) {
        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        chat.deleteChatLine(MAGIC);
        if (message != null)
            chat.printChatMessageWithOptionalDeletion(message, MAGIC);
    }
}
