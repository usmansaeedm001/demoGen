package com.digitify.ob.velo.unit.account;
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
public interface AccountService extends IService<AccountDto, AccountCreateDto, AccountSearchDto, AccountUpdateDto, AccountPartialUpdateDto> {
	default TrackCode trackCode(RequestType requestType) {
			return TrackCode.with(ApiType.UNIT)
				.with(requestType)
				.with(LayerType.SERVICE_UNIT_LAYER)
				//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and instance with name ACCOUNT
				.with(EntityType.ACCOUNT.toString())
				.build();
		}

					List<AccountDto> getAllByApplicationCustomerUuid(String uuid);
			void deleteAllByApplicationCustomerUuid(String uuid) throws ApplicationUncheckException;
										


}