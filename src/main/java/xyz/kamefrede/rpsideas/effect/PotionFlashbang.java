package xyz.kamefrede.rpsideas.effect;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.util.RPSAnimationHandler;
import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class PotionFlashbang extends PotionMod {

    //https://github.com/TeamWizardry/Shotguns-And-Glitter/blob/master/src/main/java/com/teamwizardry/shotgunsandglitter/common/potions/PotionFlash.java

    public float opacity = 0f;

    private static int lastFlashState = -1;

    public PotionFlashbang() {
        super(RPSItemNames.FLASHBANG, true, 0xffffff);
        registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "c555d485-47c3-476b-984b-473ee53f9b1b", -0.15, 2);
        registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, "0bbdd459-e5a7-4699-ba6b-27538a3a3f25", -0.9, 1);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer player = minecraft.player;
        if (player != null) {
            PotionEffect effect = RPSPotions.flash.getEffect(player);
            int amp = effect == null ? -1 : effect.getAmplifier();
            if (amp != -1) {
                if (!player.getEntityData().hasKey("rpsideas:smooth"))
                    player.getEntityData().setBoolean("rpsideas:smooth", minecraft.gameSettings.smoothCamera);
                minecraft.gameSettings.smoothCamera = true;
            } else {
                if (player.getEntityData().hasKey("rpsideas:smooth")) {
                    minecraft.gameSettings.smoothCamera = player.getEntityData().getBoolean("rpsideas:smooth");
                    player.getEntityData().removeTag("rpsideas:smooth");
                }
            }

            if (lastFlashState != amp) {
                if (amp != -1) {
                    BasicAnimation<PotionFlashbang> animation = new BasicAnimation<>(RPSPotions.flash, "opacity");
                    animation.setEasing(Easing.easeInQuint);
                    animation.setTo(0.5f + 0.5f * ((amp + 1) / 4.0));
                    animation.setDuration(5);
                    RPSAnimationHandler.animate(animation);
                } else {
                    BasicAnimation<PotionFlashbang> animation = new BasicAnimation<>(RPSPotions.flash, "opacity");
                    animation.setEasing(Easing.easeOutBack);
                    animation.setTo(0f);
                    animation.setDuration(20);
                    RPSAnimationHandler.animate(animation);
                }
            }

            lastFlashState = amp;
        }
    }

    @Nonnull
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void playSound(PlaySoundEvent e) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer player = minecraft.player;
        if (player != null && RPSPotions.flash.hasEffect(player))
            e.setResultSound(null);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderOverlayAndCancelSubtitles(RenderGameOverlayEvent.Pre e) {
        if (e.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES)
            e.setCanceled(true);
        else if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            float opacity = Math.min(RPSPotions.flash.opacity, 1);

            if (opacity > 0) {
                ScaledResolution res = e.getResolution();
                double h = res.getScaledHeight();
                double w = res.getScaledWidth();

                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.color(1, 1, 1, 1);
                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(0, h, -90).color(1, 1, 1, opacity).endVertex();
                buffer.pos(w, h, -90).color(1, 1, 1, opacity).endVertex();
                buffer.pos(w, 0, -90).color(1, 1, 1, opacity).endVertex();
                buffer.pos(0, 0, -90).color(1, 1, 1, opacity).endVertex();
                Tessellator.getInstance().draw();
                GlStateManager.depthMask(true);
                GlStateManager.disableBlend();
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
            }
        }
    }
}
