package xyz.kamefrede.rpsideas.util;
import xyz.kamefrede.rpsideas.RPSIdeas;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
@Config(modid = RPSIdeas.MODID, category = "crafting")
public class RPSConfigHandler {
    @Config.Name("enableInlineCaster")
    @Config.Comment("Is the Inline Caster craftable?")
    public static boolean enableInline = true;

    @Config.Name("enableCADCase")
    @Config.Comment("Is the CAD Case craftable?")
    public static boolean enableCase = true;

    @Config.Name("enableFlashRing")
    @Config.Comment("Is the Flash Ring craftable?")
    public static boolean enableRing = true;

    @Config.Name("enableMagazine")
    @Config.Comment("Is the Spell Magazine craftable?")
    public static boolean enableMagazine = true;

    @Config.Name("enablePsipulse")
    @Config.Comment("Is the Psionic Pulse potion effect brewable?")
    public static boolean enablePsipulse = false;

    @Config.Name("enableCreativeCAD")
    @Config.Comment("If Avaritia is present, should the creative CAD assembly be craftable?")
    public static boolean enableCreativeCAD = false;

    @Config.Name("pieceBlacklist")
    @Config.Comment("Run /psideas-dump to get a full list of registry name for the pieces" + "\n" +
            "Note: Cannot be used to blacklist pieces that are required for the chapter" + "\n" +
            "Format: operator_root, trick_till.")
    public static String[] piecesBlacklist = {};

    public static boolean isPieceBlacklisted(String id) {
        if (piecesBlacklist.length == 0) return false;
        for (String piece : piecesBlacklist)
            if (piece.equals(id))
                return true;
        return false;
    }
}
