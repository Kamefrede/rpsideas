package xyz.kamefrede.rpsideas.compat.tcon;

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static xyz.kamefrede.rpsideas.compat.tcon.RPSTinkersCompat.isPsionic;


public class TinkersToolCapability implements ISpellAcceptor, ISocketableCapability, IPsiBarDisplay, ICapabilityProvider {

    private static final String TAG_REGEN_TIME = "regenTime";
    private static final String TAG_BULLET_PREFIX = "bullet";
    private static final String TAG_SELECTED_SLOT = "selectedSlot";

    private static final String identifier = "socketable";

    public final ItemStack stack;

    public TinkersToolCapability(ItemStack stack) {
        this.stack = stack;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (isPsionic(stack)) {
            if (capability == ISocketableCapability.CAPABILITY)
                return ISocketableCapability.CAPABILITY.cast(this);
            if (capability == IPsiBarDisplay.CAPABILITY)
                return IPsiBarDisplay.CAPABILITY.cast(this);
            if (capability == ISpellAcceptor.CAPABILITY)
                return ISpellAcceptor.CAPABILITY.cast(this);
        }
        return null;


    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return isPsionic(stack) && (capability == ISocketableCapability.CAPABILITY || capability == IPsiBarDisplay.CAPABILITY || capability == ISpellAcceptor.CAPABILITY);
    }

    @Override
    public boolean shouldShow(IPlayerData data) {
        return true;
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

    @Override
    public boolean isSocketSlotAvailable(int slot) {
        return slot < Math.min(getLevel(stack), 3);
    }

    @Override
    public boolean showSlotInRadialMenu(int slot) {
        return isSocketSlotAvailable(slot - 1);
    }


    @Override
    public ItemStack getBulletInSocket(int slot) {
        String name = TAG_BULLET_PREFIX + slot;
        NBTTagCompound tag = NBTHelper.getCompound(stack, name);
        if (tag == null)
            return ItemStack.EMPTY;

        return new ItemStack(tag);
    }

    @Override
    public void setBulletInSocket(int slot, ItemStack bullet) {
        String name = TAG_BULLET_PREFIX + slot;
        NBTTagCompound cmp = new NBTTagCompound();

        if (!bullet.isEmpty())
            bullet.writeToNBT(cmp);


        NBTHelper.setCompound(stack, name, cmp);

    }

    @Override
    public int getSelectedSlot() {
        return NBTHelper.getInt(stack, TAG_SELECTED_SLOT, 0);
    }

    @Override
    public void setSelectedSlot(int slot) {
        NBTHelper.setInt(stack, TAG_SELECTED_SLOT, slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack bullet) {
        if (!isSocketSlotAvailable(slot))
            return false;

        if (bullet.isEmpty() || !ISpellAcceptor.hasSpell(bullet))
            return false;

        ISpellAcceptor container = ISpellAcceptor.acceptor(bullet);

        return stack.getItem() instanceof ICAD || !container.isCADOnlyContainer();
    }

    @Override
    public boolean canLoopcast(ItemStack stack) {
        return false;
    }

    public int getLevel(ItemStack itemStack) {
        return ModifierNBT.readTag(TinkerUtil.getModifierTag(itemStack, "socketable")).level;
    }

    public int getLevel(NBTTagCompound modifierTag) {
        ModifierNBT.IntegerNBT data = ModifierNBT.readInteger(modifierTag);
        return data.level;
    }
}
