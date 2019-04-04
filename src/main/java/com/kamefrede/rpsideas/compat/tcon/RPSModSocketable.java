package com.kamefrede.rpsideas.compat.tcon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;

import static vazkii.psi.common.item.tool.IPsimetalTool.raytraceFromEntity;

public class RPSModSocketable extends ModifierTrait {
    @CapabilityInject(ISocketableCapability.class)
    private static Capability<RPSTconCapabilitySocketable> SOCKETABLE = null;

    protected static final int maxLevel = 4;

    public RPSModSocketable() {
        super("socketable", 0x5752CC, maxLevel, 0);
        addAspects(ModifierAspect.toolOnly);
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        //noop
    }

    @Override
    public String getLocalizedDesc() {
        return super.getLocalizedDesc();
    }

    @Override
    public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
        return super.getTooltip(modifierTag, detailed);
    }


    @Override
    public void beforeBlockBreak(ItemStack tool, BlockEvent.BreakEvent event) {
        super.beforeBlockBreak(tool, event);
        EntityPlayer player = event.getPlayer();
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        ItemStack playerCad = PsiAPI.getPlayerCAD(player);

        if (!playerCad.isEmpty()) {
            ItemStack bullet = ISocketableCapability.socketable(tool).getBulletInSocket(ISocketableCapability.socketable(tool).getSelectedSlot());
            ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                context.tool = tool;
                context.positionBroken = raytraceFromEntity(player.getEntityWorld(), player, false, player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue());
            });
        }
    }

}
