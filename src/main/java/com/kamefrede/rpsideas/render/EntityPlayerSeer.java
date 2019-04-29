package com.kamefrede.rpsideas.render;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.Psi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 * Created at 9:44 AM on 2/4/19.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = RPSIdeas.MODID)
public class EntityPlayerSeer extends AbstractClientPlayer {

    public static Vec3d offsetVector = Vec3d.ZERO;
    public static int timeLeft = 0;

    private final AbstractClientPlayer player;
    private final Vec3d offset;

    public EntityPlayerSeer(AbstractClientPlayer player, Vec3d offset) {
        super(player.world, player.getGameProfile());
        this.player = player;
        this.offset = offset;

        this.inventory = player.inventory;
        this.inventoryContainer = new ContainerPlayer(this.inventory, !world.isRemote, this);
        this.openContainer = this.inventoryContainer;

        this.hurtResistantTime = player.hurtResistantTime;
        this.hurtTime = player.hurtTime;
        this.maxHurtResistantTime = player.maxHurtResistantTime;
        this.maxHurtTime = player.maxHurtTime;
        this.attackedAtYaw = player.attackedAtYaw;
        this.deathTime = player.deathTime;

        this.distanceWalkedModified = player.distanceWalkedModified;
        this.distanceWalkedOnStepModified = player.distanceWalkedOnStepModified;
        this.prevDistanceWalkedModified = player.prevDistanceWalkedModified;

        this.posX = player.posX + offset.x;
        this.posY = player.posY + offset.y;
        this.posZ = player.posZ + offset.z;
        this.prevPosX = player.prevPosX + offset.x;
        this.prevPosY = player.prevPosY + offset.y;
        this.prevPosZ = player.prevPosZ + offset.z;
        this.lastTickPosX = player.lastTickPosX + offset.x;
        this.lastTickPosY = player.lastTickPosY + offset.y;
        this.lastTickPosZ = player.lastTickPosZ + offset.z;
        this.rotationPitch = player.rotationPitch;
        this.rotationYaw = player.rotationYaw;
        this.prevRotationPitch = player.prevRotationPitch;
        this.prevRotationYaw = player.prevRotationYaw;
        this.prevRotationYawHead = player.prevRotationYawHead;
    }

    public static boolean isClairvoyanceActive() {
        return timeLeft > 0 && offsetVector.lengthSquared() != 0;
    }

    public static Vec3d clairvoyanceVector() {
        return offsetVector;
    }

    @SubscribeEvent
    public static void renderHands(RenderHandEvent event) {
        if (isClairvoyanceActive())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void renderHud(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && isClairvoyanceActive())
            renderVignette(event.getResolution());
    }

    private static final ResourceLocation VIGNETTE_TEX_PATH = new ResourceLocation("textures/misc/vignette.png");

    private static void renderVignette(ScaledResolution scaledRes) {
        Minecraft mc = Minecraft.getMinecraft();

        int h = scaledRes.getScaledHeight();
        int w = scaledRes.getScaledWidth();

        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        int color = ICADColorizer.DEFAULT_SPELL_COLOR;

        ItemStack cadStack = PsiAPI.getPlayerCAD(mc.player);
        if (!cadStack.isEmpty())
            color = Psi.proxy.getColorForCAD(cadStack);

        GlStateManager.color(PsiRenderHelper.r(color) / 255F,
                PsiRenderHelper.g(color) / 255F,
                PsiRenderHelper.b(color) / 255F, 1F);

        mc.getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0, h, -90).tex(0, 1).endVertex();
        bufferbuilder.pos(w, h, -90).tex(1, 1).endVertex();
        bufferbuilder.pos(w, 0, -90).tex(1, 0).endVertex();
        bufferbuilder.pos(0, 0, -90).tex(0, 0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    @SubscribeEvent
    public static void preClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && timeLeft > 0)
            timeLeft--;
    }

    @SubscribeEvent
    public static void preRender(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity viewEntity = mc.getRenderViewEntity();
        if (event.phase == TickEvent.Phase.START) {
            if (isClairvoyanceActive()) {
                if (viewEntity == null && mc.player != null) viewEntity = mc.player;

                if (viewEntity instanceof AbstractClientPlayer && !(viewEntity instanceof EntityPlayerSeer))
                    mc.setRenderViewEntity(new EntityPlayerSeer((AbstractClientPlayer) viewEntity, clairvoyanceVector()));
            }
        } else if (viewEntity instanceof EntityPlayerSeer)
            mc.setRenderViewEntity(((EntityPlayerSeer) viewEntity).player);
    }


    @SubscribeEvent
    public static void preRenderPlayer(RenderPlayerEvent.Pre event) {
        if (!isClairvoyanceActive())
            return;

        Minecraft mc = Minecraft.getMinecraft();
        Entity viewEntity = mc.getRenderViewEntity();
        RenderManager manager = event.getRenderer().getRenderManager();
        if (viewEntity instanceof EntityPlayerSeer)
            manager.renderViewEntity = ((EntityPlayerSeer) viewEntity).player;
    }

    @SubscribeEvent
    public static void postRenderPlayer(RenderPlayerEvent.Post event) {
        if (!isClairvoyanceActive())
            return;

        Minecraft mc = Minecraft.getMinecraft();
        Entity viewEntity = mc.getRenderViewEntity();
        RenderManager manager = event.getRenderer().getRenderManager();
        if (viewEntity instanceof EntityPlayerSeer &&
                manager.renderViewEntity == ((EntityPlayerSeer) viewEntity).player)
            event.getRenderer().getRenderManager().renderViewEntity = viewEntity;
    }

    @Nonnull
    @Override
    public Vec3d getPositionEyes(float partialTicks) {
        return player.getPositionEyes(partialTicks).add(offset);
    }

    @Nonnull
    @Override
    public Vec3d getPositionVector() {
        return player.getPositionVector().add(offset);
    }

    @Nonnull
    @Override
    public Vec3d getLook(float partialTicks) {
        return player.getLook(partialTicks);
    }

    @Nonnull
    @Override
    public BlockPos getPosition() {
        return new BlockPos(player.posX + offset.x, player.posY + 0.5 + offset.y, player.posZ + offset.z);
    }

    @Override
    public float getFovModifier() {
        return player.getFovModifier();
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return player.isCreative();
    }

    @Nullable
    @Override
    public RayTraceResult rayTrace(double blockReachDistance, float partialTicks) {
        return player.rayTrace(blockReachDistance, partialTicks);
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return player.isEntityInsideOpaqueBlock();
    }

    @Override
    public boolean isInsideOfMaterial(@Nonnull Material materialIn) {
        return player.isInsideOfMaterial(materialIn);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return player.getEntityBoundingBox();
    }
}
