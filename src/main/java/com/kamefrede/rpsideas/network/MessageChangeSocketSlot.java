package com.kamefrede.rpsideas.network;

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import javax.annotation.Nonnull;

@PacketRegister(Side.SERVER)
public class MessageChangeSocketSlot extends PacketBase {

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
        if (!stack.isEmpty()) {
            ICAD cad = (ICAD) stack.getItem();
            int maxSlot = cad.getStatValue(stack, EnumCADStat.SOCKETS) - 1;
            if (slot <= maxSlot) {
                cad.setSelectedSlot(stack, slot);
            }
        }
        PlayerDataHandler.get(player).stopLoopcast();

    }
}
