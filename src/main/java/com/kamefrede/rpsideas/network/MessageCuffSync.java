package com.kamefrede.rpsideas.network;

import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import javax.annotation.Nonnull;

@PacketRegister(Side.CLIENT)
public class MessageCuffSync extends PacketBase {

    private static final String TAG_CUFFED = "rpsideas:cuffed";

    @Save
    private int entityId;

    @Save
    private boolean isCuffed;

    public MessageCuffSync() {
        //NO-OP
    }

    public MessageCuffSync(int entityId, boolean isCuffed) {
        this.entityId = entityId;
        this.isCuffed = isCuffed;
    }


    @Override
    public void handle(@Nonnull MessageContext context) {
        Entity player = LibrarianLib.PROXY.getClientPlayer().world.getEntityByID(entityId);
        if (player instanceof EntityPlayer) {
            EntityPlayer target = (EntityPlayer) player;
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(target);
            data.getCustomData().setBoolean(TAG_CUFFED, isCuffed);
        }
    }

}
