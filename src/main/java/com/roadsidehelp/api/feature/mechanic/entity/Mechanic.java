package com.roadsidehelp.api.feature.mechanic.entity;

import com.roadsidehelp.api.core.domain.BaseEntity;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.garage.entity.Garage;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "mechanic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mechanic extends BaseEntity {

    // ===================== AUTH LINK =====================

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id", nullable = false, unique = true)
    private UserAccount userAccount;

    // ===================== DOMAIN FLAGS =====================

    @Builder.Default
    @Column(nullable = false)
    private boolean firstLogin = true;

    @NotNull(message = "Mechanic status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MechanicStatus status;

    @Builder.Default
    @Column(nullable = false)
    private boolean available = true;

    // ===================== GARAGE =====================

    @NotNull(message = "Garage is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;
}
