#set($basePackage = ${Base_package})
#set($NameCamelCase = $NAME.substring(0,1).toLowerCase()+$NAME.substring(1))
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.face.DeleteReactor;
import com.digitify.framework.handler.TrackCode;

#parse("File Header.java")

public interface ${NAME}DeleteReactor extends DeleteReactor<${NAME}> {

	Boolean react(${NAME} entity, TrackCode trackCode) throws ApplicationUncheckException;
}
