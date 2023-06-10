package me.kvalbrus.multibans.common.command.commands.multibans;

import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.utils.Message;
import org.jetbrains.annotations.NotNull;

public class Reload extends Command {

    public Reload(@NotNull PluginManager pluginManager) {
        super(pluginManager, "reload", Permission.COMMAND_RELOAD.getPerm(), null);
    }

    @Override
    public boolean cmd(@NotNull CommandSender sender, String[] args) {
        this.getPluginManager().reload();
        sender.sendMessage(Message.RELOAD.getMessage());
        return true;
    }

    @Override
    public String getNotPermissionMessage() {
        return null;
    }

    @Override
    public List<String> tab(@NotNull CommandSender sender, String[] args) {
        return null;
    }
}