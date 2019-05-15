package com.kamefrede.rpsideas.effect.base;

import com.teamwizardry.librarianlib.features.network.PacketHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

import javax.annotation.Nonnull;

public abstract class PotionPsiChange extends PotionModColorized {
    protected PotionPsiChange(String name, boolean badEffect, int color) {
        super(name, badEffect, color);
    }

    public static void addPsiToPlayer(EntityPlayer player, int psi, boolean sync) {
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        if (psi < 0)
            data.deductPsi(-psi, 40, sync, true);
        else {
            ItemStack cad = PsiAPI.getPlayerCAD(player);
            if (cad.isEmpty()) return;
            ICAD icad = (ICAD) cad.getItem();
            boolean icaditem = true;

            int overflow = icad.getStatValue(cad, EnumCADStat.OVERFLOW);
            int stored = icad.getStoredPsi(cad);

            if (stored < overflow) {
                icad.regenPsi(cad, Math.max(1, psi / 2));
                icaditem = false;
            }
            if (icaditem && data.availablePsi < data.totalPsi) {
                data.availablePsi = Math.min(data.totalPsi, data.availablePsi + psi);
                data.save();
                if (sync && player.world.isRemote && player instanceof EntityPlayerMP)
                    PacketHandler.NETWORK.sendTo(new MessageDataSync(data), (EntityPlayerMP) player);
            }
        }
    }

    public abstract int getAmpAmount();

    public abstract int getBaseAmount();

    @Override
    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
        if (entity instanceof EntityPlayer) {
            int amp = (byte) amplifier;
            addPsiToPlayer((EntityPlayer) entity, getBaseAmount() + amp * getAmpAmount(), true);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
