package me.kvalbrus.multibans.common.exceptions;

public class PlayerNotFoundException extends Exception {

    public PlayerNotFoundException(String name) {
        super("Player " + name + " wasn't found");
    }
}
