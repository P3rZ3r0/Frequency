package xyz.elevated.frequency.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String name();
    String description();
    String usage();
}
