package com.kamefrede.rpsideas.tiles;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.rules.EnumActionType;
import com.kamefrede.rpsideas.rules.ISpellRule;
import com.kamefrede.rpsideas.rules.ranges.base.IRange;
import com.kamefrede.rpsideas.util.libs.RPSBlockNames;
import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.spell.PreSpellCastEvent;
import vazkii.psi.api.spell.SpellCastEvent;
import vazkii.psi.api.spell.SpellPiece;

import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
@TileRegister(RPSBlockNames.TILE_PSI_DAMPENER)
public class TilePsiDampener extends TileMod {

    private List<ISpellRule> rules;

    private IRange range;

    private static boolean isIntercepting = false;
    private static Vec3d location;

    @SubscribeEvent
    public static void preSpellCast(PreSpellCastEvent ev) {
        Vec3d loc = ev.getContext().focalPoint.getPositionVector();
        location = loc;

        for (TileEntity te : ev.getContext().caster.world.loadedTileEntityList) {
            if (te instanceof TilePsiDampener) {
                TilePsiDampener dampener = (TilePsiDampener) te;
                if (dampener.isInRange(loc)) {
                    for (ISpellRule rule : dampener.rules) {
                        for (SpellPiece[] row : ev.getSpell().grid.gridData) {
                            for (SpellPiece piece : row) {
                                if (!rule.isAllowed(piece.registryKey)) {
                                    ev.setCanceled(true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void beginInterception(PreSpellCastEvent ev) {
        isIntercepting = ev.isCanceled();
        if (!isIntercepting)
            location = null;
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent ev) {
        cancelIfRule(ev.getWorld(), ev.getPos(), ev, EnumActionType.EDIT_BLOCK);
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.PlaceEvent ev) {
        cancelIfRule(ev.getWorld(), ev.getPos(), ev, EnumActionType.EDIT_BLOCK);
    }

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinWorldEvent ev) {
        cancelIfRule(ev.getWorld(), ev.getEntity().getPositionVector(), ev, EnumActionType.SPAWN_ENTITY);
    }

    @SubscribeEvent
    public static void onEntityHarm(LivingAttackEvent ev) {
        cancelIfRule(ev.getEntity().getEntityWorld(), ev.getEntity().getPositionVector(), ev, EnumActionType.HARM_ENTITY);
    }

    @SubscribeEvent
    public static void onEntityGetPotion(PotionEvent.PotionApplicableEvent ev) {
        denyIfRule(ev.getEntity().getEntityWorld(), ev.getEntity().getPositionVector(), ev, EnumActionType.APPLY_POTION);
    }

    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent ev) {
        isIntercepting = false;
    }

    private static void cancelIfRule(World world, Predicate<TilePsiDampener> inRange, Runnable cancel, EnumActionType action) {
        if (isIntercepting) {
            for (TileEntity te : world.loadedTileEntityList) {
                if (te instanceof TilePsiDampener) {
                    TilePsiDampener dampener = (TilePsiDampener) te;
                    if (inRange.test(dampener) || dampener.isInRange(location)) {
                        for (ISpellRule rule : dampener.rules) {
                            if (!rule.isAllowed(action)) {
                                cancel.run();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void cancelIfRule(World world, BlockPos pos, Event event, EnumActionType action) {
        cancelIfRule(world, (damp) -> damp.isInRange(pos), () -> event.setCanceled(true), action);
    }

    private static void cancelIfRule(World world, Vec3d pos, Event event, EnumActionType action) {
        cancelIfRule(world, (damp) -> damp.isInRange(pos), () -> event.setCanceled(true), action);
    }

    private static void denyIfRule(World world, Vec3d pos, Event event, EnumActionType action) {
        cancelIfRule(world, (damp) -> damp.isInRange(pos), () -> event.setResult(Event.Result.DENY), action);
    }



    public boolean isInRange(BlockPos position) {
        return isInRange(new Vec3d(position)) && isInRange(new Vec3d(position).add(1, 1, 1));
    }

    public boolean isInRange(Vec3d position) {
        return range != null && range.isInRange(pos, position.x, position.y, position.z);
    }

    @Override
    public void readCustomNBT(@NotNull NBTTagCompound cmp) {
        range = IRange.deserialize(cmp.getCompoundTag("Range"));
    }

    @Override
    public void writeCustomNBT(@NotNull NBTTagCompound cmp, boolean sync) {
        if (range != null)
            cmp.setTag("Range", range.serializeNBT());
    }
}
