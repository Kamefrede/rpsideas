package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.Psiam;
import com.github.kamefrede.rpsideas.crafting.factory.ModRecipes;
import com.github.kamefrede.rpsideas.gui.GuiHandler;
import com.github.kamefrede.rpsideas.items.base.ICadComponentAcceptor;
import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.helpers.ItemNBTHelpers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.arl.util.VanillaPacketDispatcher;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.ISpellSettable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.spell.SpellCompiler;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCADMagazine extends ItemMod implements ISocketable, ICadComponentAcceptor, ISpellSettable {
    public ItemCADMagazine(String name) {
        super(name);
        setMaxStackSize(1);
        setCreativeTab(RPSCreativeTab.INST);
    }

    public static ItemStack getSocket(ItemStack stack){
        NBTTagCompound nbt = ItemNBTHelper.getCompound(stack, "socket", true);
        return nbt == null ?  new ItemStack(ModItems.cadSocket) : new ItemStack(nbt);
    }

    public static ItemStack setSocket(ItemStack stack, ItemStack socket){
        if(socket.isEmpty()){
            ItemNBTHelpers.removeTag(stack, "socket");
        }
        else{
            NBTTagCompound nbt = new NBTTagCompound();
            socket.writeToNBT(nbt);
            ItemNBTHelper.setCompound(stack, "socket", nbt);
        }
        return stack;
    }

    public static int getSocketSlots(ItemStack stack){
        ItemStack socket = getSocket(stack);
        Item item = socket.getItem();
        if(item instanceof ICADComponent){
            return ((ICADComponent) item).getCADStatValue(socket, EnumCADStat.SOCKETS);
        }
        return 0;
    }

    public static int getBandwidth(ItemStack stack){
        ItemStack socket = getSocket(stack);
        Item item = socket.getItem();
        if(item instanceof ICADComponent){
            return ((ICADComponent) item).getCADStatValue(socket, EnumCADStat.BANDWIDTH);
        }
        return 0;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

        if(isInCreativeTab(tab)){
            for(ItemStack stack : ModRecipes.examplesockets){
                items.add(setSocket(new ItemStack(this), stack));
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(hand);
        if(tile != null && tile instanceof TileProgrammer) {
            Spell spell = getSpell(stack);
            boolean enabled = ((TileProgrammer) tile).isEnabled();
            if (spell != null) {
                SpellCompiler compiled = new SpellCompiler(spell);
                if (compiled.getCompiledSpell().metadata.stats.get(EnumSpellStat.BANDWIDTH) != null && compiled.getCompiledSpell().metadata.stats.get(EnumSpellStat.BANDWIDTH) > getBandwidth(stack) && !worldIn.isRemote) {
                    player.sendStatusMessage(new TextComponentTranslation(Reference.MODID + ".misc.too_complex_bullet").setStyle(new Style().setColor(TextFormatting.RED)), false);
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
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
                return EnumActionResult.SUCCESS;
            } else {
                if (!enabled || ((TileProgrammer) tile).playerLock.isEmpty() || ((TileProgrammer) tile).playerLock == player.getName()) {
                    ((TileProgrammer) tile).spell = spell;
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
        tooltipIfShift(tooltip, () -> {
            String socketName = local(getSocket(stack).getDisplayName());
            String line = TextFormatting.GREEN.toString() + local(EnumCADComponent.SOCKET.getName()) + TextFormatting.GRAY.toString() + ": " + socketName;
            addToTooltip(tooltip, line);
            int var12 = EnumCADStat.class.getEnumConstants().length;
            for(int var13 = 0; var13 < var12 -1; var13++ ){
               EnumCADStat stat = EnumCADStat.class.getEnumConstants()[var13];
               if(stat.getSourceType() == EnumCADComponent.SOCKET){
                   String shrt = stat.getName();
                   Item item = getSocket(stack).getItem();
                   if(item instanceof ICADComponent){
                       int statVal = ((ICADComponent) item).getCADStatValue(getSocket(stack), stat);
                       String stratValStr = statVal == -1 ? "∞" : "" + statVal;
                       line = " "  + TextFormatting.AQUA + local(shrt) + TextFormatting.GRAY + ": " + stratValStr;
                       if(!line.isEmpty()){
                           addToTooltip(tooltip, line);
                       }
                   }
               }
            }
            int slot = 0;
            while(isSocketSlotAvailable(stack, slot)){
                String name = getSocketedItemName(stack, slot, null);
                if(name != null){
                    if(slot == getSelectedSlot(stack)){
                        addToTooltip(tooltip, "| ${TextFormatting.WHITE}${TextFormatting.BOLD}$name");
                    } else
                        addToTooltip(tooltip, "| ${TextFormatting.WHITE}$name");

                }
                slot++;
            }
                }
        );
    }

    @Nullable
    public String getSocketedItemName(ItemStack stack, int slot,@Nullable String fallback){
        if(!stack.isEmpty() && stack.getItem() instanceof ISocketable){
            ISocketable socketable = (ISocketable)stack.getItem();
            ItemStack item = socketable.getBulletInSocket(stack, slot);
            return item.isEmpty() ? fallback : item.getDisplayName();
        } else{
            return fallback;
        }


    }

    @Override
    public ItemStack setPiece(ItemStack stack, EnumCADComponent type, ItemStack piece) {
        if(type != EnumCADComponent.SOCKET)
            return ItemStack.EMPTY;
        return setSocket(stack, piece);
    }

    public Spell getSpell(ItemStack stack){
        return ItemSpellDrive.getSpell(getBulletInSocket(stack, getSelectedSlot(stack)));
    }

    @Override
    public ItemStack getPiece(ItemStack stack, EnumCADComponent type) {
        if(type != EnumCADComponent.SOCKET)
            return ItemStack.EMPTY;
        return getSocket(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(!worldIn.isRemote && !PsiAPI.getPlayerCAD(playerIn).isEmpty()){
           playerIn.openGui(Psiam.INSTANCE, GuiHandler.GUI_MAGAZINE, worldIn, 0, 0, 0);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }



    @Override
    public boolean acceptsPiece(ItemStack stack, EnumCADComponent type) {
        return type == EnumCADComponent.SOCKET;
    }

    @Override
    public String getModNamespace() {
        return Reference.MODID;
    }

    @Override
    public boolean isSocketSlotAvailable(ItemStack stack, int slot) {
        return slot < getSocketSlots(stack);
    }

    @Override
    public ItemStack getBulletInSocket(ItemStack stack, int slot) {
        String name = "bullet" + slot;
        NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, name, true);
        return cmp == null ? ItemStack.EMPTY : new ItemStack(cmp);
    }

    @Override
    public void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
        String name = "bullet" + slot;
        NBTTagCompound cmp = new NBTTagCompound();
        bullet.writeToNBT(cmp);

        ItemNBTHelper.setCompound(stack, name, cmp);

    }

    @Override
    public int getSelectedSlot(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, "selectedSlot", 0);
    }

    @Override
    public void setSelectedSlot(ItemStack stack, int slot) {
        ItemNBTHelper.setInt(stack, "selectedSlot", 0);
    }

    @Override
    public void setSpell(EntityPlayer player, ItemStack stack, Spell spell) {
        int slot = this.getSelectedSlot(stack);
        ItemStack bullet = this.getBulletInSocket(stack, slot);
        SpellCompiler cmp =  new SpellCompiler(spell);
        if(cmp.getCompiledSpell().metadata.stats.get(EnumSpellStat.BANDWIDTH) != null &&  cmp.getCompiledSpell().metadata.stats.get(EnumSpellStat.BANDWIDTH) > getBandwidth(stack)){
            if(!player.world.isRemote){
                player.sendStatusMessage(new TextComponentTranslation(Reference.MODID + ".misc.too_complex").setStyle(new Style().setColor(TextFormatting.RED)), false);
            }
        } else if(!bullet.isEmpty() && bullet.getItem() instanceof ISpellSettable){
            ((ISpellSettable)bullet.getItem()).setSpell(player, bullet, spell);
            this.setBulletInSocket(stack, slot, bullet);
        }
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }
}
