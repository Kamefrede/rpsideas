package com.kamefrede.rpsideas.network;

import com.kamefrede.rpsideas.items.ItemKeypad;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ISocketable;
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
        ItemStack stack = PsiAPI.getPlayerCAD(player);
        ItemStack mainhand = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemMainhand();
        if (mainhand.getItem() instanceof ItemKeypad || offhand.getItem() instanceof ItemKeypad) {
            if (slot <= 9) {
                PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
                data.getCustomData().setInteger(TAG_KEYPAD_DIGIT, slot);
                data.save();
                return;
            }
        } else if (mainhand.getItem() instanceof ISocketable || offhand.getItem() instanceof ISocketable) {
            if (offhand.getItem() instanceof ISocketable && player.isSneaking()) {
                setSlot((ISocketable) offhand.getItem(), slot, offhand);
                PlayerDataHandler.get(player).stopLoopcast();
                return;
            }
            ISocketable socketable = mainhand.getItem() instanceof ISocketable ? (ISocketable) mainhand.getItem() : (ISocketable) offhand.getItem();
            ItemStack stack1 = mainhand.getItem() instanceof ISocketable ? mainhand : offhand;
            setSlot(socketable, slot, stack1);
            PlayerDataHandler.get(player).stopLoopcast();
            return;

        } else if (!stack.isEmpty()) {
            ICAD cad = (ICAD) stack.getItem();
            if (cad.isSocketSlotAvailable(stack, slot))
                cad.setSelectedSlot(stack, slot);
        }
        PlayerDataHandler.get(player).stopLoopcast();

    }

    public void setSlot(ISocketable socketable, int slot, ItemStack stack) {
        if (socketable.isSocketSlotAvailable(stack, slot))
            socketable.setSelectedSlot(stack, slot);
    }
}
