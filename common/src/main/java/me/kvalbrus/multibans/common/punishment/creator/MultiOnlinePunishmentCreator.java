package me.kvalbrus.multibans.common.punishment.creator;

import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.punishment.creator.OnlinePunishmentCreator;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class MultiOnlinePunishmentCreator extends MultiPunishmentCreator
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

    @Override
    public void sendMessage(Component component) {
        this.sender.sendMessage(component);
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
