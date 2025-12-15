package com.roadsidehelp.api.feature.garage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageDocumentRequest {

    @NotBlank(message = "Shop license document is required")
    private String licenseDocumentUrl;

    @NotBlank(message = "GST certificate is required")
    private String gstCertificateUrl;

    @NotBlank(message = "Owner ID proof is required")
    private String ownerIdProofUrl;

    @NotBlank(message = "Front photo of garage is required")
    private String garagePhotoUrl;

}
