package com.digitify.ob.velo.unit.applicationcustomer;

import com.digitify.ob.enums.EntityType;
import com.digitify.ob.enums.ErrorCode;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.BusinessValidationException;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.face.EntityValidator;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
public interface ApplicationCustomerEntityValidator extends EntityValidator<ApplicationCustomerDto, ApplicationCustomer>  {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT)
			.with(requestType)
			.with(LayerType.ENTITY_VALIDATION_LAYER)
			.with(EntityType.APPLICATIONCUSTOMER.toString())
			.build();
	}
}