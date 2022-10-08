package com.digitify.ob.velo.unit.applicationcustomer;
import com.digitify.framework.annotation.Mapper;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
@Mapper
public class ApplicationCustomerMapperImpl implements ApplicationCustomerMapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	@Override
	public ApplicationCustomer toEntity(ApplicationCustomerDto dto) {
		ApplicationCustomer applicationCustomer = new ApplicationCustomer();
	    applicationCustomer.setUuid(dto.getUuid());
	    return applicationCustomer;
	}

	@Override
	public ApplicationCustomerDto toDto(ApplicationCustomer applicationCustomer) {
		ApplicationCustomerDto dto = new ApplicationCustomerDto();
	    dto.setUuid(applicationCustomer.getUuid());
	    return dto;
	}

	@Override
	public ApplicationCustomer update(ApplicationCustomerDto dto, ApplicationCustomer applicationCustomer) {
		return applicationCustomer;
	}

	@Override
	public ApplicationCustomer partialUpdate(ApplicationCustomerDto dto, ApplicationCustomer applicationCustomer) {
		return applicationCustomer;
	}
	}
