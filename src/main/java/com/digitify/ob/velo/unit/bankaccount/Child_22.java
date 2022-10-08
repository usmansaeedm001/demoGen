package com.digitify.ob.velo.unit.bankaccount;

import com.digitify.framework.annotation.ValidationComponent;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.exception.BusinessValidationException;
import com.digitify.framework.handler.TrackCode;
import com.digitify.ob.enums.ErrorCode;
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
 * @author ${USER}
 * @created $DATE - $TIME
 * @project $PROJECT_NAME
 */
@ValidationComponent
class BankAccountEntityValidatorImpl implements BankAccountEntityValidator {
	@Autowired private BankAccountRepository repository;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	@Override
	public Boolean validateDto(BankAccountDto dto, TrackCode trackCode) throws BusinessValidationException {
		Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.map(BankAccountDto::getChildNumber)
			.map(s -> repository.exists(Example.of(BankAccount.builder().childNumber(s).build())))
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "ChildNumber already exists."));

		exists = exists && Optional.ofNullable(dto)
			.map(BankAccountDto::getAccountUuid)
			.map(s -> BankAccount.builder().accountUuid(s).build())
			.map(entity -> repository.exists(Example.of(entity)))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "BankAccount already exists."));
		exists = exists && Optional.ofNullable(dto)
			.map(BankAccountDto::getApplicationCustomerUuid)
			.map(s -> BankAccount.builder().applicationCustomerUuid(s).build())
			.map(entity -> repository.exists(Example.of(entity)))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "BankAccount already exists."));
		return exists;

	}

	@Override
	public Boolean validate(BankAccount entity, TrackCode trackCode) throws BusinessValidationException {
		return true;

	}

	@Override
	public Boolean validate(List<BankAccount> entityList, TrackCode trackCode) throws BusinessValidationException {
		return true;
	}

	@Override
	public Boolean validate(BankAccountDto dto, BankAccount entity, TrackCode trackCode) throws BusinessValidationException {
		Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.filter(bankAccountDto -> StringUtils.hasLength(bankAccountDto.getChildNumber()))
			.filter(bankAccountDto -> Objects.nonNull(entity))
			.map(bankAccountDto -> repository.existsByChildNumberAndUuidNot(bankAccountDto.getChildNumber(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "ChildNumber already exists."));
		exists = exists && Optional.ofNullable(dto)
			.filter(dto1 -> StringUtils.hasLength(dto1.getAccountUuid()))
			.filter(dto1 -> Objects.nonNull(entity))
			.map(dto1 -> repository.existsByAccountUuidAndUuidNot(dto1.getAccountUuid(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "BankAccount already exists."));
		exists = exists && Optional.ofNullable(dto)
			.filter(dto1 -> StringUtils.hasLength(dto1.getApplicationCustomerUuid()))
			.filter(dto1 -> Objects.nonNull(entity))
			.map(dto1 -> repository.existsByApplicationCustomerUuidAndUuidNot(dto1.getApplicationCustomerUuid(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "BankAccount already exists."));
		return exists;
	}

	@Override
	public Boolean validatePartialUpdate(BankAccountDto dto, BankAccount entity, TrackCode trackCode) throws BusinessValidationException {
		boolean exists = true;
		if (dto != null && StringUtils.hasLength(dto.getChildNumber())) {
			if (repository.existsByChildNumberAndUuidNot(dto.getChildNumber(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
					trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "ChildNumber already exists.");
			}
		}

		if (dto != null && StringUtils.hasLength(dto.getAccountUuid())) {
			if (repository.existsByAccountUuidAndUuidNot(dto.getAccountUuid(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
					trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "BankAccount already exists.");
			}
		}
		if (dto != null && StringUtils.hasLength(dto.getApplicationCustomerUuid())) {
			if (repository.existsByApplicationCustomerUuidAndUuidNot(dto.getApplicationCustomerUuid(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
					trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "BankAccount already exists.");
			}
		}
		return exists;
	}
}
