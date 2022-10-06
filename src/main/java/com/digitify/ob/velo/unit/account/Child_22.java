package com.digitify.ob.velo.unit.account;
import com.digitify.ob.enums.ErrorCode;
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
public class AccountEntityValidatorImpl implements AccountEntityValidator {

	@Autowired private AccountRepository repository;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	@Override
	public Boolean validateDto(AccountDto dto) throws BusinessValidationException {
	Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.map(AccountDto::getChildNumber)
			.map(s -> repository.exists(Example.of(Account.builder().childNumber(s).isActive(true).build())))
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode(RequestType.NOOP),
				"ChildNumber already exists."));
	
					exists = exists && Optional.ofNullable(dto)
			.map(AccountDto::getApplicationCustomerUuid)
			.map(s -> Account.builder().applicationCustomerUuid(s).build())
			.map(entity -> repository.exists(Example.of(entity)))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode(RequestType.POST),
				"Account already exists."));
			return exists;
		
	}

	@Override
	public Boolean validate(Account entity) throws BusinessValidationException {
		return true;

	}

	@Override
	public Boolean validate(List<Account> entityList) throws BusinessValidationException {
		return true;
	}

	@Override
	public Boolean validate(AccountDto dto, Account entity) throws BusinessValidationException {
		Boolean exists = true;
				exists = exists && Optional.ofNullable(dto)
			.filter(accountDto -> StringUtils.hasLength(accountDto.getChildNumber()))
			.filter(accountDto -> Objects.nonNull(entity))
			.map(accountDto -> repository.existsByChildNumberAndUuidNot(accountDto.getChildNumber(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode(RequestType.POST),
			"ChildNumber already exists."));
						exists = exists && Optional.ofNullable(dto)
			.filter(dto1 -> StringUtils.hasLength(dto1.getApplicationCustomerUuid()))
			.filter(dto1 -> Objects.nonNull(entity))
			.map(dto1 -> repository.existsByApplicationCustomerUuidAndUuidNot(dto1.getApplicationCustomerUuid(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode(RequestType.NOOP),
			"Account already exists."));
						return exists;
	}

	@Override
	public Boolean validatePartialUpdate(AccountDto dto, Account entity) throws BusinessValidationException {
		boolean exists = true;
				if (dto != null && StringUtils.hasLength(dto.getChildNumber())) {
			if (repository.existsByChildNumberAndUuidNot(dto.getChildNumber(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode(RequestType.PATCH),
			"ChildNumber already exists.");
			}
		}
				
						if (dto != null && StringUtils.hasLength(dto.getApplicationCustomerUuid())) {
			if (repository.existsByApplicationCustomerUuidAndUuidNot(dto.getApplicationCustomerUuid(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS), trackCode(RequestType.PATCH),
				"Account already exists.");
			}
		}
						return exists;
	}
}
