package com.kamefrede.rpsideas.command;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Level;
import vazkii.psi.api.PsiAPI;

public class CommandPsideasdump extends CommandBase {
    @Override
    public String getName() {
        return "psideas-dump";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return localizationKey() + ".usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        sender.sendMessage(new TextComponentString("Dumping registry names to the log file!"));
        for (String s : PsiAPI.spellPieceRegistry.getKeys()) {
            if (s.startsWith("rpsideas"))
                RPSIdeas.LOGGER.log(Level.INFO, s.substring(s.indexOf(".") + 1));
        }
        sender.sendMessage(new TextComponentString("Done logging!"));

    }

    public String localizationKey() {
        return "command." + RPSIdeas.MODID + ".dump";
    }
}
