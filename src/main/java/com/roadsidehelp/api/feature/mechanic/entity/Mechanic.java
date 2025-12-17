package com.roadsidehelp.api.feature.mechanic.entity;

import com.roadsidehelp.api.core.domain.BaseEntity;
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

    @NotBlank(message = "Mechanic full name is required")
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    @Column(nullable = false, length = 15, unique = true)
    private String phone;

    @NotNull(message = "Mechanic status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MechanicStatus status;

    @Column(nullable = false)
    @Builder.Default
    private boolean available = true;

    @NotNull(message = "Garage is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;
}
