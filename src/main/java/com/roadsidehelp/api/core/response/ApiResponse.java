package com.roadsidehelp.api.core.response;

/**
 * Global API response envelope. Use record form for brevity and immutability.
 *
 * Example:
 *   return ResponseEntity.ok(ApiResponse.ok("Success", payload));
 */
public record ApiResponse<T>(boolean success, String message, T data) {

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static ApiResponse<?> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
