package com.digitify.ob.velo.unit.account;
import com.digitify.framework.annotation.DeleteReactor;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/@DeleteReactor
public class AccountDeleteReactorImpl implements AccountDeleteReactor {

	@Override
	public Boolean react(Account entity, TrackCode trackCode) throws ApplicationUncheckException {
	trackCode = trackCode.setLayerCode(LayerType.DELETE_REACTOR_LAYER);
		return true;
	}
}
