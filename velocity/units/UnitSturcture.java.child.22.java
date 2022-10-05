#set($PARENT = ${Parent})
#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.annotation.ValidationComponent;
import com.digitify.framework.exception.BusinessValidationException;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

#parse("File Header.java")


@ValidationComponent
public class ${NAME}EntityValidatorImpl implements ${NAME}EntityValidator {

	@Autowired private ${NAME}Repository repository;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	@Override
	public Boolean validateDto(${NAME}Dto dto) throws BusinessValidationException {
		return true;
	}

	@Override
	public Boolean validate(${NAME} entity) throws BusinessValidationException {
		return true;

	}

	@Override
	public Boolean validate(List<${NAME}> entityList) throws BusinessValidationException {
		return true;
	}

	@Override
	public Boolean validate(${NAME}Dto dto, ${NAME} entity) throws BusinessValidationException {
		return true;
	}

	@Override
	public Boolean validatePartialUpdate(${NAME}Dto dto, ${NAME} entity) throws BusinessValidationException {
		return true;
	}
}
