package com.kamefrede.rpsideas.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import javax.annotation.Nonnull;
import java.io.IOException;

public class MessageChangeSocketSlot extends PacketBase {

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
        if(!stack.isEmpty()){
            ICAD cad = (ICAD)stack.getItem();
            int maxSlot = cad.getStatValue(stack, EnumCADStat.SOCKETS) - 1;
            if(slot <= maxSlot){
                cad.setSelectedSlot(stack, slot);
            }
        }
        PlayerDataHandler.get(player).stopLoopcast();

    }

    @Override
    public void read(@Nonnull PacketBuffer buf) throws IOException {
        slot = buf.readInt();
    }

    @Override
    public void write(@Nonnull PacketBuffer buf) throws IOException {
        buf.writeInt(slot);
    }
}
