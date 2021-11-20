package com.kamefrede.rpsideas.network;

import com.kamefrede.rpsideas.items.ItemKeypad;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import javax.annotation.Nonnull;

@PacketRegister(Side.SERVER)
public class MessageChangeSocketSlot extends PacketBase {

    public static final String TAG_KEYPAD_DIGIT = "rpsideas:keypad";

    @Save
    public int slot;

    public MessageChangeSocketSlot() {
        //NO-OP
    }

    public MessageChangeSocketSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public void handle(@Nonnull MessageContext context) {
        EntityPlayerMP player = context.getServerHandler().player;
        ItemStack mainhand = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemOffhand();
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        boolean isSneaking = player.isSneaking();

        if (isSneaking && offhand.getItem() instanceof ItemKeypad && slot <= 9)
            setDigit(player, slot, TAG_KEYPAD_DIGIT, data);
        if (!isSneaking && mainhand.getItem() instanceof ItemKeypad && slot <= 9)
            setDigit(player, slot, TAG_KEYPAD_DIGIT, data);

        if (isSneaking && ISocketableCapability.isSocketable(offhand))
            setSlot(ISocketableCapability.socketable(offhand), slot, offhand, data, player);
        if (!isSneaking && ISocketableCapability.isSocketable(mainhand))
            setSlot(ISocketableCapability.socketable(mainhand), slot, mainhand, data, player);

    }

    public void setSlot(ISocketableCapability socketable, int slot, ItemStack stack, PlayerDataHandler.PlayerData data, EntityPlayer player) {
        if (socketable.isSocketSlotAvailable(slot))
            socketable.setSelectedSlot(slot);
        if (data.loopcasting && data.loopcastHand != null)
            if (stack == player.getHeldItem(data.loopcastHand))
                data.stopLoopcast();
    }

    public void setDigit(EntityPlayer player, int slot, String tag, PlayerDataHandler.PlayerData data) {
        data.getCustomData().setInteger(tag, slot);
        data.save();
    }
}
