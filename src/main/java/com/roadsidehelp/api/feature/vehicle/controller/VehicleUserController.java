package com.roadsidehelp.api.feature.vehicle.controller;

import com.roadsidehelp.api.feature.vehicle.dto.CreateVehicleRequest;
import com.roadsidehelp.api.feature.vehicle.dto.UpdateVehicleRequest;
import com.roadsidehelp.api.feature.vehicle.dto.VehicleResponse;
import com.roadsidehelp.api.feature.vehicle.service.VehicleUserService;
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
@RequestMapping("/api/v1/vehicles")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Vehicles", description = "Manage vehicles of the logged-in user")
public class VehicleUserController {

    private final VehicleUserService vehicleService;

    // -------------------- ADD VEHICLE --------------------
    @Operation(
            summary = "Add a new vehicle",
            description = "Add a vehicle for the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Vehicle added successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PostMapping
    public ResponseEntity<VehicleResponse> addVehicle(
            @RequestBody @Valid CreateVehicleRequest request) {

        return ResponseEntity.ok(vehicleService.addVehicle(request));
    }

    // -------------------- GET MY VEHICLES --------------------
    @Operation(
            summary = "Get my vehicles",
            description = "Fetch all vehicles of the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Vehicles fetched successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getMyVehicles() {

        return ResponseEntity.ok(vehicleService.getMyVehicles());
    }

    // -------------------- UPDATE VEHICLE --------------------
    @Operation(
            summary = "Update vehicle details",
            description = "Update a vehicle owned by the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Vehicle updated successfully")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized vehicle access")
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(
            @PathVariable String id,
            @RequestBody UpdateVehicleRequest request) {

        return ResponseEntity.ok(vehicleService.updateVehicle(id, request));
    }

    // -------------------- DELETE VEHICLE --------------------
    @Operation(
            summary = "Delete vehicle",
            description = "Delete a vehicle owned by the authenticated user"
    )
    @ApiResponse(responseCode = "204", description = "Vehicle deleted successfully")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized vehicle access")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String id) {

        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
