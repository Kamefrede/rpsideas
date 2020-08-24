package xyz.kamefrede.rpsideas.items.components;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.items.RPSItems;
import xyz.kamefrede.rpsideas.items.base.ICADComponentAcceptor;
import xyz.kamefrede.rpsideas.items.base.ItemComponent;
import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import kotlin.jvm.functions.Function2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADColorizer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemLiquidColorizer extends ItemComponent implements ICADColorizer, IItemColorProvider, ICADComponentAcceptor {

    public ItemLiquidColorizer() {
        super(RPSItemNames.LIQUID_COLORIZER);
    }

    public static int getColorFromStack(ItemStack stack) {
        if (!stack.hasTagCompound())
            return -1;
        return NBTHelper.getInt(stack, "color", -1);
    }

    public static ItemStack getInheriting(ItemStack stack) {
        if (!stack.hasTagCompound())
            return ItemStack.EMPTY;
        NBTTagCompound cmp = NBTHelper.getCompound(stack, "inheriting");
        if (cmp == null)
            return ItemStack.EMPTY;
        return new ItemStack(cmp);
    }

    public static void setInheriting(ItemStack stack, ItemStack inheriting) {
        if (inheriting.isEmpty()) {
            NBTHelper.removeNBTEntry(stack, "inheriting");
        } else {
            NBTTagCompound nbt = new NBTTagCompound();
            inheriting.writeToNBT(nbt);
            NBTHelper.setCompound(stack, "inheriting", nbt);
        }
    }

    @Override
    public void setPiece(ItemStack stack, EnumCADComponent type, NonNullList<ItemStack> piece) {
        if (type != EnumCADComponent.DYE || piece.isEmpty())
            return;
        setInheriting(stack, piece.get(0));
    }

    @Override
    public NonNullList<ItemStack> getPiece(ItemStack stack, EnumCADComponent type) {
        if (type != EnumCADComponent.DYE)
            return NonNullList.create();
        return NonNullList.withSize(1, getInheriting(stack));
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
        int itemColor = getColorFromStack(stack);
        if (!stack.isEmpty()) {
            ItemStack inheriting = getInheriting(stack);
            if (!inheriting.isEmpty() && inheriting.getItem() instanceof ICADColorizer) {
                int inheritColor = ((ICADColorizer) inheriting.getItem()).getColor(inheriting);
                if (itemColor == -1)
                    itemColor = inheritColor;
                else {
                    int itemR = (itemColor >> 16) & 0xff;
                    int itemG = (itemColor >> 8) & 0xff;
                    int itemB = itemColor & 0xff;
                    int inheritR = (inheritColor >> 16) & 0xff;
                    int inheritG = (inheritColor >> 8) & 0xff;
                    int inheritB = inheritColor & 0xff;

                    itemColor = ((itemR + inheritR) >> 1 << 16) |
                            ((itemG + inheritG) >> 1 << 8) |
                            (itemB + inheritB) / 2;
                }
            }

        }
        return (itemColor == -1) ? ICADColorizer.DEFAULT_SPELL_COLOR : itemColor;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        ItemStack inheriting = getInheriting(stack);
        if (!inheriting.isEmpty())
            addTooltipTagRaw(tooltip, TextFormatting.GREEN,
                    RPSIdeas.MODID + ".misc.color_inheritance", inheriting.getDisplayName());

        if (advanced.isAdvanced()) {
            int color = getColorFromStack(stack);
            if (color != -1) {
                String formattedNumber = String.format("%06X", color);
                if (formattedNumber.length() > 6)
                    formattedNumber = formattedNumber.substring(formattedNumber.length() - 6);
                addTooltipTagRaw(tooltip, TextFormatting.GREEN,
                        RPSIdeas.MODID + ".misc.color", "#" + formattedNumber);
            }
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
            ItemStack inheriting = getInheriting(held);
            if (!inheriting.isEmpty())
                held = inheriting;
            else
                held = new ItemStack(RPSItems.drainedColorizer);

            if (!world.isRemote)
                world.playSound(null, player.posX, player.posY, player.posZ,
                        SoundEvents.ITEM_BUCKET_EMPTY,
                        SoundCategory.PLAYERS, 0.5f, 1f);

            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        return new ActionResult<>(EnumActionResult.PASS, held);
    }
}
