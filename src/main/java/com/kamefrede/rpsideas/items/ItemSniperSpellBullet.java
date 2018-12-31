package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.entity.EntitySniperProjectile;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.item.ItemSpellDrive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSniperSpellBullet extends ItemMod implements ISpellContainer {

    public static final String[] VARIANTS = {
            "spell_bullet_sniper",
            "spell_bullet_sniper_active",
    };
    private static final String TAG_SPELL = "spell";

    public ItemSniperSpellBullet() {
        super(RPSItemNames.SNIPER_BULLET, VARIANTS);
        setMaxStackSize(1);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return getSpell(stack) != null;
    }


    @Nonnull
    @Override
    public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
        return itemStack.copy();
    }


    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        if (!containsSpell(stack)) return super.getItemStackDisplayName(stack);

        NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_SPELL);
        if (cmp == null)
            return super.getItemStackDisplayName(stack);
        String name = cmp.getString(Spell.TAG_SPELL_NAME);
        if (name.isEmpty()) return super.getItemStackDisplayName(stack);

        return name;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab))
            for (int i = 0; i < getVariants().length; i++)
                if (i % 2 == 0)
                    subItems.add(new ItemStack(this, 1, i));
    }

    @Override
    public Spell getSpell(ItemStack itemStack) {
        return ItemSpellDrive.getSpell(itemStack);
    }

    @Override
    public boolean containsSpell(ItemStack itemStack) {
        return itemStack.getItemDamage() % 2 == 1;
    }

    @Override
    public void castSpell(ItemStack stack, SpellContext context) {
        switch (stack.getItemDamage()) {
            case 1:
                if (!context.caster.world.isRemote) {
                    EntitySniperProjectile proj = new EntitySniperProjectile(context.caster.world, context.caster);
                    ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
                    ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
                    proj.setInfo(context.caster, colorizer, stack);
                    proj.context = context;
                    proj.world.spawnEntity(proj);

                }
        }
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return containsSpell(stack) ? EnumRarity.EPIC : EnumRarity.COMMON;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            TooltipHelper.addToTooltip(tooltip, "psimisc.bulletType", TooltipHelper.local("psi.bulletType" + stack.getItemDamage() / 2));
            TooltipHelper.addToTooltip(tooltip, "psimisc.bulletCost", (int) (getCostModifier(stack) * 100));
        });
    }

    @Override
    public double getCostModifier(ItemStack itemStack) {
        return 1.25;
    }

    @Override
    public boolean isCADOnlyContainer(ItemStack stack) {
        return true;
    }

    @Override
    public void setSpell(EntityPlayer entityPlayer, ItemStack itemStack, Spell spell) {
        ItemSpellDrive.setSpell(itemStack, spell);

        if (!containsSpell(itemStack)) itemStack.setItemDamage(itemStack.getItemDamage() + 1);

    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack itemStack) {
        return false;
    }
}
