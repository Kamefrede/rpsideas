package com.github.kamefrede.rpsideas.spells.trick.botania;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.util.ITrickEnablerComponent;
import com.github.kamefrede.rpsideas.util.PieceComponentTrick;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.botania.IBlasterComponent;
import com.github.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
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
    public ITrickEnablerComponent.EnableResult acceptsPiece(ItemStack component, ItemStack cad, SpellContext context, Spell spell, int x, int y) {
        if(component.getItem() instanceof IBlasterComponent) {
            return ITrickEnablerComponent.EnableResult.fromBoolean(ManaItemHandler.requestManaExact(cad, context.caster, MANA_PER_BURST, true));
        } else return ITrickEnablerComponent.EnableResult.NOT_ENABLED;
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

        EntityManaBurst burst = formBurst(posVec, rayVec, player.world, PsiAPI.getPlayerCAD(context.caster));

        if(!player.world.isRemote) {
            player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.manaBlaster, SoundCategory.PLAYERS, .6f, 1f);

            player.world.spawnEntity(burst);
        }

        return null;
    }

    private EntityManaBurst formBurst(Vector3 pos, Vector3 rayIn, World world, ItemStack cad) {
        Vector3 ray = rayIn.copy().normalize();

        EntityManaBurst burst = new EntityManaBurst(world);
        burst.setBurstSourceCoords(new BlockPos(0, -1, 0));

        //TODO begin super sketch math code
        float yaw = (float) (-Math.atan2(ray.x, ray.z) * 180 / Math.PI - 180);
        float pitch = (float) (Math.asin(ray.y) * 180 / Math.PI);

        burst.setLocationAndAngles(pos.x, pos.y, pos.z, yaw + 180, -pitch);

        //TODO what the fuc, it's doing this crazy math then just overwriting it immediately? Mistake? :/
        burst.posX -= Math.cos((yaw + 180) / 180f * Math.PI) * .16f;
        burst.posY -= 0.1;
        burst.posZ -= Math.sin((yaw + 180) / 180f * Math.PI) * .16f;

        burst.setPosition(pos.x, pos.y, pos.z); //what

        double f = 0.4f;
        double motionX = Math.sin(yaw / 180d * Math.PI) * Math.cos(pitch / 180d * Math.PI) * f / 2;
        double motionY = Math.sin(pitch / 180d * Math.PI) * f / 2;
        double motionZ = Math.cos(yaw / 180d * Math.PI) * Math.cos(pitch / 180d * Math.PI) * f / 2 * -1;
        burst.setMotion(motionX, motionY, motionZ);

        int maxMana = 120;
        int ticksBeforeManaLossDotJpg = 60; //is this
        float manaLossjpgPerTick = 4f;      //L O S S
        float gravity = 0;
        float motionModifier = 5f;
        int color = ((ICAD)cad.getItem()).getSpellColor(cad) | 0xf000000;
        BurstProperties props = new BurstProperties(maxMana, ticksBeforeManaLossDotJpg, manaLossjpgPerTick, gravity, motionModifier, color);

        ItemStack lens = ItemManaGun.getLens(cad);
        if(!lens.isEmpty() && lens.getItem() instanceof ILens) {
            ((ILens)lens.getItem()).apply(lens, props);
        }

        burst.setSourceLens(lens);
        burst.setColor(props.color);
        burst.setMana(props.maxMana);
        burst.setMinManaLoss(props.ticksBeforeManaLoss);
        burst.setManaLossPerTick(props.manaLossPerTick);
        burst.setGravity(props.gravity);
        burst.setMotion(burst.motionX * props.motionModifier, burst.motionY * props.motionModifier, burst.motionZ * props.motionModifier);

        return burst;
    }

    private static final String[] req = new String[]{Reference.MODID + ".requirement.form_burst"};
    @Override
    public String[] requiredObjects() {
        return req;
    }
}
