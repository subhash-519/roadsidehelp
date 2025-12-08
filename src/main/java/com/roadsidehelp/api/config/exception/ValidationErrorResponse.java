package com.roadsidehelp.api.config.exception;

import java.util.List;

/** Shape returned on request validation failure. */
public record ValidationErrorResponse(String message, List<String> fieldErrors) {}
