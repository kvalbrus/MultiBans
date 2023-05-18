package me.kvalbrus.multibans.common.punishment.creator;

import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.punishment.creator.OnlinePunishmentCreator;
import org.jetbrains.annotations.NotNull;

public abstract class MultiOnlinePunishmentCreator extends MultiPunishmentCreator
    implements OnlinePunishmentCreator {

    private final CommandSender sender;

    public MultiOnlinePunishmentCreator(@NotNull CommandSender sender) {
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
