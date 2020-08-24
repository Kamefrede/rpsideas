package xyz.kamefrede.rpsideas.compat.tcon;

import xyz.kamefrede.rpsideas.items.RPSItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;

public class ModifierBattlecaster extends ModifierTrait {


    public ModifierBattlecaster() {
        super("battlecaster", 0xac5ae9);
        addRecipeMatch(new RecipeMatch.ItemCombination(1, new ItemStack(RPSItems.battlecaster)));
        aspects.clear();
        addAspects(new ModifierAspect.FreeModifierAspect(0), new RPSTinkersCompat.RPSModifierAspect(TinkerRegistry.getModifier("socketable")));
    }

    @Override
    public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean isCritical) {
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




}


