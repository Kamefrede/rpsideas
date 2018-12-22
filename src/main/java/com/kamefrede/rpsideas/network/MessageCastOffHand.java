package com.kamefrede.rpsideas.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.item.ItemCAD;

import javax.annotation.Nonnull;
import java.io.IOException;

public class MessageCastOffHand extends PacketBase {


    public MessageCastOffHand(){
        //NO-OP
    }


    @Override
    public void handle(@Nonnull MessageContext context) {
        EntityPlayerMP player = context.getServerHandler().player;
        ItemStack stack = player.getHeldItemOffhand();
        if(!stack.isEmpty() && stack.getItem() instanceof ICAD){
            stack.getItem().onItemRightClick(context.getServerHandler().player.world, player, EnumHand.OFF_HAND);
        }
    }

    @Override
    public void read(@Nonnull PacketBuffer buf) throws IOException {
        //NO-OP
    }

    @Override
    public void write(@Nonnull PacketBuffer buf) throws IOException {
        //NO-OP
    }
}
