package me.kvalbrus.multibans.bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

public class PluginLogger {

    private static final Logger logger = BukkitPlugin.getInstance().getLogger();

    public static void info(@NotNull String... messages) {
        log(Level.INFO, messages);
    }

    public static void info(@NotNull String message,
                            @NotNull Exception exception) {
        log(Level.INFO, message, exception);
    }

    public static void warn(@NotNull String... messages) {
        log(Level.WARNING, messages);
    }

    public static void warn(@NotNull String message,
                            @NotNull Exception exception) {
        log(Level.WARNING, message, exception);
    }

    public static void error(@NotNull String... messages) {
        log(Level.SEVERE, messages);
    }

    public static void error(@NotNull String message,
                             @NotNull Exception exception) {
        log(Level.SEVERE, message, exception);
    }

    public static void fileInfo(@NotNull String fileName,
                                @NotNull String... messages) {
        for (String message : messages) {
            info("[" + fileName + "] " + message);
        }
    }

    public static void fileInfo(@NotNull String fileName,
                                @NotNull String message,
                                Exception exception) {
        info("[" + fileName + "] " + message, exception);
    }

    public static void fileWarn(@NotNull String fileName,
                                @NotNull String... messages) {
        for (String message : messages) {
            warn("[" + fileName + "] " + message);
        }
    }

    public static void fileWarn(@NotNull String fileName,
                                @NotNull String message,
                                @NotNull Exception exception) {
        warn("[" + fileName + "] " + message, exception);
    }

    public static void fileError(@NotNull String fileName,
                                 @NotNull String... messages) {
        for (String message : messages) {
            error("[" + fileName + "] " + message);
        }
    }

    public static void fileError(@NotNull String fileName,
                                 @NotNull String message,
                                 @NotNull Exception exception) {
        error("[" + fileName + "] " + message, exception);
    }

    private static void log(@NotNull Level level,
                            @NotNull String... messages) {
        for (String message : messages) {
            log(level, message);
        }
    }

    private static void log(@NotNull String message) {
        log(Level.INFO, message);
    }

    private static void log(@NotNull Level level,
                            @NotNull String message) {
        logger.log(level, message);
    }

    private static void log(@NotNull Level level,
                            @NotNull String message,
                            @NotNull Exception exception) {
        logger.log(level, message, exception);
    }
}