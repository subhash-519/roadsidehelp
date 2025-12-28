package com.roadsidehelp.api.feature.mechanic.controller;

import com.roadsidehelp.api.core.utils.CurrentUser;
import com.roadsidehelp.api.feature.booking.dto.BookingLocationResponse;
import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.mechanic.dto.MechanicResponse;
import com.roadsidehelp.api.feature.mechanic.service.MechanicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('MECHANIC')")
@RestController
@RequestMapping("/api/v1/mechanic")
@RequiredArgsConstructor
public class MechanicProfileController {

    private final MechanicService mechanicService;

    // ---------------- PROFILE ----------------
    @GetMapping("/me")
    @Operation(summary = "Get logged-in mechanic profile")
    @ApiResponse(responseCode = "200", description = "Profile fetched successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<MechanicResponse> myProfile() {
        return ResponseEntity.ok(
                mechanicService.getMyProfile(CurrentUser.getUserId())
        );
    }

    // ---------------- BOOKINGS ----------------
    @GetMapping("/bookings/active")
    @Operation(summary = "Get active bookings assigned to mechanic")
    @ApiResponse(responseCode = "200", description = "Active bookings retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<List<BookingResponse>> activeBookings() {
        return ResponseEntity.ok(
                mechanicService.getAssignedBookings(CurrentUser.getUserId())
        );
    }

    @GetMapping("/bookings/history")
    @Operation(summary = "Get completed booking history")
    @ApiResponse(responseCode = "200", description = "Booking history retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<List<BookingResponse>> completedBookings() {
        return ResponseEntity.ok(
                mechanicService.getCompletedBookings(CurrentUser.getUserId())
        );
    }

    @GetMapping("/bookings/{bookingId}/location")
    @Operation(summary = "Track customer location for a booking")
    @ApiResponse(responseCode = "200", description = "Booking location retrieved")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<BookingLocationResponse> trackLocation(@PathVariable String bookingId) {
        return ResponseEntity.ok(
                mechanicService.getBookingLocation(CurrentUser.getUserId(), bookingId)
        );
    }

    @PatchMapping("/bookings/{bookingId}/start-service")
    @Operation(summary = "Start service for assigned booking")
    @ApiResponse(responseCode = "200", description = "Service started successfully")
    @ApiResponse(responseCode = "403", description = "Not assigned to this booking")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Void> startService(@PathVariable String bookingId) {
        mechanicService.startService(CurrentUser.getUserId(), bookingId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/bookings/{bookingId}/complete")
    @Operation(summary = "Mark booking as completed")
    @ApiResponse(responseCode = "200", description = "Booking completed successfully")
    @ApiResponse(responseCode = "403", description = "Not assigned to this booking")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Void> completeJob(@PathVariable String bookingId) {
        mechanicService.completeBooking(CurrentUser.getUserId(), bookingId);
        return ResponseEntity.ok().build();
    }
}
