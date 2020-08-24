package xyz.kamefrede.rpsideas.tiles;

import com.google.common.collect.Lists;
import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.rules.ActionRule;
import xyz.kamefrede.rpsideas.rules.EnumActionType;
import xyz.kamefrede.rpsideas.rules.ISpellRule;
import xyz.kamefrede.rpsideas.rules.TrickRule;
import xyz.kamefrede.rpsideas.rules.ranges.base.IRange;
import xyz.kamefrede.rpsideas.util.libs.RPSBlockNames;
import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.PreSpellCastEvent;
import vazkii.psi.api.spell.SpellCastEvent;
import vazkii.psi.api.spell.SpellPiece;

import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
@TileRegister(RPSBlockNames.TILE_PSI_DAMPENER)
public class TilePsiDampener extends TileMod {

    private static boolean isIntercepting = false;
    private static Vec3d location;
    private List<ISpellRule> rules;
    private IRange range;

    @SubscribeEvent
    public static void preSpellCast(PreSpellCastEvent ev) {
        Vec3d loc = ev.getContext().focalPoint.getPositionVector();
        location = loc;

        List<String> tricks = Lists.newArrayList();
        for (SpellPiece[] row : ev.getSpell().grid.gridData) {
            for (SpellPiece piece : row)
                if (piece != null && piece.getPieceType() == EnumPieceType.TRICK)
                    tricks.add(piece.registryKey);
        }

        cancelIfRule(ev.getContext().caster.world,
                (damp) -> damp.isInRange(loc),
                () -> ev.setCanceled(true),
                (rule) -> tricks.stream().anyMatch((str) -> !rule.isAllowed(str)));
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
        cancelIfRule(ev.getEntity().world, ev.getEntity().getPositionVector(), ev, EnumActionType.HARM_ENTITY);
    }

    @SubscribeEvent
    public static void onEntityGetPotion(PotionEvent.PotionApplicableEvent ev) {
        denyIfRule(ev.getEntity().world, ev.getEntity().getPositionVector(), ev, EnumActionType.APPLY_POTION);
    }

    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent ev) {
        isIntercepting = false;
    }

    private static void cancelIfRule(World world, Predicate<TilePsiDampener> inRange, Runnable cancel, EnumActionType action) {
        cancelIfRule(world, inRange, cancel, (rule) -> rule.isAllowed(action));
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

    private static void cancelIfRule(World world, Predicate<TilePsiDampener> inRange, Runnable cancel, Predicate<ISpellRule> allowed) {
        if (isIntercepting) {
            for (TileEntity te : world.loadedTileEntityList) {
                if (te instanceof TilePsiDampener && ((TilePsiDampener) te).rules != null) {
                    TilePsiDampener dampener = (TilePsiDampener) te;
                    if (inRange.test(dampener) || dampener.isInRange(location)) {
                        for (ISpellRule rule : dampener.rules) {
                            if (!allowed.test(rule)) {
                                cancel.run();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }


    public boolean isInRange(BlockPos position) {
        return isInRange(new Vec3d(position)) && isInRange(new Vec3d(position).add(1, 1, 1));
    }

    public boolean isInRange(Vec3d position) {
        return range != null && range.isInRange(pos, position.x, position.y, position.z);
    }

    @Override
    public void readCustomNBT(@NotNull NBTTagCompound cmp) {
        NBTTagList ruleNBT = cmp.getTagList("Rules", Constants.NBT.TAG_COMPOUND);
        List<ISpellRule> ruleList = Lists.newArrayList();
        for (int i = 0; i < ruleNBT.tagCount(); i++) {
            NBTTagCompound rule = ruleNBT.getCompoundTagAt(i);

            String type = rule.getString("Type");
            if (type.equals("action")) {
                EnumActionType actionType = EnumActionType.byName(rule.getString("Action"));
                if (actionType != null)
                    ruleList.add(new ActionRule(actionType));
            } else if (type.equals("trick")) {
                String trickName = rule.getString("Trick");
                if (PsiAPI.spellPieceRegistry.containsKey(trickName))
                    ruleList.add(new TrickRule(trickName));
            }
        }
        rules = ruleList;

        range = IRange.deserialize(cmp.getCompoundTag("Range"));
    }

    @Override
    public void writeCustomNBT(@NotNull NBTTagCompound cmp, boolean sync) {
        if (range != null)
            cmp.setTag("Range", range.serializeNBT());

        NBTTagList ruleNBT = new NBTTagList();
        for (ISpellRule rule : rules) {
            if (rule instanceof ActionRule) {
                NBTTagCompound action = new NBTTagCompound();
                action.setString("Type", "action");
                action.setString("Action", ((ActionRule) rule).getType().getName());
                ruleNBT.appendTag(action);
            } else if (rule instanceof TrickRule) {
                NBTTagCompound trick = new NBTTagCompound();
                trick.setString("Type", "trick");
                trick.setString("Trick", ((TrickRule) rule).getTrick());
                ruleNBT.appendTag(trick);
            }
        }

        cmp.setTag("Rules", ruleNBT);
    }
}
