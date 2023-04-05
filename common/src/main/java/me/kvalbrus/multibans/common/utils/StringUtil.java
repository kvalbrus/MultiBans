package me.kvalbrus.multibans.common.utils;

import me.kvalbrus.multibans.api.utils.TimeType;
import me.kvalbrus.multibans.common.exceptions.IllegalDateFormatException;
import me.kvalbrus.multibans.common.exceptions.IllegalTypeDateFormatException;
import org.jetbrains.annotations.Nullable;

public class StringUtil {

    /**
     * Converts string to time in milliseconds.
     * Use keys: s - second, m - minute, h - hour, d - day,
     * w - week, mo - month, y - year
     * Example 1: 1d -> 86400000
     * Example 2: 1m30s -> 90000
     * Example 3: 1d1mo1m2h -> 2685660000
     *
     * @param string string t convert
     * @return milliseconds if string is correct, else -1;
     */
    public static long getTime(@Nullable String string) {
        if (string == null) {
            throw new IllegalDateFormatException(string);
        }

        long milliseconds = 0L;

        for (int i = 0; i < string.length(); ) {
            if (!isNumber(string.substring(i, i + 1))) {
                throw new IllegalDateFormatException(string);
            }

            int j = i + 1;
            for (; j < string.length(); ++j) {
                if (!isNumber(string.substring(i, j + 1))) {
                    break;
                }
            }

            long number = Long.parseLong(string.substring(i, j));
            if (number < 0) {
                throw new IllegalDateFormatException(string);
            }

            i = j;

            if (string.length() >= j + 2) {
                TimeType timeType = getTimeType(string.substring(j, j + 2));
                if (timeType == null) {
                    timeType = getTimeType(string.substring(j, j + 1));
                    if (timeType == null) {
                        throw new IllegalTypeDateFormatException(string.substring(j, j + 1));
                    } else {
                        milliseconds += number * timeType.getDuration();
                        i++;
                    }
                } else {
                    milliseconds += number * timeType.getDuration();
                    i += 2;
                }
            } else if (string.length() >= j + 1) {
                TimeType timeType = getTimeType(string.substring(j, j + 1));
                if (timeType == null) {
                    throw new IllegalTypeDateFormatException(string.substring(j, j + 1));
                } else {
                    milliseconds += number * timeType.getDuration();
                    i++;
                }
            } else {
                milliseconds += number * TimeType.SECOND.getDuration();
            }
        }

        return milliseconds;
    }

    private static boolean isNumber(String string) {
        if (string == null) {
            return false;
        }

        try {
            Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            return false;
        }

        return true;
    }

    @Nullable
    private static TimeType getTimeType(String string) {
        if (string == null) {
            return null;
        }

        for (TimeType timeType : TimeType.values()) {
            if (timeType.getPrefix().equals(string)) {
                return timeType;
            }
        }

        return null;
    }
}