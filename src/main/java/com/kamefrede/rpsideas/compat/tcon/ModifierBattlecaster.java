package com.kamefrede.rpsideas.compat.tcon;

import com.google.common.collect.ImmutableList;
import com.kamefrede.rpsideas.items.RPSItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifierDisplay;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;

import java.util.Iterator;
import java.util.List;

public class ModifierBattlecaster extends AbstractTrait implements IModifierDisplay {


    public ModifierBattlecaster() {
        super("battlecaster", 0xac5ae9);
        addRecipeMatch(new RecipeMatch.ItemCombination(1, new ItemStack(RPSItems.battlecaster)));
        addAspects(new ModifierAspect.FreeModifierAspect(0), new ModifierAspect.CategoryAnyAspect(Category.HARVEST, Category.TOOL, Category.WEAPON, Category.AOE), new RPSTinkersCompat.RPSModifierAspect(TinkerRegistry.getModifier("socketable")));
    }

    @Override
    public void applyEffect(NBTTagCompound nbtTagCompound, NBTTagCompound nbtTagCompound1) {
        super.applyEffect(nbtTagCompound, nbtTagCompound1);
    }

    @Override
    public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean isCritical) {
        super.onHit(tool, player, target, damage, isCritical);
        ISocketableCapability socketable = ISocketableCapability.socketable(tool);

        EntityPlayer caster = (EntityPlayer) player;
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(caster);
        ItemStack playerCad = PsiAPI.getPlayerCAD(caster);

        if (!playerCad.isEmpty()) {
            ItemStack bullet = socketable.getBulletInSocket(socketable.getSelectedSlot());
            ItemCAD.cast(caster.getEntityWorld(), caster, data, bullet, playerCad, 5, 10, 0.05F,
                    (SpellContext context) -> {
                        context.tool = tool;
                        context.attackedEntity = target;
                    });
        }
    }


    @Override
    public int getColor() {
        return 0xac5ae9;
    }

    @Override
    public List<List<ItemStack>> getItems() {
        ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();
        Iterator var2 = this.items.iterator();

        while (var2.hasNext()) {
            RecipeMatch rm = (RecipeMatch) var2.next();
            List<ItemStack> in = rm.getInputs();
            if (!in.isEmpty()) {
                builder.add(in);
            }
        }

        return builder.build();
    }


}


