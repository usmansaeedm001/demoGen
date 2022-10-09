package com.digitify.ob.velo.unit.bankaccount;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.face.DeleteReactor;
import com.digitify.framework.handler.TrackCode;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
public interface BankAccountDeleteReactor extends DeleteReactor<BankAccount> {

	Boolean react(BankAccount entity, TrackCode trackCode) throws ApplicationUncheckException;
}
