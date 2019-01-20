package com.kamefrede.rpsideas.items.flow;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.IPsiAddonTool;
import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import com.kamefrede.rpsideas.util.helpers.FlowColorsHelper;
import com.kamefrede.rpsideas.util.helpers.IFlowColorAcceptor;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.teamwizardry.librarianlib.features.base.item.ItemModArmor;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import kotlin.jvm.functions.Function2;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.exosuit.ISensorHoldable;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.client.model.ModelPsimetalExosuit;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.ItemPsimetalTool;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ItemFlowExosuit extends ItemModArmor implements IPsiAddonTool, IPsiEventArmor, IFlowColorAcceptor {
    private static final String TAG_TIMES_CAST = "timesCast";
    @SideOnly(Side.CLIENT)
    private static ModelPsimetalExosuit[] models;
    final boolean ebony;


    private ItemFlowExosuit(String name, EntityEquipmentSlot slot, boolean ebony) {
        super(name, PsiAPI.PSIMETAL_ARMOR_MATERIAL, slot);
        this.ebony = ebony;
    }

    @SideOnly(Side.CLIENT)
    public static ModelPsimetalExosuit getModel(int index) {
        if (models == null) {
            models = new ModelPsimetalExosuit[4];
            for (int i = 0; i < models.length; i++)
                models[i] = new ModelPsimetalExosuit(i);
        }
        return models[index];
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        ItemPsimetalTool.regen(itemStack, player, false);
    }

    public void cast(ItemStack stack, PsiArmorEvent event) {
        PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(event.getEntityPlayer());
        ItemStack playerCad = PsiAPI.getPlayerCAD(event.getEntityPlayer());

        if (data != null && !playerCad.isEmpty()) {
            int timesCast = ItemNBTHelper.getInt(stack, TAG_TIMES_CAST, 0);

            ItemStack bullet = getBulletInSocket(stack, getSelectedSlot(stack));
            ItemCAD.cast(event.getEntityPlayer().world, event.getEntityPlayer(), data, bullet, playerCad, getCastCooldown(stack), 0, getCastVolume(), (SpellContext context) -> {
                context.tool = stack;
                context.attackingEntity = event.attacker;
                context.damageTaken = event.damage;
                context.loopcastIndex = timesCast;
            });

            ItemNBTHelper.setInt(stack, TAG_TIMES_CAST, timesCast + 1);
        }
    }

    @Override
    public void setSelectedSlot(ItemStack stack, int slot) {
        IPsiAddonTool.super.setSelectedSlot(stack, slot);
        ItemNBTHelper.setInt(stack, TAG_TIMES_CAST, 0);
    }

    @Override
    public void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
        IPsiAddonTool.super.setBulletInSocket(stack, slot, bullet);
        ItemNBTHelper.setInt(stack, TAG_TIMES_CAST, 0);
    }

    @Override
    public void onEvent(ItemStack stack, PsiArmorEvent event) {
        if (event.type.equals(getEvent(stack)))
            cast(stack, event);
    }


    protected float getCastVolume() {
        return 0.025f;
    }

    public abstract int getCastCooldown(ItemStack armorPiece);

    public abstract String getEvent(ItemStack armorPiece);


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            String componentName = TooltipHelper.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
            TooltipHelper.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
            TooltipHelper.addToTooltip(tooltip, getEvent(stack));
        });
    }


    @Override
    public int getColor(@Nonnull ItemStack stack) {
        return ICADColorizer.DEFAULT_SPELL_COLOR;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repair) {
        if (repair.getItem() == ModItems.material) {
            return repair.getItemDamage() == (ebony ? 4 : 5);
        } else return super.getIsRepairable(toRepair, repair);
    }


    @Override
    public boolean onEntityItemUpdate(EntityItem ent) {
        FlowColorsHelper.clearColorizer(ent.getItem());
        return super.onEntityItemUpdate(ent);
    }

    @Override
    public boolean hasColor(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (type != null && type.equals("overlay")) {
            return RPSIdeas.MODID + ":textures/model/" + (ebony ? "ebony" : "ivory") + "_exosuit.png";
        } else return "psi:textures/model/psimetal_exosuit_sensor.png";
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Function2<ItemStack, Integer, Integer> getItemColorFunction() {
        return (stack, tintIndex) -> {
            if (tintIndex == 1) {
                return ClientHelpers.getFlowColor(stack);
            } else if (tintIndex == 2) {
                return getColor(stack);
            } else return -1;
        };
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return getModel(armorSlot.ordinal() - 2);
    }

    public static class Helmet extends ItemFlowExosuit implements ISensorHoldable {
        public Helmet(String name, boolean ebony) {
            super(name, EntityEquipmentSlot.HEAD, ebony);
        }

        @Override
        public int getCastCooldown(ItemStack armorPiece) {
            return 40;
        }

        @Override
        public String getEvent(ItemStack hemlet) {
            ItemStack sensor = getAttachedSensor(hemlet);
            if (!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor) {
                return ((IExosuitSensor) sensor.getItem()).getEventType(sensor);
            }
            return PsiArmorEvent.NONE;
        }

        @Override
        public int getColor(@Nonnull ItemStack helmet) {
            ItemStack sensor = getAttachedSensor(helmet);
            if (!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor) {
                return ((IExosuitSensor) sensor.getItem()).getColor(sensor);
            } else return super.getColor(helmet);
        }

        @Override
        public ItemStack getAttachedSensor(ItemStack helmet) {
            NBTTagCompound compound = ItemNBTHelper.getCompound(helmet, "Sensor");
            return compound == null ? ItemStack.EMPTY : new ItemStack(compound);
        }

        @Override
        public void attachSensor(ItemStack helmet, ItemStack sensor) {
            ItemNBTHelper.setCompound(helmet, "Sensor", sensor.serializeNBT());
        }

        @Override
        public boolean hasContainerItem(@Nonnull ItemStack stack) {
            return true;
        }

        @Nonnull
        @Override
        public ItemStack getContainerItem(@Nonnull ItemStack helmet) {
            return getAttachedSensor(helmet);
        }
    }

    public static class Chestplate extends ItemFlowExosuit {
        public Chestplate(String name, boolean ebony) {
            super(name, EntityEquipmentSlot.CHEST, ebony);
        }

        @Override
        public int getCastCooldown(ItemStack armorPiece) {
            return 5;
        }

        @Override
        public String getEvent(ItemStack armorPiece) {
            return PsiArmorEvent.DAMAGE;
        }
    }

    public static class Leggings extends ItemFlowExosuit {
        public Leggings(String name, boolean ebony) {
            super(name, EntityEquipmentSlot.LEGS, ebony);
        }

        @Override
        public int getCastCooldown(ItemStack armorPiece) {
            return 0;
        }

        @Override
        public String getEvent(ItemStack armorPiece) {
            return PsiArmorEvent.TICK;
        }

        @Override
        protected float getCastVolume() {
            return 0f;
        }
    }

    public static class Boots extends ItemFlowExosuit {
        public Boots(String name, boolean ebony) {
            super(name, EntityEquipmentSlot.FEET, ebony);
        }

        @Override
        public int getCastCooldown(ItemStack armorPiece) {
            return 5;
        }

        @Override
        public String getEvent(ItemStack armorPiece) {
            return PsiArmorEvent.JUMP;
        }
    }

}
