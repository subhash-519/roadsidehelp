package com.roadsidehelp.api.feature.booking.controller;

import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.dto.CreateBookingRequest;
import com.roadsidehelp.api.feature.booking.service.BookingUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings/user")
@Tag(name = "User Bookings", description = "User booking operations")
public class BookingUserController {

    private final BookingUserService bookingUserService;

    // Create a new booking
    @PostMapping
    @Operation(summary = "Create booking")
    @ApiResponse(responseCode = "200", description = "Booking created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid booking request")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request) {

        return ResponseEntity.ok(
                bookingUserService.createBooking(request)
        );
    }

    // Get all bookings of logged-in user
    @GetMapping
    @Operation(summary = "Get my bookings")
    @ApiResponse(responseCode = "200", description = "User bookings fetched successfully")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {

        return ResponseEntity.ok(
                bookingUserService.getMyBookings()
        );
    }

    // Get booking details by ID
    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking by ID")
    @ApiResponse(responseCode = "200", description = "Booking details fetched successfully")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable String bookingId) {

        return ResponseEntity.ok(
                bookingUserService.getMyBookingById(bookingId)
        );
    }

    // Cancel an existing booking
    @PatchMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel booking")
    @ApiResponse(responseCode = "200", description = "Booking cancelled successfully")
    @ApiResponse(responseCode = "400", description = "Booking cannot be cancelled")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable String bookingId,
            @RequestParam(required = false) String reason) {

        return ResponseEntity.ok(
                bookingUserService.cancelBooking(bookingId, reason)
        );
    }
}
