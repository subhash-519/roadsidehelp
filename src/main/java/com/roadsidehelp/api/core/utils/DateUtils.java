package com.roadsidehelp.api.core.utils;

import com.roadsidehelp.api.core.constants.TimeZones;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public final class DateUtils {

    private static final ZoneId IST = ZoneId.of(TimeZones.INDIA);

    private DateUtils() {}

    public static OffsetDateTime nowIST() {
        return OffsetDateTime.now(IST);
    }
}
