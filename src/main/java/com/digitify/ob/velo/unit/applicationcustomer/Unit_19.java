package com.digitify.ob.velo.unit.applicationcustomer;
import com.digitify.framework.annotation.DtoMapper;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.UUID;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
	


@DtoMapper
public class ApplicationCustomerDtoMapperImpl implements ApplicationCustomerDtoMapper {

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}
	/**
	 * @param dto
	 * @return ApplicationCustomerDto
	 */
	@Override
	public ApplicationCustomerDto fromCreateDto(ApplicationCustomerCreateDto dto) {
		ApplicationCustomerDto applicationCustomerDto = new ApplicationCustomerDto();
		applicationCustomerDto.setUuid(UUID.randomUUID().toString());
		return applicationCustomerDto;

	}

	/**
	 * @param dto
	 * @return ApplicationCustomerDto
	 */
	@Override
	public ApplicationCustomerDto fromSearchDto(ApplicationCustomerSearchDto dto) {
		ApplicationCustomerDto applicationCustomerDto = new ApplicationCustomerDto();
		applicationCustomerDto.setUuid(dto.getUuid());
				return applicationCustomerDto;

	}

	/**
	 * @param dto
	 * @return ApplicationCustomerDto
	 */
	@Override
	public ApplicationCustomerDto fromUpdateDto(ApplicationCustomerUpdateDto dto) {
		ApplicationCustomerDto applicationCustomerDto = new ApplicationCustomerDto();
		return applicationCustomerDto;

	}

	/**
	 * @param dto
	 * @return ApplicationCustomerDto
	 */
	@Override
	public ApplicationCustomerDto fromPartialUpdateDto(ApplicationCustomerPartialUpdateDto dto) {
		ApplicationCustomerDto applicationCustomerDto = new ApplicationCustomerDto();
		return applicationCustomerDto;

	}
}
