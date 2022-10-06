package com.digitify.ob.velo.unit.account;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.handler.TrackCode;
import com.digitify.ob.enums.EntityType;
import com.digitify.framework.face.EntityMapper;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
public interface AccountMapper extends EntityMapper<AccountDto, Account>  {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT)
			.with(requestType)
			.with(LayerType.MAPPER_LAYER)
			.with(EntityType.ACCOUNT.toString())
			.build();
	}
}