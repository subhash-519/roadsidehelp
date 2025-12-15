package com.roadsidehelp.api.feature.garage.controller;

import com.roadsidehelp.api.core.utils.CurrentUser;
import com.roadsidehelp.api.feature.garage.dto.*;
import com.roadsidehelp.api.feature.garage.service.OwnerGarageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/garages/owner")
public class GarageOwnerController {

    private final OwnerGarageService garageService;

    // Owner: Create garage
    @Operation(summary = "Create garage", description = "Create garage for authenticated user")
    @ApiResponse(responseCode = "200", description = "Garage created successfully")
    @PostMapping("/create")
    public ResponseEntity<GarageResponse> createGarage(@RequestBody CreateGarageRequest req) {
        return ResponseEntity.ok(garageService.createGarage(req));
    }

    // Owner: Get my garage
    @Operation(summary = "Get my garage", description = "Fetch logged-in user's garage details")
    @ApiResponse(responseCode = "200", description = "Garage fetched")
    @ApiResponse(responseCode = "404", description = "Garage not found")
    @GetMapping("/me")
    public ResponseEntity<GarageResponse> getMyGarage() {
        return ResponseEntity.ok(garageService.getMyGarage());
    }

    // Owner: Update garage
    @Operation(summary = "Update my garage", description = "Update the authenticated user's garage")
    @ApiResponse(responseCode = "200", description = "Garage updated successfully")
    @PutMapping("/update-garage")
    public ResponseEntity<GarageResponse> updateGarage(@RequestBody UpdateGarageRequest req) {
        return ResponseEntity.ok(garageService.updateGarage(req));
    }

    // Owner: Upload documents
    @Operation(summary = "Update garage documents", description = "Upload or update required documents")
    @ApiResponse(responseCode = "200", description = "Documents updated")
    @PostMapping("/update-documents")
    public ResponseEntity<GarageResponse> updateDocuments(@RequestBody GarageDocumentRequest req) {
        return ResponseEntity.ok(garageService.updateDocuments(req));
    }

    // Owner: Change verification status (used for reapply)
    @Operation(summary = "Change verification status", description = "Owner requests verification or re-verify")
    @ApiResponse(responseCode = "200", description = "Status updated")
    @PatchMapping("/status")
    public ResponseEntity<GarageResponse> updateVerificationStatus(
            @RequestParam boolean verified) {
        String ownerId = CurrentUser.getUserId();
        return ResponseEntity.ok(garageService.updateVerificationStatus(ownerId, verified));
    }

    @PatchMapping("/open-status")
    @Operation(summary = "Open/Close garage")
    public ResponseEntity<GarageResponse> updateOpenStatus(
            @RequestParam boolean open) {
        return ResponseEntity.ok(garageService.updateOpenStatus(open));
    }

}
