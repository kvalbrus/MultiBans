package me.kvalbrus.multibans.api.utils

import lombok.Getter

enum class TimeType(val prefix: String, val duration: Long) {
    SECOND("s", 1000L),
    MINUTE("m", 60L * SECOND.duration),
    HOUR("h", 60L * MINUTE.duration),
    DAY("d", 24L * HOUR.duration),
    WEEK("w", 7L * DAY.duration),
    MONTH("mo", 30L * DAY.duration),
    YEAR("y", 365L * DAY.duration)
}