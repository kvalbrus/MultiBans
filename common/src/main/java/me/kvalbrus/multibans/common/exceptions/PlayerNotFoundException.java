package me.kvalbrus.multibans.common.exceptions;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String name) {
        super("Player " + name + " wasn't found");
    }
}
