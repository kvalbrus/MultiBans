package me.kvalbrus.multibans.common.exceptions;

public class NotEnoughArgumentsException extends RuntimeException {

    public NotEnoughArgumentsException(int minArguments) {
        super("Minimum " + minArguments + " arguments");
    }
}
