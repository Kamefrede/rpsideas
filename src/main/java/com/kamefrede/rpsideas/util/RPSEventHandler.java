package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.capability.CapabilityTriggerSensor;
import com.kamefrede.rpsideas.effect.RPSPotions;
import com.kamefrede.rpsideas.items.base.ICooldownAssembly;
import com.kamefrede.rpsideas.items.base.IRegenerationBattery;
import com.kamefrede.rpsideas.network.MessageCastOffHand;
import com.kamefrede.rpsideas.network.MessageChangeSocketSlot;
import com.kamefrede.rpsideas.network.MessageCuffSync;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.RegenPsiEvent;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

import static vazkii.psi.common.item.ItemCAD.getRealCost;


@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSEventHandler {

    private static final String TAG_CUFFED = "rpsideas:cuffed";
    public static final ResourceLocation CAPABILITY_TRIGGER_SENSOR = new ResourceLocation(RPSIdeas.MODID, "capability_trigger_sensor");
    public static final String TAG_OVERFLOW = "rpsAssemblyOverflow";
    public static final String TAG_COOLDOWN = "rpsAssemblyCooldown";


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
    public static void attachTriggerCap(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof EntityPlayer)
            event.addCapability(CAPABILITY_TRIGGER_SENSOR, new CapabilityTriggerSensor((EntityPlayer)event.getObject()));
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
        syncCuffState(event.getTarget(), event.getEntityPlayer());
    }

    private static void syncCuffState(Entity target2, EntityPlayer entityPlayer) {
        if (target2 instanceof EntityPlayer) {
            EntityPlayer target = (EntityPlayer) target2;
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(target);
            boolean cuffed = false;
            if (data.getCustomData() != null && data.getCustomData().getBoolean(TAG_CUFFED)) {
                cuffed = true;
            }
            PacketHandler.NETWORK.sendTo(new MessageCuffSync(target.getEntityId(), cuffed), (EntityPlayerMP) entityPlayer);

        }
    }

    @SubscribeEvent
    public static void syncCuffedStateTwo(PlayerEvent.StopTracking event) {
        syncCuffState(event.getTarget(), event.getEntityPlayer());
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

    @SubscribeEvent
    public static void potionRemove(PotionEvent.PotionRemoveEvent event) {
        checkForAssemblyPotions(event.getEntityLiving(), event.getPotionEffect());
    }


    @SubscribeEvent
    public static void potionExpiry(PotionEvent.PotionExpiryEvent event) {
        checkForAssemblyPotions(event.getEntityLiving(), event.getPotionEffect());
    }

    private static void checkForAssemblyPotions(EntityLivingBase entityLiving, PotionEffect potionEffect) {
        if (entityLiving instanceof EntityPlayer)
            if (PotionMod.Companion.hasEffect(entityLiving, RPSPotions.affinity)) {
                if (potionEffect == PotionMod.Companion.getEffect(entityLiving, RPSPotions.affinity))
                    removeOverflowAndCooldown((EntityPlayer) entityLiving, potionEffect.getAmplifier());
            } else if (PotionMod.Companion.hasEffect(entityLiving, RPSPotions.burnout))
                if (potionEffect == PotionMod.Companion.getEffect(entityLiving, RPSPotions.burnout))
                    removeOverflowAndCooldown((EntityPlayer) entityLiving, potionEffect.getAmplifier());
    }

    public static void removeOverflowAndCooldown(EntityPlayer player, int amplifier) {
        PlayerDataHandler.PlayerData playerData = PlayerDataHandler.get(player);
        if (playerData.getCustomData().hasKey(TAG_OVERFLOW))
            playerData.getCustomData().removeTag(TAG_OVERFLOW);
        playerData.getCustomData().setInteger(TAG_COOLDOWN, 15 * amplifier * 20);
        playerData.save();
        if (player instanceof EntityPlayerMP)
            NetworkHandler.INSTANCE.sendTo(new MessageDataSync(playerData), (EntityPlayerMP) player);
    }

    @SubscribeEvent
    public static void tickDownCooldown(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayerDataHandler.PlayerData playerData = PlayerDataHandler.get(event.player);
            if (playerData.getCustomData().hasKey(TAG_COOLDOWN)) {
                playerData.getCustomData().setInteger(TAG_COOLDOWN, playerData.getCustomData().getInteger(TAG_COOLDOWN) - 1);
                if (playerData.getCustomData().getInteger(TAG_COOLDOWN) <= 0)
                    playerData.getCustomData().removeTag(TAG_COOLDOWN);
                playerData.save();
                if (event.player instanceof EntityPlayerMP)
                    NetworkHandler.INSTANCE.sendTo(new MessageDataSync(playerData), (EntityPlayerMP) event.player);
            }
        }
    }

}
