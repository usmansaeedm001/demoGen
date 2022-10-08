package com.digitify.ob.velo.unit.applicationcustomer;
import com.digitify.ob.enums.ErrorCode;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.enums.LayerType;
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

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/

@ValidationComponent
public class ApplicationCustomerEntityValidatorImpl implements ApplicationCustomerEntityValidator {

	@Autowired private ApplicationCustomerRepository repository;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	@Override
	public Boolean validateDto(ApplicationCustomerDto dto, TrackCode trackCode) throws BusinessValidationException {
	Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.map(ApplicationCustomerDto::getParentNumber)
			.map(s -> repository.exists(Example.of(ApplicationCustomer.builder().parentNumber(s).build())))
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER),
				"ParentNumber already exists."));
	
		return exists;
		
	}

	@Override
	public Boolean validate(ApplicationCustomer entity, TrackCode trackCode) throws BusinessValidationException {
		return true;

	}

	@Override
	public Boolean validate(List<ApplicationCustomer> entityList, TrackCode trackCode) throws BusinessValidationException {
		return true;
	}

	@Override
	public Boolean validate(ApplicationCustomerDto dto, ApplicationCustomer entity, TrackCode trackCode) throws BusinessValidationException {
		Boolean exists = true;
				exists = exists && Optional.ofNullable(dto)
			.filter(applicationCustomerDto -> StringUtils.hasLength(applicationCustomerDto.getParentNumber()))
			.filter(applicationCustomerDto -> Objects.nonNull(entity))
			.map(applicationCustomerDto -> repository.existsByParentNumberAndUuidNot(applicationCustomerDto.getParentNumber(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER),
			"ParentNumber already exists."));
						return exists;
	}

	@Override
	public Boolean validatePartialUpdate(ApplicationCustomerDto dto, ApplicationCustomer entity, TrackCode trackCode) throws BusinessValidationException {
		boolean exists = true;
				if (dto != null && StringUtils.hasLength(dto.getParentNumber())) {
			if (repository.existsByParentNumberAndUuidNot(dto.getParentNumber(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER),
			"ParentNumber already exists.");
			}
		}
				
				return exists;
	}
}
