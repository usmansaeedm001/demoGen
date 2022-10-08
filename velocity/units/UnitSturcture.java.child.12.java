#set($PARENT = ${Parent})
#set($principal = ${Principal})
#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import ${basePackage}.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.face.DataService;
import com.digitify.framework.exception.ApplicationUncheckException;
import java.util.List;

#parse("File Header.java")


public interface ${NAME}DataService extends DataService<${NAME}Dto> {

#if(${NAME} && ${NAME} != "")
	default TrackCode trackCode(RequestType requestType) {
			return TrackCode.with(ApiType.UNIT)
				.with(requestType)
				.with(LayerType.DATA_SERVICE_LAYER)
				.with(EntityType.${NAME.toUpperCase()}.toString())
				.build();
		}
	boolean existsBy(${NAME}Dto by);
	#if(${principal} && ${principal} != "")
		List<${NAME}Dto> getAllBy${principal}Uuid(String uuid);
		void deleteAllBy${principal}Uuid(String uuid) throws ApplicationUncheckException;
		#if( ${PARENT} && ${PARENT} != "")
		#foreach($parent in $PARENT.split(","))
			#if($parent != $principal)
				List<${NAME}Dto> getAllBy${parent}Uuid(String uuid);
				void deleteAllBy${parent}Uuid(String uuid) throws ApplicationUncheckException;
			#end
		#end
		#end
	#else
		#if($PARENT  && $PARENT != "")
			#foreach($parent in $PARENT.split(","))
				List<${NAME}Dto> getAllBy${parent}Uuid(String uuid);
				void deleteAllBy${parent}Uuid(String uuid) throws ApplicationUncheckException;
			#end
		#end
	#end
#end



}