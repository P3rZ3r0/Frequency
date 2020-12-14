package xyz.elevated.frequency.command.impl;

import net.minecraft.server.v1_8_R3.EnumChatFormat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import xyz.elevated.frequency.Frequency;
import xyz.elevated.frequency.command.Command;
import xyz.elevated.frequency.command.CommandInfo;
import xyz.elevated.frequency.data.PlayerData;

@CommandInfo(name = "Velocity", description = "Sends a velocity packet to the player and receives knockback unless he is cheating", usage = "/fmac velocity <player> <velocityX> <velocityY> <velocityZ>")
public class Velocity extends Command {
    @Override
    protected boolean handle(CommandSender sender, String[] args) {
        if(args.length == 5) {
            if(Bukkit.getPlayer(args[1]) != null) {
                PlayerData data = Frequency.INSTANCE.getPlayerDataManager().getData(Bukkit.getPlayer(args[1]));

                data.sendPacket(new PacketPlayOutEntityVelocity(data.getBukkitPlayer().getEntityId(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4])));
                sender.sendMessage(EnumChatFormat.GREEN + "[FMAC] Entity Velocity packet was sent!");
                return true;
            }
        }

        return false;
    }
}
