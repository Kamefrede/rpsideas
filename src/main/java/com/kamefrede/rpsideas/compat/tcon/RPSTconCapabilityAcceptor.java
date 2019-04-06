package com.kamefrede.rpsideas.compat.tcon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.kamefrede.rpsideas.compat.tcon.RPSTconCompat.hasSocketeer;

public class RPSTconCapabilityAcceptor implements ISpellAcceptor, ICapabilityProvider {

    private final ItemStack stack;

    public RPSTconCapabilityAcceptor(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ISpellAcceptor.CAPABILITY && hasSocketeer(stack);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == ISpellAcceptor.CAPABILITY && hasSocketeer(stack) ? ISpellAcceptor.CAPABILITY.cast(this) : null;
    }

    @Override
    public void setSpell(EntityPlayer player, Spell spell) {
        ISocketableCapability socketable = ISocketableCapability.socketable(stack);
        int slot = socketable.getSelectedSlot();
        ItemStack bullet = socketable.getBulletInSocket(slot);
        if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
            ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
            socketable.setBulletInSocket(slot, bullet);
        }

    }

    @Override
    public boolean castableFromSocket() {
        return false;
    }

    @Nullable
    @Override
    public Spell getSpell() {
        return null;
    }

    @Override
    public boolean containsSpell() {
        return false;
    }

    @Override
    public void castSpell(SpellContext context) {

    }

    @Override
    public double getCostModifier() {
        return 1;
    }

    @Override
    public boolean isCADOnlyContainer() {
        return false;
    }

    @Override
    public boolean requiresSneakForSpellSet() {
        return false;
    }
}
