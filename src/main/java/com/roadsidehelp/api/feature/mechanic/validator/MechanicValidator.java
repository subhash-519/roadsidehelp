package com.roadsidehelp.api.feature.mechanic.validator;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import org.springframework.stereotype.Component;

@Component
public class MechanicValidator {

    public void validateExists(Mechanic mechanic) {
        if (mechanic == null) {
            throw new ApiException(ErrorCode.MECHANIC_NOT_FOUND, "Mechanic not found");
        }
    }
}
