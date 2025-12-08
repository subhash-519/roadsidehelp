package com.roadsidehelp.api.config.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public record ErrorResponse(
        boolean success,
        String message,
        ErrorCode errorCode,
        String detail,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime timestamp
) {
    public static ErrorResponse of(ErrorCode code, String message, String detail) {
        return new ErrorResponse(
                false,
                message,
                code,
                detail,
                OffsetDateTime.now(ZoneId.of("Asia/Kolkata"))
        );
    }
}
