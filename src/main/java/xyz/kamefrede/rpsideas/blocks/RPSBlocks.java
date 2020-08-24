package xyz.kamefrede.rpsideas.blocks;

import xyz.kamefrede.rpsideas.util.libs.RPSBlockNames;
import com.teamwizardry.librarianlib.features.base.block.BlockMod;
import net.minecraft.item.EnumDyeColor;

public class RPSBlocks {

    public static final BlockMod conjuredEthereal = new BlockConjuredEthereal();
    public static final BlockMod conjuredGravityBlock = new BlockConjuredGravityBlock();
    public static final BlockMod conjuredPulsar = new BlockConjuredPulsar();
    public static final BlockMod conjuredPulsarLight = new BlockPulsarLight();
    public static final BlockMod conjuredStar = new BlockConjuredStar();
    public static final BlockCADCase[] cadCases = new BlockCADCase[16];
    public static final BlockMod brightPlate = new BlockPlate(RPSBlockNames.BRIGHT_PLATE);
    public static final BlockMod darkPlate = new BlockPlate(RPSBlockNames.DARK_PLATE);

    static {
        for (int i = 0; i < cadCases.length; i++) {
            EnumDyeColor dye = EnumDyeColor.byMetadata(i);
            cadCases[i] = new BlockCADCase(RPSBlockNames.CAD_CASE + "_" + dye.getName(), dye);
        }
    }
}
