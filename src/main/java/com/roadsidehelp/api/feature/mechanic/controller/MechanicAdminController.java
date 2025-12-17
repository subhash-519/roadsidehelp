package com.roadsidehelp.api.feature.mechanic.controller;

import com.roadsidehelp.api.feature.mechanic.dto.MechanicResponse;
import com.roadsidehelp.api.feature.mechanic.service.MechanicAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/api/v1/admin/mechanics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MechanicAdminController {

    private final MechanicAdminService mechanicAdminService;

    // Get all mechanics
    @GetMapping
    @Operation(summary = "Get all mechanics")
    @ApiResponse(responseCode = "200", description = "All mechanics fetched successfully")
    public ResponseEntity<List<MechanicResponse>> getAll() {
        return ResponseEntity.ok(mechanicAdminService.getAllMechanics());
    }

    // Activate a mechanic
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a mechanic")
    @ApiResponse(responseCode = "200", description = "Mechanic activated successfully")
    @ApiResponse(responseCode = "404", description = "Mechanic not found")
    public ResponseEntity<MechanicResponse> activate(@PathVariable String id) {
        return ResponseEntity.ok(mechanicAdminService.activateMechanic(id));
    }

    // Deactivate a mechanic
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a mechanic")
    @ApiResponse(responseCode = "200", description = "Mechanic deactivated successfully")
    @ApiResponse(responseCode = "404", description = "Mechanic not found")
    public ResponseEntity<MechanicResponse> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(mechanicAdminService.deactivateMechanic(id));
    }

    // Delete a mechanic
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a mechanic permanently")
    @ApiResponse(responseCode = "204", description = "Mechanic deleted successfully")
    @ApiResponse(responseCode = "404", description = "Mechanic not found")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        mechanicAdminService.deleteMechanic(id);
        return ResponseEntity.noContent().build();
    }
}
