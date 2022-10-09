package com.digitify.ob.velo.unit.bankaccount;
import com.digitify.framework.annotation.DeleteReactor;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/@DeleteReactor
public class BankAccountDeleteReactorImpl implements BankAccountDeleteReactor {

	@Override
	public Boolean react(BankAccount entity, TrackCode trackCode) throws ApplicationUncheckException {
	trackCode = trackCode.setLayerCode(LayerType.DELETE_REACTOR_LAYER);
		return true;
	}
}
