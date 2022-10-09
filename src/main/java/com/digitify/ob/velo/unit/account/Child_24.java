package com.digitify.ob.velo.unit.account;
import com.digitify.framework.annotation.ChangeReactor;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
@ChangeReactor
public class AccountChangeReactorImpl implements AccountChangeReactor {

	@Override
	public Boolean react(AccountDto dto, Account entity, TrackCode trackCode) throws ApplicationUncheckException {
		trackCode = trackCode.setLayerCode(LayerType.CHANGE_REACTOR_LAYER);
		return continueReaction(null, null, trackCode);
	}
	private Boolean continueReaction(AccountStatus currentStatus, AccountStatus previousStatus, TrackCode trackCode) throws ApplicationUncheckException {
		return true;
	}
}
