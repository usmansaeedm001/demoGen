package com.digitify.ob.velo.unit.applicationcustomer;
import com.digitify.framework.annotation.DeleteReactor;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/@DeleteReactor
public class ApplicationCustomerDeleteReactorImpl implements ApplicationCustomerDeleteReactor {

	@Override
	public Boolean react(ApplicationCustomer entity, TrackCode trackCode) throws ApplicationUncheckException {
	trackCode = trackCode.setLayerCode(LayerType.DELETE_REACTOR_LAYER);
		return true;
	}
}
