package com.roadsidehelp.api.feature.mechanic.mapper;

import com.roadsidehelp.api.feature.mechanic.dto.MechanicResponse;
import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import com.roadsidehelp.api.feature.mechanic.entity.MechanicStatus;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;

public class MechanicMapper {

    private MechanicMapper() {}

    // ===================== ENTITY → RESPONSE =====================

    public static MechanicResponse toResponse(Mechanic mechanic) {
        if (mechanic == null) return null;

        UserAccount user = mechanic.getUserAccount();

        return MechanicResponse.builder()
                // Mechanic
                .id(mechanic.getId())
                .status(
                        mechanic.getStatus() != null
                                ? mechanic.getStatus().name()
                                : null
                )
                .available(mechanic.isAvailable())

                // Garage
                .garageId(
                        mechanic.getGarage() != null
                                ? mechanic.getGarage().getId()
                                : null
                )
                .garageName(
                        mechanic.getGarage() != null
                                ? mechanic.getGarage().getName()
                                : null
                )

                // UserAccount
                .userAccountId(user != null ? user.getId() : null)
                .fullName(user != null ? user.getFullName() : null)
                .email(user != null ? user.getEmail() : null)
                .phoneNumber(user != null ? user.getPhoneNumber() : null)
                .roles(user != null ? user.getRoles() : null)
                .active(user != null && user.isActive())
                .verified(user != null && user.isVerified())

                .build();
    }

    // ===================== REQUEST → ENTITY =====================
    // NOTE: UserAccount is created in SERVICE layer

    public static Mechanic toEntity(UserAccount userAccount) {
        if (userAccount == null) return null;

        return Mechanic.builder()
                .userAccount(userAccount)
                .status(MechanicStatus.INACTIVE)
                .available(false)
                .firstLogin(true)
                .build();
    }

    // ===================== STATUS UPDATE =====================

    public static void updateStatus(Mechanic mechanic, String status) {
        if (mechanic == null || status == null || status.isBlank()) return;

        try {
            mechanic.setStatus(
                    MechanicStatus.valueOf(status.toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid mechanic status: " + status);
        }
    }
}
