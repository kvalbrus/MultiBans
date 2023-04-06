package me.kvalbrus.multibans.common.exceptions;

public class NotEnoughArgumentsException extends Exception {

    public NotEnoughArgumentsException(int minArguments) {
        super("Minimum " + minArguments + " arguments");
    }
}
