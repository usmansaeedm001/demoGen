#set($basePackage = ${Base_package})
#set($NameCamelCase = $NAME.substring(0,1).toLowerCase()+$NAME.substring(1))
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.annotation.ChangeReactor;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;

#parse("File Header.java")

@ChangeReactor
public class ${NAME}ChangeReactorImpl implements ${NAME}ChangeReactor {

	@Override
	public Boolean react(${NAME}Dto dto, ${NAME} entity, TrackCode trackCode) throws ApplicationUncheckException {
		trackCode = trackCode.setLayerCode(LayerType.CHANGE_REACTOR_LAYER);
		return continueReaction(null, null, trackCode);
	}
	private Boolean continueReaction(${NAME}Status currentStatus, ${NAME}Status previousStatus, TrackCode trackCode) throws ApplicationUncheckException {
		return true;
	}
}
