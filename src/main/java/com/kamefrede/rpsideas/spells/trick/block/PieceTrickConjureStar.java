package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.blocks.RPSBlocks;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.tiles.TileCracklingStar;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickConjureStar extends PieceTrick implements IPulsarConjuration {

    protected SpellParam positionParam;
    private SpellParam timeParam;
    private SpellParam rayParam;

    public PieceTrickConjureStar(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        super.initParams();

        positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);
        timeParam = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, true, true);
        rayParam = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.GREEN, false, false);

        addParam(rayParam);
        addParam(timeParam);
        addParam(positionParam);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;

        Vector3 rayVec = SpellHelpers.getVector3(this, context, rayParam, false, false);
        Vector3 positionVec = SpellHelpers.getVector3(this, context, positionParam, true, false);

        if (!context.isInRadius(positionVec.copy().add(rayVec)))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        conjurePulsar(context, positionParam, timeParam);
        return null;
    }

    @Override
    public IBlockState getStateToSet() {
        return RPSBlocks.conjuredStar.getDefaultState();
    }

    @Override
    public void postSet(SpellContext context, World world, BlockPos pos, int time) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCracklingStar) {
            TileCracklingStar pulsar = (TileCracklingStar) tile;
            if (time > 0) {
                pulsar.setTime(time);
            }

            Vector3 ray = getParamValue(context, rayParam);

            if (ray != null)
                pulsar.addRay(ray.toVec3D());

            ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
            if (cad != null)
                pulsar.setColorizer(((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE));
        }
    }
}
