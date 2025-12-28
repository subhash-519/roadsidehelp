package com.roadsidehelp.api.feature.mechanic.controller;

import com.roadsidehelp.api.core.utils.CurrentUser;
import com.roadsidehelp.api.feature.mechanic.dto.CreateMechanicRequest;
import com.roadsidehelp.api.feature.mechanic.dto.MechanicResponse;
import com.roadsidehelp.api.feature.mechanic.dto.UpdateMechanicStatusRequest;
import com.roadsidehelp.api.feature.mechanic.service.MechanicGarageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('GARAGE')")
@RestController
@RequestMapping("/api/v1/garage/mechanics")
@RequiredArgsConstructor
public class MechanicGarageController {

    private final MechanicGarageService mechanicGarageService;

    // Create a new mechanic for the garage
    @PostMapping
    @Operation(summary = "Create a new mechanic for the garage")
    @ApiResponse(responseCode = "200", description = "Mechanic created successfully")
    public ResponseEntity<MechanicResponse> create(@RequestBody @Valid CreateMechanicRequest request) {

        return ResponseEntity.ok(
                mechanicGarageService.createMechanic(
                        CurrentUser.getUserId(),
                        request
                )
        );
    }

    // List all mechanics for the garage
    @GetMapping
    @Operation(summary = "Get all mechanics for the garage")
    @ApiResponse(responseCode = "200", description = "Garage mechanics fetched successfully")
    public ResponseEntity<List<MechanicResponse>> list() {
        return ResponseEntity.ok(
                mechanicGarageService.getGarageMechanics(CurrentUser.getUserId())
        );
    }

    // Update mechanic status (ACTIVE / INACTIVE)
    @PatchMapping("/{mechanicId}/status")
    @Operation(summary = "Update mechanic status (ACTIVE / INACTIVE)")
    @ApiResponse(responseCode = "200", description = "Mechanic status updated successfully")
    @ApiResponse(responseCode = "404", description = "Mechanic not found")
    public ResponseEntity<MechanicResponse> updateStatus(
            @PathVariable String mechanicId,
            @RequestBody @Valid UpdateMechanicStatusRequest request) {
        return ResponseEntity.ok(
                mechanicGarageService.updateMechanicStatus(CurrentUser.getUserId(), mechanicId, request)
        );
    }

    // Delete a mechanic
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a mechanic permanently")
    @ApiResponse(responseCode = "204", description = "Mechanic deleted successfully")
    @ApiResponse(responseCode = "404", description = "Mechanic not found")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        mechanicGarageService.deleteMechanic(id);
        return ResponseEntity.noContent().build();
    }
}
