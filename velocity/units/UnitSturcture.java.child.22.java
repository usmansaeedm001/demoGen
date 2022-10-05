#set($PARENT = ${Parent})
#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import ${basePackage}.enums.ErrorCode;
import com.digitify.framework.annotation.ValidationComponent;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.BusinessValidationException;
import com.digitify.validator.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
	Boolean exists = true;
	#if( ${PARENT} && ${PARENT} != "")
	#foreach($parent in $PARENT.split(","))
		#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
			exists = exists && Optional.ofNullable(dto)
			.map(${NAME}Dto::get${parent}Uuid)
			.map(s -> ${NAME}.builder().${parentCamelCase}Uuid(s).isActive(true).build())
			.map(entity -> repository.exists(Example.of(entity)))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode(RequestType.POST),
				"${NAME} already exists."));
	#end
	#end
	return exists;
		
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
		Boolean exists = true;
		#if( ${PARENT} && ${PARENT} != "")
#foreach($parent in $PARENT.split(","))
	#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
		exists = exists && Optional.ofNullable(dto)
			.filter(dto1 -> StringUtils.hasLength(dto1.get${parent}Uuid()))
			.filter(dto1 -> Objects.nonNull(entity))
			.map(dto1 -> repository.existsBy${parent}UuidAndUuidNotAndIsActiveTrue(dto1.get${parent}Uuid(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode(RequestType.NOOP), "${NAME} already exists."));
		#end
		#end
		return exists;
	}

	@Override
	public Boolean validatePartialUpdate(${NAME}Dto dto, ${NAME} entity) throws BusinessValidationException {
		boolean exists = true;
		#if( ${PARENT} && ${PARENT} != "")
		#foreach($parent in $PARENT.split(","))
		#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
		if (dto != null && StringUtils.hasLength(dto.get${parent}Uuid())) {
			if (repository.existsBy${parent}UuidAndUuidNotAndIsActiveTrue(dto.get${parent}Uuid(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode(RequestType.PATCH), "${NAME} already exists.");
			}
		}
		#end
		#end
		return exists;
	}
}
