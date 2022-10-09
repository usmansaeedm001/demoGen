package com.digitify.ob.velo.unit.account;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.face.DeleteReactor;
import com.digitify.framework.handler.TrackCode;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
public interface AccountDeleteReactor extends DeleteReactor<Account> {

	Boolean react(Account entity, TrackCode trackCode) throws ApplicationUncheckException;
}
