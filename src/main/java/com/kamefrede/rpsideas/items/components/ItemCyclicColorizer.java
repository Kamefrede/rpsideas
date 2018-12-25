package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.ICADComponentAcceptor;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import kotlin.jvm.functions.Function2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.common.item.base.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


/**
 * @author WireSegal
 * Created at 11:06 AM on 12/25/18.
 */
public class ItemCyclicColorizer extends ItemComponent implements ICADColorizer, IItemColorProvider, ICADComponentAcceptor {
    public ItemCyclicColorizer() {
        super(RPSItemNames.CYCLIC_COLORIZER);
    }

    public static ItemStack getInheriting(ItemStack stack, boolean first) {
        if (!stack.hasTagCompound())
            return ItemStack.EMPTY;
        NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, first ? "first" : "last");
        if (cmp == null)
            return ItemStack.EMPTY;
        return new ItemStack(cmp);
    }

    public static void setInheriting(ItemStack stack, ItemStack inheriting, boolean first) {
        String tag = first ? "first" : "last";
        if (inheriting.isEmpty()) {
            ItemNBTHelper.removeEntry(stack, tag);
        } else {
            NBTTagCompound nbt = new NBTTagCompound();
            inheriting.writeToNBT(nbt);
            ItemNBTHelper.setCompound(stack, tag, nbt);
        }
    }

    @Override
    public void setPiece(ItemStack stack, EnumCADComponent type, NonNullList<ItemStack> piece) {
        if (type != EnumCADComponent.DYE || piece.size() < 2)
            return;
        setInheriting(stack, piece.get(0), true);
        setInheriting(stack, piece.get(1), false);
    }

    @Override
    public NonNullList<ItemStack> getPiece(ItemStack stack, EnumCADComponent type) {
        if (type != EnumCADComponent.DYE)
            return NonNullList.create();
        return NonNullList.from(ItemStack.EMPTY, getInheriting(stack, true), getInheriting(stack, false));
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            setInheriting(stack, new ItemStack(ModItems.cadColorizer, 1, EnumDyeColor.WHITE.getMetadata()), true);
            setInheriting(stack, new ItemStack(ModItems.cadColorizer, 1, EnumDyeColor.BLACK.getMetadata()), false);
            subItems.add(stack);
        }
    }

    @Override
    public boolean acceptsPiece(ItemStack stack, EnumCADComponent type) {
        return type == EnumCADComponent.DYE;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Function2<ItemStack, Integer, Integer> getItemColorFunction() {
        return (stack, layer) -> {
            if (layer == 1) {
                return getColor(stack);
            } else return -1;
        };
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColor(ItemStack stack) {
        if (!stack.isEmpty()) {
            ItemStack first = getInheriting(stack, true);
            ItemStack last = getInheriting(stack, false);

            // edge cases
            if ((last.isEmpty() || !(last.getItem() instanceof ICADColorizer)) && !first.isEmpty())
                last = first;
            if ((first.isEmpty() || !(first.getItem() instanceof ICADColorizer)) && !last.isEmpty())
                first = last;
            if (first.isEmpty() || !(first.getItem() instanceof ICADColorizer))
                return ICADColorizer.DEFAULT_SPELL_COLOR;

            int firstColor = ((ICADColorizer) first.getItem()).getColor(first);
            int lastColor = ((ICADColorizer) last.getItem()).getColor(last);

            float wave = (1 + MathHelper.sin(ClientTickHandler.ticksInGame * 0.1f)) / 2;

            int firstR = (firstColor >> 16) & 0xff;
            int firstG = (firstColor >> 8) & 0xff;
            int firstB = firstColor & 0xff;
            int lastR = (lastColor >> 16) & 0xff;
            int lastG = (lastColor >> 8) & 0xff;
            int lastB = lastColor & 0xff;

            return (int) (firstR * wave + lastR * (1 - wave)) << 16 |
                    (int) (firstG * wave + lastG * (1 - wave)) << 8 |
                    (int) (firstB * wave + lastB * (1 - wave));
        }
        return ICADColorizer.DEFAULT_SPELL_COLOR;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        ItemStack first = getInheriting(stack, true);
        ItemStack last = getInheriting(stack, false);

        if (!first.isEmpty() && !last.isEmpty()) {
            addTooltipTagRaw(tooltip, TextFormatting.GREEN,
                    RPSIdeas.MODID + ".misc.color_inheritance",
                    first.getDisplayName());
            addTooltipTagSubLineRaw(tooltip,
                    RPSIdeas.MODID + ".misc.color_inheritance",
                    last.getDisplayName());
        }

        addTooltipTag(tooltip, TextFormatting.DARK_AQUA,
                RPSIdeas.MODID + ".misc.info",
                RPSIdeas.MODID + ".misc.sneak_to_destroy",
                TextFormatting.AQUA + sneak.getDisplayName() + TextFormatting.GRAY);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);

        if (player.isSneaking()) {
            if (!world.isRemote) {
                EntityItem item = player.dropItem(getInheriting(held, false), false, true);
                if (item != null) {
                    item.posY -= player.getEyeHeight() / 2;
                    world.spawnEntity(item);
                }
            }
            held = getInheriting(held, true);

            if (!world.isRemote)
                world.playSound(null, player.posX, player.posY, player.posZ,
                        SoundEvents.BLOCK_GLASS_BREAK,
                        SoundCategory.PLAYERS, 0.5f, 1f);

            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        return new ActionResult<>(EnumActionResult.PASS, held);
    }
}
