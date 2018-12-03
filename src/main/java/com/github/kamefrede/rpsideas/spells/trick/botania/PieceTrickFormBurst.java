package com.github.kamefrede.rpsideas.spells.trick.botania;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.util.botania.PieceComponentTrick;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.mana.*;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.common.spell.trick.entity.PieceTrickAddMotion;

import javax.annotation.Nonnull;

public class PieceTrickFormBurst extends PieceComponentTrick {
    public PieceTrickFormBurst(Spell spell) {
        super(spell);
    }

    private SpellParam positionParam;
    private SpellParam rayParam;



    private static int MANA_PER_BURST = 120;

    @Override
    public void initParams() {
        positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);
        rayParam = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.GREEN, false, false);

        SpellHelpers.Building.addAllParams(this, positionParam, rayParam);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);

        meta.addStat(EnumSpellStat.COST, 400);
        meta.addStat(EnumSpellStat.POTENCY, 150);
    }


    @Override
    public Object executeIfAllowed(SpellContext context) throws SpellRuntimeException {
        Vector3 posVec = getParamValue(context, positionParam);
        Vector3 rayVec = getParamValue(context, rayParam);

        if(posVec == null || rayVec == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }
        if(!context.isInRadius(posVec)) {
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        }

        EntityPlayer player = context.caster;

        EntityManaBurst burst = formBurst(posVec, rayVec, context.caster.world, PsiAPI.getPlayerCAD(context.caster), context);

        if(!player.world.isRemote) {
            player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.manaBlaster, SoundCategory.PLAYERS, .6f, 1f);
            player.world.spawnEntity(burst);
            PieceTrickAddMotion.addMotion(context, burst, rayVec, 4F);
        }

        return null;
    }

    @Nonnull
    public BurstProperties getBurstProps(EntityPlayer player, ItemStack stack, boolean request, EnumHand hand) {
        int maxMana = 120;
        int color = 0x20FF20;
        int ticksBeforeManaLoss = 60;
        float manaLossPerTick = 4F;
        float motionModifier = 5F;
        float gravity = 0F;
        BurstProperties props = new BurstProperties(maxMana, ticksBeforeManaLoss, manaLossPerTick, gravity, motionModifier, color);

        return props;
    }

    public EntityManaBurst formBurst(Vector3 pos, Vector3 rayIn, World world, ItemStack cad, SpellContext context) {
        EntityManaBurst burst = new EntityManaBurst(context.caster, EnumHand.MAIN_HAND);
        BurstProperties props = getBurstProps(context.caster, cad, true, EnumHand.MAIN_HAND);


        ItemStack lens = ItemManaGun.getLens(cad);
        if(!lens.isEmpty() && lens.getItem() instanceof ILens) {
            ((ILens)lens.getItem()).apply(lens, props);
        }

        int color = ((ICAD)cad.getItem()).getSpellColor(cad) | 0xf000000;

        double yaw = -Math.atan2(rayIn.x, rayIn.z) * 180 / Math.PI - 180;
        double pitch =  Math.asin(rayIn.y) * 180 / Math.PI;

        burst.setSourceLens(lens);
        if(ManaItemHandler.requestManaExact(cad, context.caster, props.maxMana, false)) {
            burst.setBurstSourceCoords(new BlockPos(0, -1, 0));
            burst.setLocationAndAngles(pos.x, pos.y, pos.z, (float)yaw + 180, (float)pitch);


            burst.setColor(color);
            burst.setMana(props.maxMana);
            burst.setStartingMana(props.maxMana);
            burst.setMinManaLoss(props.ticksBeforeManaLoss);
            burst.setManaLossPerTick(props.manaLossPerTick);
            burst.setGravity(props.gravity);
            burst.setMotion(0.01, 0.01,0.01);


            return burst;
        }
        return null;
    }

    private static final String[] req = new String[]{Reference.MODID + ".requirement.form_burst"};

    @Override
    public int manaDrain(SpellContext context, int x, int y) {
        return 120;
    }

    @Override
    public String[] requiredObjects() {
        return req;
    }
}
