#set($basePackage = ${Base_package})
#set($NameCamelCase = $NAME.substring(0,1).toLowerCase()+$NAME.substring(1))
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.face.ChangeReactor;
import com.digitify.framework.handler.TrackCode;

#parse("File Header.java")

public interface ${NAME}ChangeReactor extends ChangeReactor<${NAME}Dto, ${NAME}> {

}
