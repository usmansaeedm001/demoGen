#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end


import ${basePackage}.enums.EntityType;
import ${basePackage}.enums.ErrorCode;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.BusinessValidationException;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.face.EntityValidator;
import org.springframework.util.StringUtils;

import java.util.Optional;

#parse("File Header.java")

public interface ${NAME}EntityValidator extends EntityValidator<${NAME}Dto, ${NAME}>  {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT)
			.with(requestType)
			.with(LayerType.ENTITY_VALIDATION_LAYER)
			.with(EntityType.${NAME.toUpperCase()}.toString())
			.build();
	}
}