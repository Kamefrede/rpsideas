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
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.RegenPsiEvent;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import static vazkii.psi.common.item.ItemCAD.getRealCost;


@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSEventHandler {

    private static final String TAG_CUFFED = "rpsideas:cuffed";

    public static boolean canCast(EntityPlayer player) {
        if (!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ICAD) {
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            ItemStack stack = player.getHeldItemOffhand();
            ICAD cad = (ICAD) player.getHeldItemOffhand().getItem();
            ItemStack bullet = cad.getBulletInSocket(stack, cad.getSelectedSlot(stack));
            if (!data.overflowed && data.getAvailablePsi() > 0 && !bullet.isEmpty() && ISpellAcceptor.isContainer(stack)) {
                ISpellAcceptor acceptor = ISpellAcceptor.acceptor(stack);
                if (acceptor.containsSpell()) {
                    Spell spell = acceptor.getSpell();
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
        if (event.getPlayerData().getCustomData().getBoolean("rpsideas:cuffed") && !event.getPlayer().world.isRemote) {
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
    public static void modifyRegen(RegenPsiEvent e) {
        ItemStack cad = e.getCad();

        if (!cad.isEmpty()) {
            ICAD cadItem = (ICAD) cad.getItem();
            ItemStack batteryStack = cadItem.getComponentInSlot(cad, EnumCADComponent.BATTERY);
            if (batteryStack.getItem() instanceof IRegenerationBattery)
                ((IRegenerationBattery) batteryStack.getItem())
                    .alterRegenerationBehavior(batteryStack, e);
        }
    }

}
