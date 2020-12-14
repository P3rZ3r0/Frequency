package xyz.elevated.frequency.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

@Getter
@Setter
public abstract class Command {
    private String name, desc, usage;

    public Command() {
        if(this.getClass().isAnnotationPresent(CommandInfo.class)) {
            final CommandInfo cmdInfo = this.getClass().getAnnotation(CommandInfo.class);

            name = cmdInfo.name();
            desc = cmdInfo.description();
            usage = cmdInfo.usage();
        }
    }

    protected abstract boolean handle(final CommandSender sender, final String[] args);
}
