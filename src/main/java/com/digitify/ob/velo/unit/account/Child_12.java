package com.digitify.ob.velo.unit.account;
import com.digitify.ob.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.face.DataService;
import com.digitify.framework.exception.ApplicationUncheckException;
import java.util.List;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/

public interface AccountDataService extends DataService<AccountDto> {

	default TrackCode trackCode(RequestType requestType) {
			return TrackCode.with(ApiType.UNIT)
				.with(requestType)
				.with(LayerType.DATA_SERVICE_LAYER)
				.with(EntityType.ACCOUNT.toString())
				.build();
		}
	boolean existsBy(AccountDto by);
			List<AccountDto> getAllByApplicationCustomerUuid(String uuid);
		void deleteAllByApplicationCustomerUuid(String uuid) throws ApplicationUncheckException;
												


}