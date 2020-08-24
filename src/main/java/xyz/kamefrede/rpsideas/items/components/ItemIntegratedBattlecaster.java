package xyz.kamefrede.rpsideas.items.components;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.spell.PreSpellCastEvent;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author WireSegal
 * Created at 4:32 PM on 1/4/19.
 */
@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemIntegratedBattlecaster extends ItemMod {

    public static final String HAS_BATTLECASTER = RPSIdeas.MODID + "-HasBattlecaster";

    public ItemIntegratedBattlecaster() {
        super(RPSItemNames.INTEGRATED_BATTLECASTER);
    }

    public static void setHasBattlecaster(ItemStack stack, boolean hasBattlecaster) {
        if (canHaveBattlecaster(stack))
            NBTHelper.setBoolean(stack, HAS_BATTLECASTER, hasBattlecaster);
    }

    public static boolean canHaveBattlecaster(ItemStack stack) {
        return !stack.isEmpty() &&
                ISocketableCapability.isSocketable(stack) &&
                stack.getItem().isDamageable() &&
                !(stack.getItem() instanceof ItemSword) &&
                !(stack.getItem() instanceof ItemArmor);
    }

    public static boolean hasBattlecaster(ItemStack stack) {
        return canHaveBattlecaster(stack) && NBTHelper.getBoolean(stack, HAS_BATTLECASTER, false);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        TooltipHelper.addDynamic(tooltip, getTranslationKey(stack) + ".desc");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void addTooltip(ItemTooltipEvent event) {
        if (hasBattlecaster(event.getItemStack()))
            event.getToolTip().add(1, TooltipHelper.local(RPSIdeas.MODID + ".battlecaster_attached").replace("&", "\u00a7"));
    }

    @SubscribeEvent
    public static void cancelSpellCast(PreSpellCastEvent event) {
        if (event.getContext().attackedEntity == null && hasBattlecaster(event.getContext().tool)) {
            event.setCancellationMessage(null);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttackEntity(LivingAttackEvent event) {
        DamageSource source = event.getSource();

        if (event.getAmount() <= 0 ||
                !source.damageType.equals("player") ||
                source.isUnblockable() ||
                source.isDamageAbsolute() ||
                source.isExplosion() ||
                source.isMagicDamage() ||
                source.isFireDamage() ||
                source.isProjectile())
            return;

        EntityLivingBase affected = event.getEntityLiving();
        Entity attacker = source.getTrueSource();
        if (attacker != source.getImmediateSource())
            return;

        if (attacker instanceof EntityPlayer) {
            EntityPlayer caster = (EntityPlayer) attacker;

            ItemStack mainHand = caster.getHeldItemMainhand();

            if (hasBattlecaster(mainHand)) {
                ISocketableCapability socketable = ISocketableCapability.socketable(mainHand);

                PlayerDataHandler.PlayerData data = PlayerDataHandler.get(caster);
                ItemStack playerCad = PsiAPI.getPlayerCAD(caster);

                if (!playerCad.isEmpty()) {
                    ItemStack bullet = socketable.getBulletInSocket(socketable.getSelectedSlot());
                    ItemCAD.cast(caster.getEntityWorld(), caster, data, bullet, playerCad, 5, 10, 0.05F,
                            (SpellContext context) -> {
                                context.tool = mainHand;
                                context.attackedEntity = affected;
                            });
                }
            }
        }
    }
}
