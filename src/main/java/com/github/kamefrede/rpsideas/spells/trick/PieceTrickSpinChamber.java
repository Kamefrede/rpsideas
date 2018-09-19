package com.github.kamefrede.rpsideas.spells.trick;

import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;

public class PieceTrickSpinChamber extends PieceTrick {

    SpellParam num;

    public PieceTrickSpinChamber(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public void addParam(SpellParam param) {
        super.addParam(param);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double spin = this.<Double>getParamValue(context, num);
        if(spin == 0) throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_NUMBER);
        if(context.caster.getEntityWorld().isRemote) return null;
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        if (cad != null){
            int totalSlots =((ICAD) cad.getItem()).getStatValue(cad, EnumCADStat.SOCKETS);
            int curSlot = ((ICAD) cad.getItem()).getSelectedSlot(cad);
            if(spin > 0){
                if(curSlot == totalSlots){
                    ((ICAD) cad.getItem()).setSelectedSlot(cad, 1);
                    ItemStack bullet = ((ICAD) cad.getItem()).getBulletInSocket(cad, 1);
                    if(bullet == ItemStack.EMPTY){
                        return null;
                    } else {
                        ItemCAD.cast(context.caster.getEntityWorld(), context.caster, PlayerDataHandler.get(context.caster), bullet, cad, 40, 25, 0.5f, null);
                        return new ActionResult<>(EnumActionResult.SUCCESS, cad);
                    }

                } else {
                    ((ICAD) cad.getItem()).setSelectedSlot(cad, curSlot + 1);
                    ItemStack bullet = ((ICAD) cad.getItem()).getBulletInSocket(cad, curSlot +1);
                    if(bullet == ItemStack.EMPTY){
                        return null;
                    } else {
                        ItemCAD.cast(context.caster.getEntityWorld(), context.caster, PlayerDataHandler.get(context.caster), bullet, cad, 40, 25, 0.5f, null);
                        return new ActionResult<>(EnumActionResult.SUCCESS, cad);
                    }
                }
            } else {
                if(curSlot == 1){
                    ((ICAD) cad.getItem()).setSelectedSlot(cad, totalSlots);
                    ItemStack bullet = ((ICAD) cad.getItem()).getBulletInSocket(cad, totalSlots);
                    if(bullet == ItemStack.EMPTY){
                        return null;
                    } else {
                        ItemCAD.cast(context.caster.getEntityWorld(), context.caster, PlayerDataHandler.get(context.caster), bullet, cad, 40, 25, 0.5f, null);
                        return new ActionResult<>(EnumActionResult.SUCCESS, cad);
                    }
                } else {
                    ((ICAD) cad.getItem()).setSelectedSlot(cad, curSlot - 1);
                    ItemStack bullet = ((ICAD) cad.getItem()).getBulletInSocket(cad, curSlot - 1);
                    if(bullet == ItemStack.EMPTY){
                        return null;
                    } else {
                        ItemCAD.cast(context.caster.getEntityWorld(), context.caster, PlayerDataHandler.get(context.caster), bullet, cad, 40, 25, 0.5f, null);
                        return new ActionResult<>(EnumActionResult.SUCCESS, cad);
                    }
                }

            }

        }
        return null;
    }
}
