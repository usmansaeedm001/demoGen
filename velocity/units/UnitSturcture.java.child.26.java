#set($basePackage = ${Base_package})
#set($NameCamelCase = $NAME.substring(0,1).toLowerCase()+$NAME.substring(1))
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.annotation.DeleteReactor;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;

#parse("File Header.java")
@DeleteReactor
public class ${NAME}DeleteReactorImpl implements ${NAME}DeleteReactor {

	@Override
	public Boolean react(${NAME} entity, TrackCode trackCode) throws ApplicationUncheckException {
	trackCode = trackCode.setLayerCode(LayerType.DELETE_REACTOR_LAYER);
		return true;
	}
}
