package com.digitify.ob.velo.unit.account;
import com.digitify.framework.face.DtoMapper;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.handler.TrackCode;
import com.digitify.ob.enums.EntityType;
import com.digitify.framework.face.DtoValidator;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
public interface AccountDtoMapper extends DtoMapper<AccountDto, AccountCreateDto, AccountSearchDto, AccountUpdateDto, AccountPartialUpdateDto> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT)
			.with(requestType)
			.with(LayerType.DTO_VALIDATION_LAYER)
			.with(EntityType.ACCOUNT.toString())
			.build();
	}

}