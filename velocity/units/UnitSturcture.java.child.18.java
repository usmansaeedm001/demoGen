#set($PARENT = ${Parent})
#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

###if( ${PARENT} && ${PARENT} != "")
##	#foreach($parent in $PARENT.split(","))
##	import ${basePackage}.unit.${parent.toLowerCase()}.${parent}DataService;
##	#end
##	import com.digitify.framework.dto.EnumerationWrapper;
##	import ${basePackage}.enums.ErrorCode;
##	import org.springframework.beans.factory.annotation.Autowired;
##	import org.springframework.util.StringUtils;
##	import com.digitify.framework.util.Rethrow;
###end
##import com.digitify.validator.dto.UserDto;
##import org.springframework.security.core.Authentication;
##import org.springframework.security.core.context.SecurityContextHolder;
##import com.digitify.framework.annotation.ValidationComponent;
##import com.digitify.framework.enums.RequestType;
##import com.digitify.framework.exception.BusinessValidationException;
##import java.util.Optional;

import ${basePackage}.enums.ErrorCode;
import com.digitify.framework.annotation.ValidationComponent;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.BusinessValidationException;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Optional;

#parse("File Header.java")

@ValidationComponent
public class ${NAME}DtoValidatorImpl implements ${NAME}DtoValidator {
#if(${NAME} && ${NAME} != "")

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validateCreateDto(${NAME}CreateDto dto) throws BusinessValidationException {
	 	/** todo: validate if there is anything unique other tha uuid*/
		 return true;
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */

	@Override
	public Boolean validateSearchDto(${NAME}SearchDto dto) throws BusinessValidationException {
		return true;
	}

		/**
	 * @param uuid
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validateUpdateDto(String uuid, ${NAME}UpdateDto dto) throws BusinessValidationException {
		/** todo:  validate if there is anything unique other tha uuid*/
		return true;
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validatePartialUpdateDto(${NAME}PartialUpdateDto dto) throws BusinessValidationException {
		/** todo:  validate if there is anything unique other tha uuid*/
		return true;
	}

#end



}
