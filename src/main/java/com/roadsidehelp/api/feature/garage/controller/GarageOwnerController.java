package com.roadsidehelp.api.feature.garage.controller;

import com.roadsidehelp.api.core.utils.CurrentUser;
import com.roadsidehelp.api.feature.garage.dto.*;
import com.roadsidehelp.api.feature.garage.service.OwnerGarageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/garages/owner")
public class GarageOwnerController {

    private final OwnerGarageServiceImpl garageService;

    //Create garage (USER only)
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Create garage",
            description = "Create a new garage for the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Garage created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "409", description = "Garage already exists")
    public ResponseEntity<GarageResponse> createGarage(
            @RequestBody @Valid CreateGarageRequest req
    ) {
        return ResponseEntity.ok(
                garageService.createGarage(CurrentUser.getUserId(), req)
        );
    }

    //Get my garage (USER or GARAGE)
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','GARAGE')")
    @Operation(
            summary = "Get my garage",
            description = "Fetch the garage associated with the logged-in user"
    )
    @ApiResponse(responseCode = "200", description = "Garage details fetched successfully")
    @ApiResponse(responseCode = "404", description = "Garage not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<GarageResponse> getMyGarage() {
        return ResponseEntity.ok(
                garageService.getMyGarage(CurrentUser.getUserId())
        );
    }

    //Update garage details
    @PutMapping
    @PreAuthorize("hasAnyRole('USER','GARAGE')")
    @Operation(
            summary = "Update garage details",
            description = "Update basic garage information. Re-verification may be required."
    )
    @ApiResponse(responseCode = "200", description = "Garage updated successfully")
    @ApiResponse(responseCode = "404", description = "Garage not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<GarageResponse> updateGarage(
            @RequestBody @Valid UpdateGarageRequest req
    ) {
        return ResponseEntity.ok(
                garageService.updateGarage(CurrentUser.getUserId(), req)
        );
    }

    // Upload documents (KYC)
    @PostMapping("/documents")
    @PreAuthorize("hasAnyRole('USER','GARAGE')")
    @Operation(
            summary = "Upload garage documents",
            description = "Upload or update KYC documents for the garage"
    )
    @ApiResponse(responseCode = "200", description = "Documents updated successfully")
    @ApiResponse(responseCode = "404", description = "Garage not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<GarageResponse> updateDocuments(
            @RequestBody @Valid GarageDocumentRequest req
    ) {
        return ResponseEntity.ok(
                garageService.updateDocuments(CurrentUser.getUserId(), req)
        );
    }

    // Open / Close garage (APPROVED GARAGE only)
    @PatchMapping("/open-status")
    @PreAuthorize("hasRole('GARAGE')")
    @Operation(
            summary = "Open or close garage",
            description = "Change garage open status. Garage must be verified and KYC approved to open."
    )
    @ApiResponse(responseCode = "200", description = "Garage status updated successfully")
    @ApiResponse(responseCode = "403", description = "Garage not verified or KYC not approved")
    @ApiResponse(responseCode = "404", description = "Garage not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<GarageResponse> updateOpenStatus(
            @RequestParam boolean open
    ) {
        return ResponseEntity.ok(
                garageService.updateOpenStatus(CurrentUser.getUserId(), open)
        );
    }

    @PreAuthorize("hasAnyRole('USER','GARAGE')")
    @GetMapping("/status")
    @Operation(
            summary = "Get garage application status",
            description = "Check whether the user has applied for a garage and its verification status"
    )
    public ResponseEntity<GarageOwnerStatusResponse> getGarageStatus() {
        return ResponseEntity.ok(
                garageService.getMyGarageStatus(CurrentUser.getUserId())
        );
    }
}
