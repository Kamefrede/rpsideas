package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.blocks.ModBlocks;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.tiles.TileCracklingStar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;

public class PieceTrickConjureStar extends PieceTrickConjurePulsarTest {// TODO: 12/15/18 look at

    private SpellParam rayParam;
    private volatile Vector3 ray = null; //Just to pass from one method to the other.

    public PieceTrickConjureStar(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        super.initParams();

        rayParam = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.GREEN, false, false);
        addParam(rayParam);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;

        Vector3 rayVec = getParamValue(context, rayParam);
        Vector3 positionVec = getParamValue(context, positionParam);

        if (positionVec == null || rayVec == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        if (!context.isInRadius(positionVec) || !context.isInRadius(positionVec.copy().add(rayVec))) {
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        }

        ray = rayVec;
        return super.execute(context);
    }

    @Override
    public IBlockState getStateToSet() {
        return ModBlocks.conjuredStar.getDefaultState();
    }

    @Override
    protected void postSet(SpellContext context, World world, BlockPos pos, int time) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCracklingStar) {
            TileCracklingStar pulsar = (TileCracklingStar) tile;
            if (time > 0) {
                pulsar.setTime(time);
            }

            if (ray != null) {
                pulsar.addRay(ray.toVec3D());
                ray = null;
            }


            ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
            if (cad != null)
                pulsar.setColorizer(((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE));
        }
    }
}
