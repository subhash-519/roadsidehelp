package com.roadsidehelp.api.feature.garage.controller;

import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.feature.garage.dto.GarageResponse;
import com.roadsidehelp.api.feature.garage.service.GarageAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/garages/admin")
@PreAuthorize("hasRole('ADMIN')")
public class GarageAdminController {

    private final GarageAdminService adminService;
    private final UserAccountRepository userAccountRepository;

    // Get all pending KYC garages
    @Operation(summary = "Get pending garages")
    @ApiResponse(responseCode = "200", description = "Pending garages fetched")
    @GetMapping("/pending")
    public ResponseEntity<List<GarageResponse>> getPendingGarages() {
        return ResponseEntity.ok(adminService.getPendingGarages());
    }

    // Approve garage KYC
    @Operation(summary = "Approve garage KYC")
    @ApiResponse(responseCode = "200", description = "Garage approved")
    @PatchMapping("/{garageId}/approve")
    public ResponseEntity<GarageResponse> approveGarage(@PathVariable String garageId) {
        return ResponseEntity.ok(adminService.approveGarage(garageId));
    }

    // Reject garage KYC
    @Operation(summary = "Reject garage KYC")
    @ApiResponse(responseCode = "200", description = "Garage rejected")
    @PatchMapping("/{garageId}/reject")
    public ResponseEntity<GarageResponse> rejectGarage(
            @PathVariable String garageId,
            @RequestParam String reason) {
        return ResponseEntity.ok(adminService.rejectGarage(garageId, reason));
    }

    // Admin: Get all garages
    @Operation(summary = "Get all garages")
    @ApiResponse(responseCode = "200", description = "All garages fetched")
    @GetMapping
    public ResponseEntity<List<GarageResponse>> getAllGarages() {
        return ResponseEntity.ok(adminService.getAllGarages());
    }

    // Admin: Delete garage
    @Operation(summary = "Delete garage")
    @ApiResponse(responseCode = "200", description = "Garage deleted")
    @DeleteMapping("/{garageId}")
    public ResponseEntity<Void> deleteGarage(@PathVariable String garageId) {
        adminService.deleteGarage(garageId);
        return ResponseEntity.ok().build();
    }
}
