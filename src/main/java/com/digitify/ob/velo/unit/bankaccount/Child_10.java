package com.digitify.ob.velo.unit.bankaccount;
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
public interface BankAccountService extends IService<BankAccountDto, BankAccountCreateDto, BankAccountSearchDto, BankAccountUpdateDto, BankAccountPartialUpdateDto> {
	default TrackCode trackCode(RequestType requestType) {
			return TrackCode.with(ApiType.UNIT)
				.with(requestType)
				.with(LayerType.SERVICE_UNIT_LAYER)
				//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and instance with name BANKACCOUNT
				.with(EntityType.BANKACCOUNT.toString())
				.build();
		}

					List<BankAccountDto> getAllByApplicationCustomerUuid(String uuid);
			void deleteAllByApplicationCustomerUuid(String uuid) throws ApplicationUncheckException;
										List<BankAccountDto> getAllByAccountUuid(String uuid);
				void deleteAllByAccountUuid(String uuid) throws ApplicationUncheckException;
												


}