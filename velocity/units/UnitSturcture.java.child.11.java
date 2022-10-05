#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.handler.TrackCode;
import ${basePackage}.enums.EntityType;
import com.digitify.framework.face.DtoValidator;

#parse("File Header.java")

public interface ${NAME}DtoValidator extends DtoValidator<${NAME}CreateDto, ${NAME}SearchDto, ${NAME}UpdateDto, ${NAME}PartialUpdateDto>  {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT)
			.with(requestType)
			.with(LayerType.DTO_VALIDATION_LAYER)
			.with(EntityType.${NAME.toUpperCase()}.toString())
			.build();
	}

}