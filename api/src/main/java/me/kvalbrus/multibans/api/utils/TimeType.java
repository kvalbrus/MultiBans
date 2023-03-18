package me.kvalbrus.multibans.api.utils;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum TimeType {

    SECOND("s", 1000L),
    MINUTE("m", 60L * SECOND.duration),
    HOUR("h", 60L * MINUTE.duration),
    DAY("d", 24L * HOUR.duration),
    WEEK("w", 7L * DAY.duration),
    MONTH("mo", 30L * DAY.duration),
    YEAR("y", 365L * DAY.duration);

    @Getter
    private final String prefix;

    @Getter
    private final long duration;

    TimeType(@NotNull String prefix,
             long duration) {
        this.prefix = prefix;
        this.duration = duration;
    }
}