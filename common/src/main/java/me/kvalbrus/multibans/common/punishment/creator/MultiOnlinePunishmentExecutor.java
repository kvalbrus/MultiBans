package me.kvalbrus.multibans.common.punishment.creator;

import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.punishment.executor.OnlinePunishmentExecutor;
import org.jetbrains.annotations.NotNull;

public abstract class MultiOnlinePunishmentExecutor extends MultiPunishmentExecutor
    implements OnlinePunishmentExecutor {

    private final CommandSender sender;

    public MultiOnlinePunishmentExecutor(@NotNull CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        this.sender.sendMessage(message);
    }

    @Override
    public void sendMessage(String... messages) {
        this.sender.sendMessage(messages);
    }

    @NotNull
    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }
}
