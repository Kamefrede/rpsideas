package com.github.kamefrede.rpsideas.items.components.botania;

import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.botania.IBlasterComponent;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.helpers.MiscHelpers;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IExtraVariantHolder;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.item.component.ItemCADComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Mod.EventBusSubscriber(Side.CLIENT)
public class ItemBlasterAssembly extends ItemCADComponent implements IBlasterComponent,IExtraVariantHolder {

    public ItemBlasterAssembly() {
        super(LibItems.CAD_ASSEMBLY);
        vazkii.psi.common.item.base.ModItems.cad.addPropertyOverride(new ResourceLocation(Reference.MODID, "clip"), ((stack, world, ent) -> ItemManaGun.hasClip(stack) ? 1f : 0f));
        setCreativeTab(RPSCreativeTab.INST);
    }


    public static final String[] VARIANTS = {
            "cad_assembly_mana_blaster"
    };

    public static final String[] CAD_MODELS = {
            "cad_blaster"
    };



    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getCADModel(ItemStack itemStack, ItemStack itemStack1) {
        return new ModelResourceLocation(new ResourceLocation(Reference.MODID, "cad_blaster"), "inventory");
    }

    protected void addTooltipTags(List<String> tooltip) {
        //TODO my modified addtooltip is a bit different
        addTooltipTag(true, tooltip, Reference.MODID + ".requirement.mana_cad");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
        tooltip.add(I18n.translateToLocal("rpsideas.misc.hold") + TextFormatting.AQUA + I18n.translateToLocal("rpsideas.misc.shift") + TextFormatting.RESET + I18n.translateToLocal("rpsideas.misc.info") );
        if(!GuiScreen.isShiftKeyDown()) return;
        tooltip.remove(1);

        addTooltipTags(tooltip);

        EnumCADComponent componentType = getComponentType(stack);
        String componentName = I18n.translateToLocal(componentType.getName());
        tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("rpsideas.componentType") + " " + TextFormatting.GRAY + componentName);

        for(EnumCADStat cadStat : EnumCADStat.values()) {
            if(cadStat.getSourceType() == componentType) {
                int statValue = getCADStatValue(stack, cadStat);
                String statValueString = statValue == -1 ? "∞" : String.valueOf(statValue);
                String statName = I18n.translateToLocal(cadStat.getName());
                tooltip.add(" " + TextFormatting.AQUA + statName + TextFormatting.GRAY + ": " + statValueString);
            }
        }
    }



    protected void addTooltipTag(boolean positiveEffect, List<String> tooltip, String descriptionTranslationKey, Object... descriptionFormatArgs) {
        String nameFormatted = I18n.translateToLocal(Reference.MODID + ".cadstat." + (positiveEffect ? "extra" : "downside"));

        if(descriptionFormatArgs == null) descriptionFormatArgs = new String[0];
        String descriptionFormatted = I18n.translateToLocalFormatted(descriptionTranslationKey, descriptionFormatArgs);

        TextFormatting color = positiveEffect ? TextFormatting.AQUA : TextFormatting.RED;

        tooltip.add(" " + color + nameFormatted + ": " + TextFormatting.GRAY + descriptionFormatted);
    }

    protected void addTooltip(List<String> tooltip) {
        //TODO my modified addtooltip is a bit different
        addTooltipTag(true, tooltip, Reference.MODID + ".requirement.mana_cad");
    }


    @Override
    public void registerStats() {
        addStat(EnumCADStat.EFFICIENCY, 0,80);
        addStat(EnumCADStat.POTENCY,0,  250);
    }


    private static final Pattern advancedMatcher = Pattern.compile("\\s+(?=\\(#\\d+\\))");

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent e) {
        ItemStack stack = e.getItemStack();
        Item item = stack.getItem();

        if(!(item instanceof ICAD)) return;
        ICAD icad = (ICAD) item;

        ItemStack assemblyStack = icad.getComponentInSlot(e.getItemStack(), EnumCADComponent.ASSEMBLY);
        if(assemblyStack.isEmpty() || !(assemblyStack.getItem() instanceof IBlasterComponent)) return;

        ItemStack lens = ItemManaGun.getLens(stack);
        if(!lens.isEmpty()) {
            String itemName = e.getToolTip().get(0);


            //Wtf is going on
            StringBuilder joined = new StringBuilder();
            String[] match = advancedMatcher.split(itemName);
            if(match.length > 1) {
                for(int i = 1; i < match.length; i++) {
                    if(i != 1) joined.append(' ');
                    joined.append(match[i]);
                }
            }

            itemName = match[0].trim() + ' ' + TextFormatting.RESET + '(' + TextFormatting.GREEN + lens.getDisplayName() + TextFormatting.RESET + ')' + joined.toString();

            if(GuiScreen.isShiftKeyDown()) {
                List<String> gunTooltip = new ArrayList<>();
                vazkii.botania.common.item.ModItems.manaGun.addInformation(stack, e.getEntityPlayer().world, gunTooltip, e.getFlags());
                e.getToolTip().addAll(2, gunTooltip);
            }

            e.getToolTip().set(0, itemName);
        }
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickItem e) {
        if(e.getEntityPlayer().isSneaking()) {
            ItemStack heldStack = e.getItemStack();
            EnumHand hand = e.getHand();
            World world = e.getWorld();

            if(!heldStack.isEmpty() && heldStack.getItem() instanceof ICAD) {
                ICAD icad = (ICAD) heldStack.getItem();
                boolean hasBlasterAssembly = icad.getComponentInSlot(heldStack, EnumCADComponent.ASSEMBLY).getItem() instanceof IBlasterComponent;
                if(hasBlasterAssembly && ItemManaGun.hasClip(heldStack)) {
                    ItemManaGun.rotatePos(heldStack);

                    MiscHelpers.emitSoundFromEntity(world, e.getEntityPlayer(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, .6f, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

                    if(world.isRemote) e.getEntityPlayer().swingArm(hand);

                    ItemsRemainingRenderHandler.set(ItemManaGun.getLens(heldStack), -2);

                    e.setCanceled(true);
                }
            }
        }
    }


    @Override
    public String[] getExtraVariants() {
        return CAD_MODELS;
    }

    @Override
    public String[] getVariants() {
        return VARIANTS;
    }


    @Override
    public String getModNamespace() {
        return Reference.MODID;
    }
}
