package xyz.kamefrede.rpsideas.effect.base;

import xyz.kamefrede.rpsideas.RPSIdeas;
import com.teamwizardry.librarianlib.core.common.RegistrationHandler;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

/**
 * @author WireSegal
 * Created at 11:23 PM on 12/24/18.
 */
public class PotionTypeMod extends PotionType {
    public PotionTypeMod(String name, PotionEffect... effects) {
        super(RPSIdeas.MODID + "." + name, effects);
        RegistrationHandler.register(this, new ResourceLocation(RPSIdeas.MODID, name));
    }
}
