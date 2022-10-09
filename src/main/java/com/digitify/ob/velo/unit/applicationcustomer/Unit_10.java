package com.digitify.ob.velo.unit.applicationcustomer;
import com.digitify.ob.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.face.IService;
import java.util.List;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
public interface ApplicationCustomerService extends IService<ApplicationCustomerDto, ApplicationCustomerCreateDto, ApplicationCustomerSearchDto, ApplicationCustomerUpdateDto, ApplicationCustomerPartialUpdateDto> {
	default TrackCode trackCode(RequestType requestType) {
			return TrackCode.with(ApiType.UNIT)
				.with(requestType)
				.with(LayerType.SERVICE_UNIT_LAYER)
				//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and instance with name APPLICATIONCUSTOMER
				.with(EntityType.APPLICATIONCUSTOMER.toString())
				.build();
		}

							


}