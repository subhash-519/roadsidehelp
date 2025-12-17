package com.roadsidehelp.api.feature.booking.controller;

import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.service.BookingAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Bookings", description = "Admin booking management APIs")
public class BookingAdminController {

    private final BookingAdminService bookingAdminService;

    // Get all bookings
    @GetMapping
    @Operation(summary = "Get all bookings (Admin)")
    @ApiResponse(responseCode = "200", description = "All bookings fetched successfully")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(
                bookingAdminService.getAllBookings()
        );
    }

    // Get booking by ID
    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking details (Admin)")
    @ApiResponse(responseCode = "200", description = "Booking details fetched successfully")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable String bookingId) {

        return ResponseEntity.ok(
                bookingAdminService.getBookingById(bookingId)
        );
    }

    // Force update booking status
    @PatchMapping("/{bookingId}/status")
    @Operation(summary = "Force update booking status (Admin)")
    @ApiResponse(responseCode = "200", description = "Booking status updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid status value")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<BookingResponse> forceUpdateStatus(
            @PathVariable String bookingId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                bookingAdminService.forceUpdateStatus(bookingId, status)
        );
    }

    // Delete booking permanently
    @DeleteMapping("/{bookingId}")
    @Operation(summary = "Delete booking permanently (Admin)")
    @ApiResponse(responseCode = "204", description = "Booking deleted successfully")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable String bookingId) {

        bookingAdminService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}
