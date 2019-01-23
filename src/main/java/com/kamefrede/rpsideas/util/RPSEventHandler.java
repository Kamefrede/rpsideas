package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.ICooldownAssembly;
import com.kamefrede.rpsideas.items.base.IRegenerationBattery;
import com.kamefrede.rpsideas.network.MessageCastOffHand;
import com.kamefrede.rpsideas.network.MessageChangeSocketSlot;
import com.kamefrede.rpsideas.network.MessageCuffSync;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

import static vazkii.psi.common.item.ItemCAD.getRealCost;


@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSEventHandler {

    public static final int DEFAULT_REGEN_RATE = 25;
    public static final String REGEN_KEY = "rpsideasRegen";
    public static final String REGEN_BEFORE_KEY = "rpsideasRegenBefore";
    private static final String TAG_CUFFED = "rpsideas:cuffed";

    public static boolean canCast(EntityPlayer player) {
        if (!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ICAD) {
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            ItemStack stack = player.getHeldItemOffhand();
            ICAD cad = (ICAD) player.getHeldItemOffhand().getItem();
            ItemStack bullet = cad.getBulletInSocket(stack, cad.getSelectedSlot(stack));
            if (!data.overflowed && data.getAvailablePsi() > 0 && !bullet.isEmpty() && bullet.getItem() instanceof ISpellContainer) {
                ISpellContainer spellContainer = (ISpellContainer) bullet.getItem();
                if (spellContainer.containsSpell(bullet)) {
                    Spell spell = spellContainer.getSpell(bullet);
                    SpellContext context = new SpellContext().setPlayer(player).setSpell(spell);
                    if (context.isValid()) {
                        if (context.cspell.metadata.evaluateAgainst(stack)) {
                            int cost = getRealCost(stack, bullet, context.cspell.metadata.stats.get(EnumSpellStat.COST));
                            PreSpellCastEvent event = new PreSpellCastEvent(cost, 0, 0, 0, spell, context, player, data, stack, bullet);
                            if (MinecraftForge.EVENT_BUS.post(event)) {
                                return false;
                            }
                            cost = event.getCost();
                            if (cost == 0)
                                return true;
                            else return cost > 0 && data.availablePsi > cost;
                        }
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    public static void offhandCastClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        RPSKeybind offhandbinding = RPSKeybindHandler.offhandCast;
        if (offhandbinding.isPressed() && canCast(event.getEntityPlayer()))
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    public static void offhandClickClickEmtpty(PlayerInteractEvent.LeftClickEmpty event) {
        RPSKeybind offhandbinding = RPSKeybindHandler.offhandCast;
        if (offhandbinding.isPressed() && canCast(event.getEntityPlayer()))
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    public static void offhandAttackEnemy(AttackEntityEvent event) {
        RPSKeybind offhandbinding = RPSKeybindHandler.offhandCast;
        if (offhandbinding.isPressed() && canCast(event.getEntityPlayer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void keyDown(InputEvent.KeyInputEvent ev) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen == null) {
            RPSKeybind[] slotbindings = RPSKeybindHandler.keybinds;
            RPSKeybind offhandbinding = RPSKeybindHandler.offhandCast;
            if (offhandbinding.isPressed()) {
                PacketHandler.NETWORK.sendToServer(new MessageCastOffHand());
            }
            for (int i = 0; i < slotbindings.length; i++) {
                if (slotbindings[i].isPressed()) {
                    PacketHandler.NETWORK.sendToServer(new MessageChangeSocketSlot(i));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void assemblyCooldown(PreSpellCastEvent event) {
        ItemStack cad = event.getCad();
        ICAD icad = (ICAD) event.getCad().getItem();
        ItemStack assembly = icad.getComponentInSlot(cad, EnumCADComponent.ASSEMBLY);
        if (!assembly.isEmpty() && assembly.getItem() instanceof ICooldownAssembly) {
            ICooldownAssembly assembl = (ICooldownAssembly) assembly.getItem();
            event.setCooldown((int) (event.getCooldown() * assembl.getCooldownFactor(cad)));
        }
    }

    @SubscribeEvent
    public static void cuffedCastEvent(PreSpellCastEvent event) {
        if (event.getPlayerData().getCustomData().getBoolean("rpsideas:cuffed")) {
            event.setCancellationMessage("rpsideas.cancellation.cuffed");
            event.setCanceled(true);
            event.setCooldown(40);
            event.getPlayer().getCooldownTracker().setCooldown(event.getCad().getItem(), 40);
        }

    }

    @SubscribeEvent
    public static void syncCuffedState(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof EntityPlayer) {
            EntityPlayer target = (EntityPlayer) event.getTarget();
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(target);
            boolean cuffed = false;
            if (data.getCustomData() != null && data.getCustomData().getBoolean(TAG_CUFFED)) {
                cuffed = true;
            }
            PacketHandler.NETWORK.sendTo(new MessageCuffSync(target.getEntityId(), cuffed), (EntityPlayerMP) event.getEntityPlayer());

        }
    }


    @SubscribeEvent
    public static void updateRegenRate(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntityLiving();
            ItemStack cad = PsiAPI.getPlayerCAD(player);
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            if (data.getCustomData() != null) {

                int extraRegen = data.getCustomData().getInteger(REGEN_KEY);
                data.getCustomData().setInteger(REGEN_BEFORE_KEY, data.regen);

                int regenBefore = data.getCustomData().getInteger(REGEN_BEFORE_KEY);
                if (regenBefore == 0) regenBefore = DEFAULT_REGEN_RATE;

                int regenNow = DEFAULT_REGEN_RATE;
                if (regenBefore != extraRegen + DEFAULT_REGEN_RATE)
                    regenNow = regenBefore - extraRegen;


                extraRegen = 0;
                if (!cad.isEmpty()) {
                    ICAD cadItem = (ICAD) cad.getItem();
                    ItemStack battery = cadItem.getComponentInSlot(cad, EnumCADComponent.BATTERY);
                    if (battery.getItem() instanceof IRegenerationBattery)
                        extraRegen = ((IRegenerationBattery) battery.getItem()).getRegenerationValue(battery);
                }

                if (regenNow + extraRegen == data.regen)
                    return;

                data.getCustomData().setInteger(REGEN_KEY, extraRegen);
                data.regen = Math.max(DEFAULT_REGEN_RATE, regenNow + extraRegen);

                data.save();
                if (player instanceof EntityPlayerMP)
                    NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) player);
            }
        }
    }

}
