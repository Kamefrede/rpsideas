package xyz.kamefrede.rpsideas.items.base;

import com.teamwizardry.librarianlib.features.base.ModCreativeTab;
import com.teamwizardry.librarianlib.features.base.item.IModItemProvider;
import com.teamwizardry.librarianlib.features.helpers.VariantHelper;
import kotlin.jvm.functions.Function1;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.teamwizardry.librarianlib.features.helpers.CommonUtilMethods.getCurrentModId;

/**
 * @author WireSegal
 * Created at 10:06 AM on 12/16/18.
 */
public abstract class ItemModRod extends ItemFishingRod implements IModItemProvider {

    private final String bareName;
    private final String modId;
    private final String[] variants;
    public ItemModRod(String name) {
        super();

        bareName = VariantHelper.toSnakeCase(name);
        modId = getCurrentModId();

        variants = VariantHelper.setupItem(this, bareName, new String[0], this::getCreativeTab);
    }

    @NotNull
    @Override
    public Item getProvidedItem() {
        return this;
    }

    @Nonnull
    @Override
    public Item setTranslationKey(@Nonnull String name) {
        VariantHelper.setTranslationKeyForItem(this, modId, name);
        return super.setTranslationKey(name);
    }

    @NotNull
    @Override
    public String[] getVariants() {
        return variants;
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        int dmg = stack.getItemDamage();
        String name = dmg >= variants.length ? this.bareName : variants[dmg];

        return "item." + modId + ":" + name;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab))
            for (int i = 0; i < variants.length; i++)
                items.add(new ItemStack(this, 1, i));
    }

    public ModCreativeTab getCreativeTab() {
        return ModCreativeTab.Companion.getDefaultTabs().get(modId);
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Function1<ItemStack, ModelResourceLocation> getMeshDefinition() {
        return null;
    }
}
