package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.blocks.PsionicBlocksCompat;
import com.github.kamefrede.rpsideas.compat.botania.BotaniaCompatItems;
import com.github.kamefrede.rpsideas.items.blocks.ItemCADCase;
import com.github.kamefrede.rpsideas.items.components.*;
import com.github.kamefrede.rpsideas.items.components.botania.ItemBlasterAssembly;
import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.arl.item.ItemMod;
import vazkii.arl.item.ItemModTool;

public class ModItems {

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.WIDE_SOCKET)
    public static final Item wideBandSocket = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.FLASH_RING)
    public static final Item flashRing = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.CAD_CASE)
    public static final Item cadCaseItem = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.INLINE_CASTER)
    public static final Item inlineCaster = Items.AIR;


    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.BIOTIC_SENSOR)
    public static final Item bioticSensor = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.UNSTABLE_BATTERY)
    public static final Item unstableBattery = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.TWINFLOW_BATTERY)
    public static final Item twinflowBattery = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.CREATIVE_BATTERY)
    public static final Item creativeBattery = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.CREATIVE_CORE)
    public static final Item creativeCore = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.CREATIVE_SOCKET)
    public static final Item creativeSocket = Items.AIR;

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibItems.PSIMETAL_ROD)
    public static final Item psimetalRod = Items.AIR;


    public static ItemMod sniperBullet;

    public static ItemModTool psimetalHoe;

    public static ItemMod psimetalShears;



    public static void preInit(){
        sniperBullet = new ItemSniperSpellBullet();
        psimetalHoe = new ItemPsionicHoe();
        psimetalShears = new ItemPsimetalShears();

    }




    public static void register(IForgeRegistry<Item> reg) {
        reg.register(createItem(new ItemFlashRing(), LibItems.FLASH_RING));
        reg.register(createItem(new ItemInlineCaster(), LibItems.INLINE_CASTER));
        reg.register(createItem(new ItemWideCADSocket(), LibItems.WIDE_SOCKET));
        reg.register(createItem(new ItemBioticSensor(), LibItems.BIOTIC_SENSOR));
        reg.register(createItem(new ItemUnstableBattery(), LibItems.UNSTABLE_BATTERY));
        reg.register(createItem(new ItemTwinflowBattery(), LibItems.TWINFLOW_BATTERY));
        reg.register(createItem(new ItemCreativeBattery(), LibItems.CREATIVE_BATTERY));
        reg.register(createItem(new ItemCreativeCore(), LibItems.CREATIVE_CORE));
        reg.register(createItem(new ItemCreativeSocket(), LibItems.CREATIVE_SOCKET));
        reg.register(createItem(new ItemPsimetalRod(), LibItems.PSIMETAL_ROD));

        reg.register(createItemBlock(new ItemCADCase(PsionicBlocksCompat.cadCase)));
        if(Loader.isModLoaded("botania")){
            BotaniaCompatItems.botaniaPreInit();
        }
    }

    public static <I extends Item> I createItem(I item, String name) {
        return createItem(item, name, true);
    }

    public static <I extends Item> I createItem(I item, String name, boolean showInCreative) {
        ResourceLocation res = new ResourceLocation(Reference.MODID, name);

        item.setRegistryName(res);
        item.setTranslationKey(res.getNamespace() + "." + res.getPath());

        if(showInCreative) item.setCreativeTab(RPSCreativeTab.INST);



        return item;
    }

    static <IB extends ItemBlock> IB createItemBlock(IB itemBlock) {
        return createItemBlock(itemBlock, true);
    }

    static <IB extends ItemBlock> IB createItemBlock(IB itemBlock, boolean showInCreative) {
        ResourceLocation res = itemBlock.getBlock().getRegistryName();

        itemBlock.setRegistryName(res);

        if(showInCreative) itemBlock.setCreativeTab(RPSCreativeTab.INST);


        return itemBlock;
    }
}
