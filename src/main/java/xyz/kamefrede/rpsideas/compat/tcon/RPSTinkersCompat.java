package xyz.kamefrede.rpsideas.compat.tcon;

import xyz.kamefrede.rpsideas.RPSIdeas;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.tconstruct.library.book.TinkerBook;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tinkering.ITinkerable;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import vazkii.psi.api.cad.ISocketable;


public class RPSTinkersCompat {

    public static Object modSocketable, modBattlecaster;

    private static final ResourceLocation TINKERS_SOCKETABLE_TOOL = new ResourceLocation(RPSIdeas.MODID, "rpstinkerssocketable");

    static {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && Loader.isModLoaded("tconstruct")) {
            TinkerBook.INSTANCE.addRepository(new FileRepository(String.format("%s:%s", RPSIdeas.MODID, "book")));
        }
    }

    @Optional.Method(modid = "tconstruct")
    public static void init() {
        modSocketable = new ModifierPsionic();
        modBattlecaster = new ModifierBattlecaster();
        MinecraftForge.EVENT_BUS.register(RPSTinkersCompat.class);
    }


    @SubscribeEvent
    @Optional.Method(modid = "tconstruct")
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof ITinkerable) {
            event.addCapability(TINKERS_SOCKETABLE_TOOL, new TinkersToolCapability(event.getObject()));
        }

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    @Optional.Method(modid = "tconstruct")
    public static void addTooltip(ItemTooltipEvent event) {
        if (isPsionic(event.getItemStack())) {
            String componentName = I18n.format(ISocketable.getSocketedItemName(event.getItemStack(), "psimisc.none"));
            TooltipHelper.addToTooltip(event.getToolTip(), "psimisc.spellSelected", componentName);
        }
    }

    @Optional.Method(modid = "tconstruct")
    public static boolean isPsionic(ItemStack stack) {
        for (IModifier modifiers : TinkerUtil.getModifiers(stack)) {
            if (modifiers.getIdentifier().equals("socketable")) {
                return true;
            }
        }
        return false;
    }

    @Optional.Method(modid = "tconstruct")
    public static boolean isBattlecaster(ItemStack stack) {
        for (IModifier modifiers : TinkerUtil.getModifiers(stack)) {
            if (modifiers.getIdentifier().equals("battlecaster")) {
                return true;
            }
        }
        return false;
    }

    public static class RPSModifierAspect extends ModifierAspect {

        public final IModifier parent;

        public RPSModifierAspect(IModifier parent) {
            super(parent);
            this.parent = parent;
        }

        @Override
        public boolean canApply(ItemStack itemStack, ItemStack itemStack1) throws TinkerGuiException {
            if (!TinkerUtil.hasModifier(TagUtil.getTagSafe(itemStack), parent.getIdentifier())) {
                String error = I18n.format("rpsideas.gui.error.no_socketable");
                throw new TinkerGuiException(error);
            }
            return true;
        }

        @Override
        public void updateNBT(NBTTagCompound nbtTagCompound, NBTTagCompound nbtTagCompound1) {

        }
    }


}
