package xyz.kamefrede.rpsideas.command;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.util.RPSConfigHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.PsiAPI;

public class CommandPsideasdump extends CommandBase {

    @NotNull
    @Override
    public String getName() {
        return "psideas-dump";
    }

    @NotNull
    @Override
    public String getUsage(@NotNull ICommandSender sender) {
        return localizationKey() + ".usage";
    }

    @Override
    public void execute(@NotNull MinecraftServer server, ICommandSender sender, @NotNull String[] args) throws CommandException {
        sender.sendMessage(new TextComponentString("Dumping registry names to the log file!"));
        sender.sendMessage(new TextComponentString("Beware that any disabled pieces on the config will not show up in the output"));
        for (String s : PsiAPI.spellPieceRegistry.getKeys()) {
            if (s.startsWith("rpsideas") && !RPSConfigHandler.isPieceBlacklisted(s.substring(s.indexOf(".") + 1)))
                RPSIdeas.LOGGER.log(Level.INFO, s.substring(s.indexOf(".") + 1));
        }
        sender.sendMessage(new TextComponentString("Done logging!"));

    }

    public String localizationKey() {
        return "command." + RPSIdeas.MODID + ".dump";
    }
}
