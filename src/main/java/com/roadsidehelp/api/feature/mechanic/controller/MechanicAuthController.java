package com.roadsidehelp.api.feature.mechanic.controller;

import com.roadsidehelp.api.feature.mechanic.dto.*;
import com.roadsidehelp.api.feature.mechanic.service.MechanicEmailVerificationService;
import com.roadsidehelp.api.feature.mechanic.service.MechanicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mechanic/auth")
@RequiredArgsConstructor
public class MechanicAuthController {

    private final MechanicService mechanicService;
    private final MechanicEmailVerificationService mechanicEmailVerificationService;

    @GetMapping("/verify-email")
    @Operation(
            summary = "Verify mechanic email using verification token",
            description = "Verifies the mechanic's email address using a token sent via email. "
                    + "Once verified, the mechanic can proceed to login."
    )
    @ApiResponse(responseCode = "200", description = "Email verified successfully")
    @ApiResponse(responseCode = "400", description = "Invalid or expired verification token")
    @ApiResponse(responseCode = "404", description = "Mechanic not found")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        mechanicEmailVerificationService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully. You may now login.");
    }

    @PostMapping("/first-login")
    @Operation(summary = "Mechanic first login using temporary credentials")
    @ApiResponse(responseCode = "200", description = "First login successful")
    @ApiResponse(responseCode = "400", description = "Invalid or expired credentials")
    public ResponseEntity<FirstLoginResponse> firstLogin(
            @RequestBody @Valid MechanicFirstLoginRequest request) {

        String mechanicId = mechanicService.firstLogin(request);

        return ResponseEntity.ok(
                new FirstLoginResponse(
                        "First login successful. Please set password.",
                        mechanicId
                )
        );
    }

    @PostMapping("/set-password")
    @Operation(summary = "Set password after first login")
    @ApiResponse(responseCode = "200", description = "Password set successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request or mechanic not found")
    public ResponseEntity<String> setPassword(
            @RequestHeader("X-MECHANIC-ID") String mechanicId,
            @RequestBody @Valid MechanicSetPasswordRequest request) {

        mechanicService.setPassword(mechanicId, request);
        return ResponseEntity.ok("Password set successfully");
    }

    @PostMapping("/login")
    @Operation(summary = "Mechanic login and receive JWT token")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<String> login(
            @RequestBody @Valid MechanicLoginRequest request) {

        return ResponseEntity.ok(mechanicService.login(request));
    }
}
