package com.kamefrede.rpsideas.network;

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.psi.api.cad.ICAD;

import javax.annotation.Nonnull;

@PacketRegister(Side.SERVER)
public class MessageCastOffHand extends PacketBase {

    public MessageCastOffHand() {
        //NO-OP
    }

    @Override
    public void handle(@Nonnull MessageContext context) {
        EntityPlayerMP player = context.getServerHandler().player;
        ItemStack stack = player.getHeldItemOffhand();
        if (!stack.isEmpty() && stack.getItem() instanceof ICAD)
            stack.useItemRightClick(player.world, player, EnumHand.OFF_HAND);
    }
}
