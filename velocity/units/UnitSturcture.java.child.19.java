#set($PARENT = ${Parent})
#set($basePackage = ${Base_package})
#set($nameCamelCase = $NAME.substring(0,1).toLowerCase()+$NAME.substring(1))
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.annotation.DtoMapper;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.UUID;

#parse("File Header.java")

	


@DtoMapper
public class ${NAME}DtoMapperImpl implements ${NAME}DtoMapper {

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}
	/**
	 * @param dto
	 * @return ${NAME}Dto
	 */
	@Override
	public ${NAME}Dto fromCreateDto(${NAME}CreateDto dto) {
		${NAME}Dto ${nameCamelCase}Dto = new ${NAME}Dto();
		${nameCamelCase}Dto.setUuid(UUID.randomUUID().toString());
		return ${nameCamelCase}Dto;

	}

	/**
	 * @param dto
	 * @return ${NAME}Dto
	 */
	@Override
	public ${NAME}Dto fromSearchDto(${NAME}SearchDto dto) {
		${NAME}Dto ${nameCamelCase}Dto = new ${NAME}Dto();
		${nameCamelCase}Dto.setUuid(dto.getUuid());
		#if(${PARENT} && ${PARENT} != "")
		#foreach($parent in $PARENT.split(","))
		${nameCamelCase}Dto.set${parent}Uuid(dto.get${parent}Uuid());
		#end
		#end
		return ${nameCamelCase}Dto;

	}

	/**
	 * @param dto
	 * @return ${NAME}Dto
	 */
	@Override
	public ${NAME}Dto fromUpdateDto(${NAME}UpdateDto dto) {
		${NAME}Dto ${nameCamelCase}Dto = new ${NAME}Dto();
		return ${nameCamelCase}Dto;

	}

	/**
	 * @param dto
	 * @return ${NAME}Dto
	 */
	@Override
	public ${NAME}Dto fromPartialUpdateDto(${NAME}PartialUpdateDto dto) {
		${NAME}Dto ${nameCamelCase}Dto = new ${NAME}Dto();
		return ${nameCamelCase}Dto;

	}
}
