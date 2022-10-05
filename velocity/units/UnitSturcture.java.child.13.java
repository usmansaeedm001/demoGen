#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.handler.TrackCode;
import ${basePackage}.enums.EntityType;
import com.digitify.framework.face.EntityMapper;

#parse("File Header.java")

public interface ${NAME}Mapper extends EntityMapper<${NAME}Dto, ${NAME}>  {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT)
			.with(requestType)
			.with(LayerType.MAPPER_LAYER)
			.with(EntityType.${NAME.toUpperCase()}.toString())
			.build();
	}
}