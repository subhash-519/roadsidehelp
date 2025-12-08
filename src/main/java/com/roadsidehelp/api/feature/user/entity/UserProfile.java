package com.roadsidehelp.api.feature.user.entity;

import com.roadsidehelp.api.core.domain.BaseEntity;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount user;

    @Size(max = 30)
    private String gender;

    @Past
    private java.time.LocalDate dateOfBirth;

    @Column(length = 500)
    private String profileImageUrl;
}
