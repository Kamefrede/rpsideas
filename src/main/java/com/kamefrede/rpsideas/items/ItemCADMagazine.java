package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.crafting.IngredientCADComponent;
import com.kamefrede.rpsideas.gui.GuiHandler;
import com.kamefrede.rpsideas.items.base.ICADComponentAcceptor;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.spell.SpellCompiler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCADMagazine extends ItemMod implements ISocketable, ICADComponentAcceptor, ISpellSettable {
    public ItemCADMagazine(String name) {
        super(name);
        setMaxStackSize(1);
    }

    public static ItemStack getSocket(ItemStack stack) {
        NBTTagCompound nbt = NBTHelper.getCompound(stack, "socket");
        return nbt == null ? new ItemStack(ModItems.cadSocket) : new ItemStack(nbt);
    }

    public static ItemStack setSocket(ItemStack stack, ItemStack socket) {
        if (socket.isEmpty()) {
            NBTHelper.removeNBTEntry(stack, "socket");
        } else {
            NBTTagCompound nbt = new NBTTagCompound();
            socket.writeToNBT(nbt);
            NBTHelper.setCompound(stack, "socket", nbt);
        }
        return stack;
    }

    public static int getSocketSlots(ItemStack stack) {
        ItemStack socket = getSocket(stack);
        Item item = socket.getItem();
        if (item instanceof ICADComponent) {
            return ((ICADComponent) item).getCADStatValue(socket, EnumCADStat.SOCKETS);
        }
        return 0;
    }

    public static int getBandwidth(ItemStack stack) {
        ItemStack socket = getSocket(stack);
        Item item = socket.getItem();
        if (item instanceof ICADComponent) {
            return ((ICADComponent) item).getCADStatValue(socket, EnumCADStat.BANDWIDTH);
        }
        return 0;
    }

    public static boolean isValid(ItemStack stack, CompiledSpell spell) {
        int bandwidth = getBandwidth(stack);
        int spellBandwidth = spell.metadata.stats.getOrDefault(EnumSpellStat.BANDWIDTH, 0);
        return bandwidth == -1 || bandwidth >= spellBandwidth;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) for (ItemStack stack : IngredientCADComponent.defaults(EnumCADComponent.SOCKET)) {
            items.add(setSocket(new ItemStack(this), stack));
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(hand);
        if (tile instanceof TileProgrammer) {
            Spell spell = getSpell(stack);

            boolean enabled = ((TileProgrammer) tile).isEnabled();
            if (spell != null) {
                SpellCompiler compiled = new SpellCompiler(spell);
                if (compiled.getCompiledSpell().metadata.stats.get(EnumSpellStat.BANDWIDTH) != null && compiled.getCompiledSpell().metadata.stats.get(EnumSpellStat.BANDWIDTH) > getBandwidth(stack) && !worldIn.isRemote) {
                    player.sendStatusMessage(new TextComponentTranslation(RPSIdeas.MODID + ".misc.too_complex_bullet").setStyle(new Style().setColor(TextFormatting.RED)), false);
                } else if (!worldIn.isRemote) {
                    if (enabled && !((TileProgrammer) tile).playerLock.isEmpty()) {
                        player.sendStatusMessage(new TextComponentTranslation("psimisc.notYourProgrammer").setStyle(new Style().setColor(TextFormatting.RED)), false);
                        return EnumActionResult.SUCCESS;
                    }
                } else {
                    ((TileProgrammer) tile).playerLock = player.getName();
                }
                ((TileProgrammer) tile).spell = spell;
                ((TileProgrammer) tile).onSpellChanged();
                tile.markDirty();
                return EnumActionResult.SUCCESS;
            } else {
                if (!enabled || ((TileProgrammer) tile).playerLock.isEmpty() || ((TileProgrammer) tile).playerLock.equals(player.getName())) {
                    ((TileProgrammer) tile).spell = null;
                    ((TileProgrammer) tile).onSpellChanged();
                    worldIn.markBlockRangeForRenderUpdate(tile.getPos(), tile.getPos());
                    worldIn.playSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundCategory.PLAYERS, 0.5f, 1.0f, false);
                    return EnumActionResult.SUCCESS;
                }
            }

        }
        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            TooltipHelper.addToTooltip(tooltip, getTranslationKey(stack) + ".desc");
            TooltipHelper.addToTooltip(tooltip, getTranslationKey(stack) + ".desc1", Minecraft.getMinecraft().gameSettings.keyBindSneak.getDisplayName());

                    String socketName = TooltipHelper.local(getSocket(stack).getDisplayName());
                    String line = TextFormatting.GREEN.toString() + TooltipHelper.local(EnumCADComponent.SOCKET.getName()) + TextFormatting.GRAY.toString() + ": " + socketName;
                    tooltip.add(line);
                    int var12 = EnumCADStat.class.getEnumConstants().length;
                    for (int var13 = 0; var13 < var12 - 1; var13++) {
                        EnumCADStat stat = EnumCADStat.class.getEnumConstants()[var13];
                        if (stat.getSourceType() == EnumCADComponent.SOCKET) {
                            String shrt = stat.getName();
                            Item item = getSocket(stack).getItem();
                            if (item instanceof ICADComponent) {
                                int statVal = ((ICADComponent) item).getCADStatValue(getSocket(stack), stat);
                                String stratValStr = statVal == -1 ? "∞" : "" + statVal;
                                line = " " + TextFormatting.AQUA + TooltipHelper.local(shrt) + TextFormatting.GRAY + ": " + stratValStr;
                                if (!line.isEmpty()) {
                                    tooltip.add(line);
                                }
                            }
                        }
                    }
                    int slot = 0;
                    while (isSocketSlotAvailable(stack, slot)) {
                        String name = getSocketedItemName(stack, slot, null);
                        if (name != null) {
                            if (slot == getSelectedSlot(stack)) {
                                tooltip.add("| " + TextFormatting.WHITE + TextFormatting.BOLD + name);
                            } else
                                tooltip.add("| " + TextFormatting.WHITE + name);

                        }
                        slot++;
                    }
                }
        );
    }

    @Nullable
    public String getSocketedItemName(ItemStack stack, int slot, @Nullable String fallback) {
        if (!stack.isEmpty() && ISocketableCapability.isSocketable(stack)) {
            ISocketableCapability socketable = ISocketableCapability.socketable(stack);
            ItemStack item = socketable.getBulletInSocket(slot);
            return item.isEmpty() ? fallback : item.getDisplayName();
        } else {
            return fallback;
        }
    }

    @Override
    public void setPiece(ItemStack stack, EnumCADComponent type, NonNullList<ItemStack> piece) {
        if (type != EnumCADComponent.SOCKET || piece.isEmpty())
            return;
        setSocket(stack, piece.get(0));
    }

    public Spell getSpell(ItemStack stack) {
        return ItemSpellDrive.getSpell(getBulletInSocket(stack, getSelectedSlot(stack)));
    }

    @Override
    public NonNullList<ItemStack> getPiece(ItemStack stack, EnumCADComponent type) {
        if (type != EnumCADComponent.SOCKET)
            return NonNullList.create();
        return NonNullList.withSize(1, getSocket(stack));
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack mag = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote) {
            ItemStack cad = PsiAPI.getPlayerCAD(playerIn);
            if (!cad.isEmpty()) {
                if (playerIn.isSneaking()) {
                    ICAD cadItem = (ICAD) cad.getItem();
                    int socketsRequired = 0;
                    int bandwidthRequired = 0;
                    for (int i = 0; cadItem.isSocketSlotAvailable(cad, i); i++) {
                        ItemStack bullet = cadItem.getBulletInSocket(cad, i);
                        if (!bullet.isEmpty()) {
                            if (socketsRequired < i + 1)
                                socketsRequired = i + 1;

                            CompiledSpell spell = new CompiledSpell(ISpellAcceptor.acceptor(bullet).getSpell());
                            int bandwidth = spell.metadata.stats.get(EnumSpellStat.BANDWIDTH);
                            if (bandwidthRequired < bandwidth)
                                bandwidthRequired = bandwidth;
                        }
                    }

                    for (int i = 0; isSocketSlotAvailable(mag, i); i++) {
                        if (socketsRequired < i + 1 && !getBulletInSocket(mag, i).isEmpty())
                            socketsRequired = i + 1;
                    }

                    int cadSockets = cadItem.getStatValue(cad, EnumCADStat.SOCKETS);
                    int magSockets = getSocketSlots(mag);
                    int magBandwidth = getBandwidth(mag);

                    boolean bandwidthMatch = magBandwidth < 0 || magBandwidth >= bandwidthRequired;

                    if (cadSockets >= socketsRequired && magSockets >= socketsRequired && bandwidthMatch) {
                        NonNullList<ItemStack> tempInventory = NonNullList.create();
                        for (int i = 0; i < socketsRequired && isSocketSlotAvailable(mag, i); i++)
                            tempInventory.add(getBulletInSocket(mag, i));
                        for (int i = 0; i < socketsRequired && cadItem.isSocketSlotAvailable(cad, i); i++)
                            setBulletInSocket(mag, i, cadItem.getBulletInSocket(cad, i));

                        for (int i = 0; i < socketsRequired && i < tempInventory.size(); i++)
                            cadItem.setBulletInSocket(cad, i, tempInventory.get(i));

                        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                                PsiSoundHandler.bulletCreate,
                                SoundCategory.PLAYERS, 0.5f, 1f);
                        playerIn.getCooldownTracker().setCooldown(this, 10);
                    } else {
                        ITextComponent text = new TextComponentTranslation(RPSIdeas.MODID +
                                (bandwidthMatch ? ".misc.too_complex_bullet" : ".misc.slot_mismatch"));
                        text.getStyle().setColor(TextFormatting.RED);
                        playerIn.sendStatusMessage(text, true);
                        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                                PsiSoundHandler.compileError,
                                SoundCategory.PLAYERS, 0.5f, 1f);
                    }

                } else
                    playerIn.openGui(RPSIdeas.INSTANCE, GuiHandler.GUI_MAGAZINE, worldIn, 0, 0, 0);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, mag);
    }


    @Override
    public boolean acceptsPiece(ItemStack stack, EnumCADComponent type) {
        return type == EnumCADComponent.SOCKET;
    }


    @Override
    public boolean isSocketSlotAvailable(ItemStack stack, int slot) {
        return slot < getSocketSlots(stack);
    }

    @Override
    public ItemStack getBulletInSocket(ItemStack stack, int slot) {
        String name = "bullet" + slot;
        NBTTagCompound cmp = NBTHelper.getCompound(stack, name);
        return cmp == null ? ItemStack.EMPTY : new ItemStack(cmp);
    }

    @Override
    public void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
        String name = "bullet" + slot;
        NBTTagCompound cmp = new NBTTagCompound();
        bullet.writeToNBT(cmp);

        NBTHelper.setCompound(stack, name, cmp);
    }

    @Override
    public boolean isItemValid(ItemStack stack, int slot, ItemStack bullet) {
        if (!this.isSocketSlotAvailable(stack, slot)) {
            return false;
        } else if (!bullet.isEmpty() && ISpellAcceptor.isContainer(bullet)) {
            ISpellAcceptor acceptor = ISpellAcceptor.acceptor(bullet);
            if (!acceptor.containsSpell()) {
                return false;
            } else {
                Spell spell = acceptor.getSpell();
                SpellCompiler cmp = new SpellCompiler(spell);
                return isValid(stack, cmp.getCompiledSpell());
            }
        } else {
            return false;
        }
    }

    @Override
    public int getSelectedSlot(ItemStack stack) {
        return NBTHelper.getInt(stack, "selectedSlot", 0);
    }

    @Override
    public void setSelectedSlot(ItemStack stack, int slot) {
        NBTHelper.setInt(stack, "selectedSlot", 0);
    }

    @Override
    public void setSpell(EntityPlayer player, ItemStack stack, Spell spell) {
        int slot = this.getSelectedSlot(stack);
        ItemStack bullet = this.getBulletInSocket(stack, slot);
        SpellCompiler cmp = new SpellCompiler(spell);
        if (isValid(stack, cmp.getCompiledSpell())) {
            if (!player.world.isRemote) {
                player.sendStatusMessage(new TextComponentTranslation(RPSIdeas.MODID + ".misc.too_complex").setStyle(new Style().setColor(TextFormatting.RED)), false);
            }
        } else if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
            ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
            this.setBulletInSocket(stack, slot, bullet);
        }
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }
}
