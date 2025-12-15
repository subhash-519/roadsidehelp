//package com.roadsidehelp.api.feature.garage.controller;
//
//import com.roadsidehelp.api.feature.garage.dto.GarageResponse;
//import com.roadsidehelp.api.feature.garage.service.GarageAdminService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/admin/garages")
//public class GarageAdminController {
//
//    private final GarageAdminService adminService;
//
//    // Admin: Get pending garages
//    @Operation(summary = "Get pending garages", description = "List garages waiting for approval")
//    @ApiResponse(responseCode = "200", description = "Pending garages fetched")
//    @GetMapping("/pending")
//    public ResponseEntity<List<GarageResponse>> getPendingGarages() {
//        return ResponseEntity.ok(adminService.getPendingGarages());
//    }
//
//    // Admin: Approve a garage
//    @Operation(summary = "Approve garage", description = "Approve a garage verification request")
//    @ApiResponse(responseCode = "200", description = "Garage approved successfully")
//    @PatchMapping("/{id}/approve")
//    public ResponseEntity<GarageResponse> approveGarage(@PathVariable String id) {
//        return ResponseEntity.ok(adminService.approveGarage(id));
//    }
//
//    // Admin: Reject garage
//    @Operation(summary = "Reject garage", description = "Reject garage with a reason")
//    @ApiResponse(responseCode = "200", description = "Garage rejected")
//    @PatchMapping("/{id}/reject")
//    public ResponseEntity<GarageResponse> rejectGarage(
//            @PathVariable String id,
//            @RequestParam String reason) {
//        return ResponseEntity.ok(adminService.rejectGarage(id, reason));
//    }
//
//    // Admin: Get all garages
//    @Operation(summary = "Get all garages", description = "Admin view of all garages")
//    @ApiResponse(responseCode = "200", description = "Garages fetched")
//    @GetMapping
//    public ResponseEntity<List<GarageResponse>> getAllGarages() {
//        return ResponseEntity.ok(adminService.getAllGarages());
//    }
//
//    // Admin: Delete garage
//    @Operation(summary = "Delete garage", description = "Delete a garage permanently")
//    @ApiResponse(responseCode = "200", description = "Garage deleted successfully")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteGarage(@PathVariable String id) {
//        adminService.deleteGarage(id);
//        return ResponseEntity.ok().build();
//    }
//}
