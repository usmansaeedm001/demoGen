#set($PARENT = ${Parent})
#set($basePackage = ${Base_package})
#set($nameCamelCase = $NAME.substring(0,1).toLowerCase()+$NAME.substring(1))
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.annotation.Mapper;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

#parse("File Header.java")

@Mapper
public class ${NAME}MapperImpl implements ${NAME}Mapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}
#if(${NAME} && ${NAME} != "")

	@Override
	public ${NAME} toEntity(${NAME}Dto dto) {
		${NAME} ${nameCamelCase} = new ${NAME}();
	    ${nameCamelCase}.setUuid(dto.getUuid());
	    return ${nameCamelCase};
	}

	@Override
	public ${NAME}Dto toDto(${NAME} $nameCamelCase) {
		${NAME}Dto dto = new ${NAME}Dto();
	    dto.setUuid(${nameCamelCase}.getUuid());
	    return dto;
	}

	@Override
	public ${NAME} update(${NAME}Dto dto, ${NAME} ${nameCamelCase}) {
		return ${nameCamelCase};
	}

	@Override
	public ${NAME} partialUpdate(${NAME}Dto dto, ${NAME} ${nameCamelCase}) {
		return ${nameCamelCase};
	}
	#end
}
