package me.kvalbrus.multibans.common.exceptions;

public class IllegalDateFormatException extends RuntimeException {

    public IllegalDateFormatException(String dateFormat) {
        super("Date format " + dateFormat + " isn't allowed");
    }
}