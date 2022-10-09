package com.digitify.ob.velo.unit.applicationcustomer;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.face.DeleteReactor;
import com.digitify.framework.handler.TrackCode;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
public interface ApplicationCustomerDeleteReactor extends DeleteReactor<ApplicationCustomer> {

	Boolean react(ApplicationCustomer entity, TrackCode trackCode) throws ApplicationUncheckException;
}
