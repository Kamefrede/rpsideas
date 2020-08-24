package xyz.kamefrede.rpsideas.util;

import xyz.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.IFixableData;

import javax.annotation.Nonnull;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WireSegal
 * Created at 3:03 PM on 12/22/18.
 */
public class RPSDataFixer {

    private static final Pattern SEM_VER = Pattern.compile("^(\\d+)(?:\\.(\\d+)(?:\\.(\\d+))?)?");

    public static int parseSemVer(String semVer) {
        Matcher match = SEM_VER.matcher(semVer);
        if (match.matches()) {
            String majorMatch = match.group(1);
            String minorMatch = match.group(2);
            String patchMatch = match.group(3);

            int major = (majorMatch == null || majorMatch.isEmpty()) ? 0 : Integer.parseInt(majorMatch);
            int minor = (minorMatch == null || minorMatch.isEmpty()) ? 0 : Integer.parseInt(minorMatch);
            int patch = (patchMatch == null || patchMatch.isEmpty()) ? 0 : Integer.parseInt(patchMatch);

            return (major * 100 + minor) * 1000 + patch;
        }

        return 0;
    }

    public static void registerFix(IFixType type, String fixVersion, UnaryOperator<NBTTagCompound> fixer) {
        int version = parseSemVer(fixVersion);

        RPSIdeas.DATA_FIXER.registerFix(type, new IFixableData() {
            @Override
            public int getFixVersion() {
                return version;
            }

            @Nonnull
            @Override
            public NBTTagCompound fixTagCompound(@Nonnull NBTTagCompound compound) {
                return fixer.apply(compound);
            }
        });
    }
}
