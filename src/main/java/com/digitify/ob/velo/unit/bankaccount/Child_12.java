package com.digitify.ob.velo.unit.bankaccount;
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

public interface BankAccountDataService extends DataService<BankAccountDto> {

	default TrackCode trackCode(RequestType requestType) {
			return TrackCode.with(ApiType.UNIT)
				.with(requestType)
				.with(LayerType.DATA_SERVICE_LAYER)
				.with(EntityType.BANKACCOUNT.toString())
				.build();
		}
	boolean existsBy(BankAccountDto by);
			List<BankAccountDto> getAllByApplicationCustomerUuid(String uuid);
		void deleteAllByApplicationCustomerUuid(String uuid) throws ApplicationUncheckException;
									List<BankAccountDto> getAllByAccountUuid(String uuid);
				void deleteAllByAccountUuid(String uuid) throws ApplicationUncheckException;
											


}