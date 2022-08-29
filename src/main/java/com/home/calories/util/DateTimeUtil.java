package com.home.calories.util;

import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateTimeUtil {

    public static final ZoneOffset MOSCOW_ZONE_OFFSET = ZoneOffset.ofHours(+3);
    public static final ZoneId MOSCOW_ZONE_ID = ZoneId.from(ZoneOffset.of("+03:00"));

    private DateTimeUtil() {
        throw new UnsupportedOperationException();
    }

}


