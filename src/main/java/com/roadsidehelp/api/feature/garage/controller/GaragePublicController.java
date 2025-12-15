//package com.roadsidehelp.api.feature.garage.controller;
//
//import com.roadsidehelp.api.feature.garage.dto.GarageResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/garages/public")
//public class GaragePublicController {
//
//    private final GarageService garageService;
//
//    // Public: Get all garages
//    @Operation(summary = "Get all garages", description = "Public endpoint to fetch all garages")
//    @ApiResponse(responseCode = "200", description = "Garages fetched successfully")
//    @GetMapping
//    public ResponseEntity<List<GarageResponse>> getAllGarages() {
//        return ResponseEntity.ok(garageService.getAllGarages());
//    }
//
//    // Public: Get garage by ID
//    @Operation(summary = "Get garage by ID", description = "Fetch a garage using its ID")
//    @ApiResponse(responseCode = "200", description = "Garage found")
//    @ApiResponse(responseCode = "404", description = "Garage not found")
//    @GetMapping("/{id}")
//    public ResponseEntity<GarageResponse> getGarageById(
//            @Parameter(description = "Garage ID") @PathVariable String id) {
//        return ResponseEntity.ok(garageService.getGarageById(id));
//    }
//
//    // Public: Get nearby garages
//    @Operation(summary = "Get nearby garages", description = "Fetch garages within a radius")
//    @ApiResponse(responseCode = "200", description = "Nearby garages fetched")
//    @GetMapping("/nearby")
//    public ResponseEntity<List<GarageResponse>> getNearbyGarages(
//            @RequestParam double lat,
//            @RequestParam double lng,
//            @RequestParam(defaultValue = "5") double radiusKm) {
//        return ResponseEntity.ok(garageService.getNearbyGarages(lat, lng, radiusKm));
//    }
//
//    // Public: Search garages
//    @Operation(summary = "Search garages", description = "Search garages by filters")
//    @ApiResponse(responseCode = "200", description = "Garages fetched")
//    @GetMapping("/search")
//    public ResponseEntity<List<GarageResponse>> searchGarages(
//            @RequestParam(required = false) String city,
//            @RequestParam(required = false) String type,
//            @RequestParam(required = false) String name) {
//        return ResponseEntity.ok(garageService.searchGarages(city, type, name));
//    }
//
//    // Public: Get reviews for a garage
//    @Operation(summary = "Get garage reviews", description = "Fetch all reviews for a garage")
//    @ApiResponse(responseCode = "200", description = "Reviews fetched successfully")
//    @ApiResponse(responseCode = "404", description = "Garage not found")
//    @GetMapping("/{id}/reviews")
//    public ResponseEntity<?> getGarageReviews(
//            @Parameter(description = "Garage ID") @PathVariable String id) {
//        return ResponseEntity.ok(garageService.getGarageReviews(id));
//    }
//}
