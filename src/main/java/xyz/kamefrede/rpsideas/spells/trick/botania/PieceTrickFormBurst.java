package xyz.kamefrede.rpsideas.spells.trick.botania;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.entity.botania.EntityPsiManaBurst;
import xyz.kamefrede.rpsideas.spells.base.SpellParams;
import xyz.kamefrede.rpsideas.spells.enabler.PieceComponentTrick;
import xyz.kamefrede.rpsideas.spells.enabler.botania.IManaTrick;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.common.spell.trick.entity.PieceTrickAddMotion;

import javax.annotation.Nonnull;

public class PieceTrickFormBurst extends PieceComponentTrick implements IManaTrick {

    private static final String[] req = new String[]{RPSIdeas.MODID + ".requirement.form_burst"};
    private static final int MANA_PER_BURST = 120;
    private SpellParam positionParam;
    private SpellParam rayParam;

    public PieceTrickFormBurst(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);
        rayParam = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.GREEN, false, false);

        SpellHelpers.addAllParams(this, positionParam, rayParam);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);

        meta.addStat(EnumSpellStat.COST, 400);
        meta.addStat(EnumSpellStat.POTENCY, 150);
    }

    @Override
    public Object executeIfAllowed(SpellContext context) throws SpellRuntimeException {

        Vector3 posVec = SpellHelpers.getVector3(this, context, positionParam, true, false);
        Vector3 rayVec = SpellHelpers.getVector3(this, context, rayParam, false, false);

        EntityPlayer player = context.caster;

        EntityManaBurst burst = formBurst(posVec, rayVec, PsiAPI.getPlayerCAD(context.caster), context);
        player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.manaBlaster, SoundCategory.PLAYERS, .6f, 1f);
        player.world.spawnEntity(burst);
        PieceTrickAddMotion.addMotion(context, burst, rayVec, 4F);


        return null;
    }

    @Nonnull
    public BurstProperties getBurstProps() {
        int maxMana = 120;
        int color = ICADColorizer.DEFAULT_SPELL_COLOR;
        int ticksBeforeManaLoss = 60;
        float manaLossPerTick = 4F;
        float motionModifier = 5F;
        float gravity = 0F;

        return new BurstProperties(maxMana, ticksBeforeManaLoss, manaLossPerTick, gravity, motionModifier, color);
    }

    public EntityManaBurst formBurst(Vector3 pos, Vector3 rayIn, ItemStack cad, SpellContext context) {
        BurstProperties props = getBurstProps();

        boolean lensHasColor = false;


        int color = props.color;
        ItemStack lens = ItemManaGun.getLens(cad);
        if (!lens.isEmpty() && lens.getItem() instanceof ILens) {
            ILens ilens = (ILens) lens.getItem();
            ilens.apply(lens, props);
            if (ilens.getLensColor(lens) != -1) {
                lensHasColor = true;
            }
        }


        EntityManaBurst burst;

        if (props.color == color && !lensHasColor) {
            ICAD icad = (ICAD) cad.getItem();
            ItemStack colorizer = icad.getComponentInSlot(cad, EnumCADComponent.DYE);

            burst = new EntityPsiManaBurst(context.caster, EnumHand.MAIN_HAND);
            ((EntityPsiManaBurst) burst).setColorizer(colorizer);
        } else
            burst = new EntityManaBurst(context.caster, EnumHand.MAIN_HAND);


        double yaw = -Math.atan2(rayIn.x, rayIn.z) * 180 / Math.PI - 180;
        double pitch = Math.asin(rayIn.y) * 180 / Math.PI;

        burst.setSourceLens(lens);
        if (ManaItemHandler.requestManaExact(cad, context.caster, props.maxMana, false)) {
            burst.setBurstSourceCoords(new BlockPos(0, -1, 0));
            burst.setLocationAndAngles(pos.x, pos.y, pos.z, (float) yaw + 180, (float) pitch);

            burst.setColor(props.color);
            burst.setMana(props.maxMana);
            burst.setStartingMana(props.maxMana);
            burst.setMinManaLoss(props.ticksBeforeManaLoss);
            burst.setManaLossPerTick(props.manaLossPerTick);
            burst.setGravity(props.gravity);
            burst.setMotion(0.01, 0.01, 0.01);


            return burst;
        }

        return null;
    }

    @Override
    public int manaDrain(SpellContext context) {
        return MANA_PER_BURST;
    }

    @Override
    public String[] requiredObjects() {
        return req;
    }
}
