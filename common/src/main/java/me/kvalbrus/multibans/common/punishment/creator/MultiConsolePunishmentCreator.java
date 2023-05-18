package me.kvalbrus.multibans.common.punishment.creator;

import me.kvalbrus.multibans.api.Console;
import me.kvalbrus.multibans.api.punishment.creator.ConsolePunishmentCreator;
import org.jetbrains.annotations.NotNull;

public class MultiConsolePunishmentCreator extends MultiOnlinePunishmentCreator
    implements ConsolePunishmentCreator {

    private final Console console;

    public MultiConsolePunishmentCreator(@NotNull Console console) {
        super(console);
        this.console = console;
    }

    @Override
    public void sendMessage(String message) {
        this.console.sendMessage(message);
    }

    @Override
    public void sendMessage(String... messages) {
        this.console.sendMessage(messages);
    }

    @NotNull
    @Override
    public String getName() {
        return this.console.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.console.hasPermission(permission);
    }
}