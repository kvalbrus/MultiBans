package me.kvalbrus.multibans.common.utils;

import me.kvalbrus.multibans.common.exceptions.IllegalDateFormatException;
import me.kvalbrus.multibans.common.exceptions.IllegalTypeDateFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static me.kvalbrus.multibans.api.utils.TimeType.SECOND;
import static me.kvalbrus.multibans.api.utils.TimeType.MINUTE;
import static me.kvalbrus.multibans.api.utils.TimeType.HOUR;
import static me.kvalbrus.multibans.api.utils.TimeType.DAY;
import static me.kvalbrus.multibans.api.utils.TimeType.WEEK;
import static me.kvalbrus.multibans.api.utils.TimeType.MONTH;
import static me.kvalbrus.multibans.api.utils.TimeType.YEAR;

public class StringUtilTest {

    @Test
    @DisplayName("Convert time from string test")
    public void testConvertTimeFromString() {
        assertEquals(StringUtil.getTime("1s"), SECOND.getDuration());
        assertEquals(StringUtil.getTime("34s"), 34 * SECOND.getDuration());
        assertEquals(StringUtil.getTime("231"), 231 * SECOND.getDuration());

        assertEquals(StringUtil.getTime("2m"), 2 * MINUTE.getDuration());
        assertEquals(StringUtil.getTime("321m"), 321 * MINUTE.getDuration());

        assertEquals(StringUtil.getTime("4h"), 4 * HOUR.getDuration());

        assertEquals(StringUtil.getTime("1d"), DAY.getDuration());

        assertEquals(StringUtil.getTime("4w"), 4 * WEEK.getDuration());

        assertEquals(StringUtil.getTime("1mo"), MONTH.getDuration());
        assertEquals(StringUtil.getTime("3243mo"), 3243 * MONTH.getDuration());

        assertEquals(StringUtil.getTime("32y"), 32 * YEAR.getDuration());

        assertEquals(StringUtil.getTime("4w3d"), 4 * WEEK.getDuration() + 3 * DAY.getDuration());
        assertEquals(StringUtil.getTime("34s56"), (34 + 56) * SECOND.getDuration());
        assertEquals(StringUtil.getTime("98d45y32s34mo"), 98 * DAY.getDuration() +
            45 * YEAR.getDuration() + 32 * SECOND.getDuration() + 34 * MONTH.getDuration());
        assertEquals(StringUtil.getTime("1s2d3h4d5w6mo7y"), SECOND.getDuration() +
            2 * DAY.getDuration() + 3 * HOUR.getDuration() + 4 * DAY.getDuration() +
            5 * WEEK.getDuration() + 6 * MONTH.getDuration() + 7 * YEAR.getDuration());

        assertThrows(IllegalDateFormatException.class, () -> StringUtil.getTime(null));
        assertThrows(IllegalDateFormatException.class, () -> StringUtil.getTime("d45m"));
        assertThrows(IllegalTypeDateFormatException.class, () -> StringUtil.getTime("434r"));
        assertThrows(IllegalDateFormatException.class, () -> StringUtil.getTime("4d-5m"));
    }
}
