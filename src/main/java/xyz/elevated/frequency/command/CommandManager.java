package xyz.elevated.frequency.command;

import net.minecraft.server.v1_8_R3.EnumChatFormat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.elevated.frequency.command.impl.Velocity;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {
    private final List<xyz.elevated.frequency.command.Command> commands = new ArrayList<>();

    public CommandManager() {
        commands.add(new Velocity());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        boolean ret = false;

        if(args.length > 0) {
            for(xyz.elevated.frequency.command.Command cmd : commands) {
                if(cmd.getName().equalsIgnoreCase(args[0])) {
                    if(!cmd.handle(commandSender, args)) {
                        commandSender.sendMessage(EnumChatFormat.RED + "[FMAC] Wrong command usage");
                        ret = false;
                    } else ret = true;
                }
            }
        }

        return ret;
    }
}
