package me.kvalbrus.multibans.common.utils;

import java.util.Date;
import me.kvalbrus.multibans.api.utils.TimeType;
import me.kvalbrus.multibans.common.exceptions.IllegalDateFormatException;
import me.kvalbrus.multibans.common.exceptions.IllegalTypeDateFormatException;
import org.jetbrains.annotations.NotNull;
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

    /**
     * The format will be changed according to the rules:
     * yyyy - year, mm - month, dd - day, HH - hour, MM - minutes, SS - seconds
     *
     * @param milliseconds Milliseconds
     * @param format       Template into which date values are inserted
     * @return String by format
     */
    @NotNull
    public static String getStringDate(long milliseconds, @NotNull String format) {
        Date date = new Date(milliseconds);

        format = format.replaceAll("yyyy", String.valueOf(date.getYear() + 1900));

        int month = date.getMonth() + 1;
        format = format.replaceAll("mm", String.valueOf(month >= 10 ? month : "0" + month));

        int day = date.getDate();
        format = format.replaceAll("dd", String.valueOf(day >= 10 ? day : "0" + day));

        int hour = date.getHours();
        format = format.replaceAll("HH", String.valueOf(hour >= 10 ? hour : "0" + hour));

        int minute = date.getMinutes();
        format = format.replaceAll("MM", String.valueOf(minute >= 10 ? minute : "0" + minute));

        int second = date.getSeconds();
        format = format.replaceAll("SS", String.valueOf(second >= 10 ? second : "0" + second));

        return format;
    }

    /**
     * @param milliseconds milliseconds
     * @return duration
     */
    @NotNull
    public static String getDuration(long milliseconds) {
        if (milliseconds < 0) {
            return Message.PERMANENTLY.getMessage();
        } else if (milliseconds < TimeType.SECOND.getDuration()) {
            return "0 " + getWordSecond(0);
        }

        StringBuilder builder = new StringBuilder();
        int days = (int) (milliseconds / TimeType.DAY.getDuration());
        milliseconds %= TimeType.DAY.getDuration();

        int hours = (int) (milliseconds / TimeType.HOUR.getDuration());
        milliseconds %= TimeType.HOUR.getDuration();

        int minutes = (int) (milliseconds / TimeType.MINUTE.getDuration());
        milliseconds %= TimeType.MINUTE.getDuration();

        int seconds = (int) (milliseconds / TimeType.SECOND.getDuration());

        if (days > 0) {
            builder.append(days).append("").append(getWordDay(days)).append(" ");
        }

        if (hours > 0) {
            builder.append(hours).append("").append(getWordHour(hours)).append(" ");
        }

        if (minutes > 0) {
            builder.append(minutes).append("").append(getWordMinute(minutes)).append(" ");
        }

        if (seconds > 0) {
            builder.append(seconds).append("").append(getWordSecond(seconds)).append(" ");
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    /**
     * @param seconds seconds
     * @return word seconds
     */
    @NotNull
    public static String getWordSecond(int seconds) {
        if (seconds <= 1) {
            return Message.SECOND.getMessage();
        } else {
            return Message.SECONDS.getMessage();
        }
    }

    /**
     * @param minutes minutes
     * @return word minutes
     */
    @NotNull
    public static String getWordMinute(int minutes) {
        if (minutes <= 1) {
            return Message.MINUTE.getMessage();
        } else {
            return Message.MINUTES.getMessage();
        }
    }

    /**
     * @param hours hours
     * @return word hours
     */
    @NotNull
    public static String getWordHour(int hours) {
        if (hours <= 1) {
            return Message.HOUR.getMessage();
        } else {
            return Message.HOURS.getMessage();
        }
    }

    /**
     * @param days days
     * @return word days
     */
    @NotNull
    public static String getWordDay(int days) {
        if (days <= 1) {
            return Message.DAY.getMessage();
        } else {
            return Message.DAYS.getMessage();
        }
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