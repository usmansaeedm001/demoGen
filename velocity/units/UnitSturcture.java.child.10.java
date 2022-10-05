#set($PARENT = ${Parent})
#set($principal = ${Principal})
#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import ${basePackage}.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.face.IService;
import java.util.List;

#parse("File Header.java")

public interface ${NAME}Service extends IService<${NAME}Dto, ${NAME}CreateDto, ${NAME}SearchDto, ${NAME}UpdateDto, ${NAME}PartialUpdateDto> {
#if(${NAME} && ${NAME} != "")
	default TrackCode trackCode(RequestType requestType) {
			return TrackCode.with(ApiType.UNIT)
				.with(requestType)
				.with(LayerType.SERVICE_UNIT_LAYER)
				//todo: create EntityType enum and inherit it with EnumerationFace interface under ${basePackage}.enums and instance with name ${NAME.toUpperCase()}
				.with(EntityType.${NAME.toUpperCase()}.toString())
				.build();
		}

		#if(${principal} && ${principal} != "")
			List<${NAME}Dto> getAllBy${principal}Uuid(String uuid);
			void deleteAllBy${principal}Uuid(String uuid) throws ApplicationUncheckException;
			#foreach($parent in $PARENT.split(","))
			#if($parent  && $parent != "" && $parent != $principal)
				List<${NAME}Dto> getAllBy${parent}Uuid(String uuid);
				void deleteAllBy${parent}Uuid(String uuid) throws ApplicationUncheckException;
			#end
		#end
		#else
			#if($parent  && $parent != "")
				#foreach($parent in $PARENT.split(","))
					List<${NAME}Dto> getAllBy${parent}Uuid(String uuid);
					void deleteAllBy${parent}Uuid(String uuid) throws ApplicationUncheckException;
					#end
			#end
		#end
#end



}