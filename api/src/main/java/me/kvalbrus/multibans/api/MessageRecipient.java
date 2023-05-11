package me.kvalbrus.multibans.api;

import net.kyori.adventure.text.Component;

public interface MessageRecipient {

    void sendMessage(String message);

    void sendMessage(String... messages);

   // void sendMessage(Component component);
}
