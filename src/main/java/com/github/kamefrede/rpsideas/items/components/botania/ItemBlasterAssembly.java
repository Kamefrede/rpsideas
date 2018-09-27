package com.github.kamefrede.rpsideas.items.components.botania;

import com.github.kamefrede.rpsideas.items.base.ItemComponent;
import com.github.kamefrede.rpsideas.util.botania.EnumManaTier;
import com.github.kamefrede.rpsideas.util.botania.IBlasterComponent;
import com.github.kamefrede.rpsideas.util.ITrickEnablerComponent;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.botania.IManaTrick;
import com.github.kamefrede.rpsideas.util.helpers.MiscHelpers;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.util.ModelHandler;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ItemBlasterAssembly extends ItemComponent implements IBlasterComponent {

    public ItemBlasterAssembly() {
        //TODO find a home for those recipes.
        MinecraftForge.EVENT_BUS.register(this.getClass());

        vazkii.psi.common.item.base.ModItems.cad.addPropertyOverride(new ResourceLocation(Reference.MODID, "clip"), ((stack, world, ent) -> ItemManaGun.hasClip(stack) ? 1f : 0f));
    }



    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getCADModel(ItemStack itemStack, ItemStack itemStack1) {
        return new ModelResourceLocation(new ResourceLocation(Reference.MODID, "cad_blaster"), "inventory");
    }

    @Override
    protected void addTooltipTags(List<String> tooltip) {
        //TODO my modified addtooltip is a bit different
        addTooltipTag(true, tooltip, Reference.MODID + ".requirement.mana_cad");
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.EFFICIENCY, 80);
        addStat(EnumCADStat.POTENCY, 250);
    }

    @Override
    public EnableResult enablePiece(EntityPlayer player, ItemStack component, ItemStack cad, SpellContext context, Spell spell, int x, int y) {
        boolean isElven = ItemManaGun.hasClip(cad);
        EnumManaTier cadTier = isElven ? EnumManaTier.ALFHEIM : EnumManaTier.BASE;

        SpellPiece piece = spell.grid.gridData[x][y];

        if(piece instanceof IManaTrick) {
            IManaTrick manaPiece = (IManaTrick) piece;
            if(EnumManaTier.allowed(cadTier, manaPiece.tier())) {
                int manaDrain = manaPiece.manaDrain(context, x, y);
                return ITrickEnablerComponent.EnableResult.fromBoolean(ManaItemHandler.requestManaExact(cad, context.caster, manaDrain, true));
            }
        }

        return EnableResult.NOT_ENABLED;
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
            if(e.getFlags().isAdvanced()) {
                StringBuilder joined = new StringBuilder();
                String[] match = advancedMatcher.split(itemName);
                if(match.length > 1) {
                    for(int i = 1; i < match.length; i++) {
                        if(i != 1) joined.append(' ');
                        joined.append(match[i]);
                    }
                }

                itemName = match[0].trim() + ' ' + TextFormatting.RESET + '(' + TextFormatting.GREEN + lens.getDisplayName() + TextFormatting.RESET + ' ' + joined.toString();
            }

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
}
