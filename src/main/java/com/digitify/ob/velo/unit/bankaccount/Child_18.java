package com.digitify.ob.velo.unit.bankaccount;

import com.digitify.ob.enums.ErrorCode;
import com.digitify.framework.annotation.ValidationComponent;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.BusinessValidationException;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
@ValidationComponent
public class BankAccountDtoValidatorImpl implements BankAccountDtoValidator {

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
	public Boolean validateCreateDto(BankAccountCreateDto dto) throws BusinessValidationException {
	 	/** todo: validate if there is anything unique other tha uuid*/
		 return true;
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */

	@Override
	public Boolean validateSearchDto(BankAccountSearchDto dto) throws BusinessValidationException {
		return true;
	}

		/**
	 * @param uuid
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validateUpdateDto(String uuid, BankAccountUpdateDto dto) throws BusinessValidationException {
		/** todo:  validate if there is anything unique other tha uuid*/
		return true;
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validatePartialUpdateDto(BankAccountPartialUpdateDto dto) throws BusinessValidationException {
		/** todo:  validate if there is anything unique other tha uuid*/
		return true;
	}




}
