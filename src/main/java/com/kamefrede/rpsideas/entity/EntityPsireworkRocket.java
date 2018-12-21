package com.kamefrede.rpsideas.entity;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.common.Psi;

/**
 * @author WireSegal
 * Created at 8:10 AM on 12/21/18.
 */
public class EntityPsireworkRocket extends EntityFireworkRocket {

    private static final DataParameter<ItemStack> COLORIZER = EntityDataManager.createKey(EntityPsireworkRocket.class, DataSerializers.ITEM_STACK);

    public EntityPsireworkRocket(World worldIn) {
        super(worldIn);
    }

    public EntityPsireworkRocket(World worldIn, double x, double y, double z, ItemStack givenItem, ItemStack colorizer) {
        super(worldIn, x, y, z, givenItem);
        setColorizer(colorizer);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(COLORIZER, ItemStack.EMPTY);
    }

    public void setColorizer(ItemStack colorizer) {
        dataManager.set(COLORIZER, colorizer);
    }

    public ItemStack getColorizer() {
        return dataManager.get(COLORIZER);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Colorizer", getColorizer().serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setColorizer(new ItemStack(compound.getCompoundTag("Colorizer")));
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 17 && this.world.isRemote) {
            ItemStack colorizer = getColorizer();
            if (!colorizer.isEmpty()) {

                ItemStack fireworkItem = dataManager.get(FIREWORK_ITEM);

                int color = Psi.proxy.getColorizerColor(colorizer).getRGB();

                NBTTagCompound fireworks = ItemNBTHelper.getCompound(fireworkItem, "Fireworks", false);

                NBTTagList explosions = fireworks.getTagList("Explosions", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < explosions.tagCount(); i++) {
                    NBTTagCompound explosion = explosions.getCompoundTagAt(i);
                    explosion.setIntArray("Colors", new int[]{ color });
                }

                if (explosions.isEmpty()) {
                    NBTTagCompound explosion = new NBTTagCompound();
                    explosion.setIntArray("Colors", new int[]{ color });
                    int type = 1;
                    double rand = Math.random();
                    if (rand > 0.25) {
                        if (rand > 0.9)
                            type = 2;
                        else type = 0;
                    }

                    explosion.setInteger("Type", type);

                    if (Math.random() < 0.05) {
                        if (Math.random() < 0.5)
                            explosion.setBoolean("Flicker", true);
                        else explosion.setBoolean("Trail", true);
                    }

                    explosions.appendTag(explosion);
                }

                fireworks.setTag("Explosions", explosions);

                ItemNBTHelper.setCompound(fireworkItem, "Fireworks", fireworks);
            }
        }


        super.handleStatusUpdate(id);
    }
}
