package me.kvalbrus.multibans.api;

public interface MessageRecipient {

    void sendMessage(String message);

    void sendMessage(String... messages);
}
