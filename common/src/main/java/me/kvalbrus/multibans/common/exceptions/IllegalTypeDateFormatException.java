package me.kvalbrus.multibans.common.exceptions;

public class IllegalTypeDateFormatException extends RuntimeException {

    public IllegalTypeDateFormatException(String type) {
        super("Type " + type + " isn't allowed");
    }
}