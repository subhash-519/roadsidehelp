package com.roadsidehelp.api.feature.booking.controller;

import com.roadsidehelp.api.feature.booking.dto.AssignMechanicRequest;
import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.dto.UpdateBookingStatusRequest;
import com.roadsidehelp.api.feature.booking.service.BookingGarageService;
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
@RequestMapping("/api/v1/bookings/garage")
@Tag(name = "Garage Bookings", description = "Garage booking operations")
public class BookingGarageController {

    private final BookingGarageService bookingGarageService;

    // Get all bookings for logged-in garage
    @GetMapping
    @Operation(summary = "Get garage bookings")
    @ApiResponse(responseCode = "200", description = "Garage bookings fetched successfully")
    public ResponseEntity<List<BookingResponse>> getGarageBookings() {
        return ResponseEntity.ok(
                bookingGarageService.getGarageBookings()
        );
    }

    // Accept or reject a booking
    @PatchMapping("/{bookingId}/status")
    @Operation(summary = "Accept or reject booking")
    @ApiResponse(responseCode = "200", description = "Booking status updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid status or request")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable String bookingId,
            @Valid @RequestBody UpdateBookingStatusRequest request) {

        return ResponseEntity.ok(
                bookingGarageService.updateBookingStatus(bookingId, request)
        );
    }

    // Assign mechanic to a booking
    @PostMapping("/assign-mechanic")
    @Operation(summary = "Assign mechanic to booking")
    @ApiResponse(responseCode = "200", description = "Mechanic assigned successfully")
    @ApiResponse(responseCode = "400", description = "Invalid mechanic or booking")
    @ApiResponse(responseCode = "404", description = "Booking or mechanic not found")
    public ResponseEntity<BookingResponse> assignMechanic(
            @Valid @RequestBody AssignMechanicRequest request) {

        return ResponseEntity.ok(
                bookingGarageService.assignMechanic(request)
        );
    }

    // Start service for a booking
    @PatchMapping("/{bookingId}/start")
    @Operation(summary = "Start service")
    @ApiResponse(responseCode = "200", description = "Service started successfully")
    @ApiResponse(responseCode = "400", description = "Service cannot be started")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<BookingResponse> startService(
            @PathVariable String bookingId) {

        return ResponseEntity.ok(
                bookingGarageService.startService(bookingId)
        );
    }

    // Complete service for a booking
    @PatchMapping("/{bookingId}/complete")
    @Operation(summary = "Complete service")
    @ApiResponse(responseCode = "200", description = "Service completed successfully")
    @ApiResponse(responseCode = "400", description = "Service cannot be completed")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public ResponseEntity<BookingResponse> completeService(
            @PathVariable String bookingId) {

        return ResponseEntity.ok(
                bookingGarageService.completeService(bookingId)
        );
    }
}
