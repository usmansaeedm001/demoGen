package com.digitify.ob.velo.unit.bankaccount;
import com.digitify.ob.enums.ErrorCode;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.annotation.DataService;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.util.Rethrow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
@Slf4j
@DataService
public class BankAccountDataServiceImpl implements BankAccountDataService {
	@Autowired private BankAccountRepository repository;
	@Autowired private BankAccountMapper mapper;
	@Autowired private BankAccountEntityValidator validator;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	private List<BankAccountDto> processList(List<BankAccountDto> list, TrackCode trackCode, Function<BankAccountDto, BankAccount> function) throws ApplicationUncheckException {
		return Optional.ofNullable(list)
			.filter(dtoList -> !dtoList.isEmpty())
			.map(Rethrow.rethrowFunction(dtoList -> dtoList.stream().map(function).collect(Collectors.toList())))
			.map(entityList -> repository.saveAll(entityList))
			.map(entityList -> mapper.toDtoList(entityList))
			.orElseThrow(
				() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public boolean existsBy(BankAccountDto by) {
		return Optional.ofNullable(by)
			.map(dto -> mapper.toEntity(dto))
			.map(Example::of)
			.map(example -> repository.exists(example))
			.orElse(false);
	}
	


				@Override
			public boolean existsByUuid(String uuid) {
				return repository.existsByUuidAndApplicationCustomerUuidAndIsActiveTrue(uuid, getPrincipalUuid());
			}

			@Override
			@Transactional(readOnly = true)
			public Optional<BankAccountDto> getByUuid(String uuid) {
				return Optional.ofNullable(uuid)
					.filter(StringUtils::hasLength)
					.flatMap(s -> repository.findByUuidAndApplicationCustomerUuidAndIsActiveTrue(s, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> mapper.toDto(entity));
			}

			@Override
			public List<BankAccountDto> search(BankAccountDto dto, PageRequest pageRequest) {
				return Optional.ofNullable(dto)
					.filter(dto1 -> Objects.nonNull(pageRequest))
					.map(aDto -> {
						BankAccount entity = mapper.toEntity(aDto);
						entity.setApplicationCustomerUuid(getPrincipalUuid());  /** search only loggedIn customer's BankAccount */
						entity.setIsActive(true);
						return entity;
					})
					.map(Example::of)
					.map(example -> repository.findAll(example, pageRequest))
					.stream()
					.map(Slice::getContent)
					.flatMap(Collection::stream)
					.filter(entity -> validator.validate(entity))
					.map(entity -> mapper.toDto(entity))
					.collect(Collectors.toList());
			}

			@Override
			public Optional<BankAccountDto> save(BankAccountDto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToSave))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private BankAccount prepareEntityToSave(BankAccountDto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.POST);
				if (dto == null) {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				Optional<BankAccount> optionalEntity = Optional.of(dto)
				.filter(userCardDto -> validator.validateDto(dto))
					.filter(aDto -> validator.validatePrincipalUuid(aDto.getApplicationCustomerUuid(), ErrorCode.NOT_AUTHORIZED, trackCode))
					.filter(aDto -> validator.validateDto(aDto))
					.map(BankAccountDto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAndApplicationCustomerUuid(dtoUuid, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
				if (optionalEntity.isPresent()) {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				return mapper.toEntity(dto);
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<BankAccountDto> save(List<BankAccountDto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.POST_ALL);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToSave));
			}

			@Override
			public Optional<BankAccountDto> update(BankAccountDto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToUpdate))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private BankAccount prepareEntityToUpdate(BankAccountDto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PUT);
				return Optional.of(dto)
					.filter(aDto -> validator.validatePrincipalUuid(aDto.getApplicationCustomerUuid(), ErrorCode.NOT_AUTHORIZED, trackCode))
					.map(BankAccountDto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAndApplicationCustomerUuidAndIsActiveTrue(dtoUuid, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity)))
					.map(entity -> mapper.update(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<BankAccountDto> update(List<BankAccountDto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PUT_ALL);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToUpdate));
			}

			@Override
			public Optional<BankAccountDto> partialUpdate(BankAccountDto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private BankAccount prepareEntityToPartialUpdate(BankAccountDto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PATCH);
				return Optional.ofNullable(dto)
					.filter(aDto -> validator.validatePrincipalIfPresent(aDto.getApplicationCustomerUuid(), ErrorCode.NOT_AUTHORIZED, trackCode))
					.map(BankAccountDto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAndApplicationCustomerUuidAndIsActiveTrue(dtoUuid, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity)))
					.map(entity -> mapper.partialUpdate(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<BankAccountDto> partialUpdate(List<BankAccountDto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PATCH);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate));
			}

			@Override
			public void delete(String uuid) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.DELETE);
				Optional<BankAccount> optionalEntity = Optional.ofNullable(uuid)
				.filter(StringUtils::hasLength)
				.flatMap(s -> repository.findByUuidAndApplicationCustomerUuidAndIsActiveTrue(s, getPrincipalUuid()))
				.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
				if (optionalEntity.isPresent()) {
					repository.delete(optionalEntity.get());
				} else {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
			}

			@Override
			public void delete(BankAccountDto dto) throws ApplicationUncheckException {
				if (dto != null && StringUtils.hasLength(dto.getUuid() )) {
					delete(dto.getUuid());
				}
			}

			@Override
			@Transactional(readOnly = true)
			public List<BankAccountDto> getAllByApplicationCustomerUuid(String uuid) {
				TrackCode trackCode = trackCode(RequestType.GET_ALL);
				return Stream.ofNullable(uuid)
				.filter(StringUtils::hasLength)
				.filter(s -> validator.validatePrincipalUuid(s, ErrorCode.NOT_AUTHORIZED, trackCode))
				.map(s ->  repository.findAllByApplicationCustomerUuidAndIsActiveTrue(s))
				.flatMap(Collection::stream)
				.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
				.map(entity -> mapper.toDto(entity))
				.collect(Collectors.toList());
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public void deleteAllByApplicationCustomerUuid(String uuid) throws ApplicationUncheckException {

				TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
				List<BankAccount> list = Stream.ofNullable(uuid)
					.filter(StringUtils::hasLength)
					.filter(s -> validator.validatePrincipalUuid(s, ErrorCode.NOT_AUTHORIZED, trackCode))
					.map(s -> repository.findAllByApplicationCustomerUuidAndIsActiveTrue(uuid))
					.flatMap(Collection::stream)
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.collect(Collectors.toList());
				if(!list.isEmpty()){
					repository.deleteAll(list);
				}else {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
			}

													@Override
						@Transactional(readOnly = true)
						public List<BankAccountDto> getAllByAccountUuid(String uuid) {
							return Stream.ofNullable(uuid)
							.map(s ->  repository.findAllByAccountUuidAndApplicationCustomerUuidAndIsActiveTrue(s, getPrincipalUuid()))
							.flatMap(Collection::stream)
							.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
							.map(entity -> mapper.toDto(entity))
							.collect(Collectors.toList());
						}

						@Override
						@Transactional(propagation = Propagation.REQUIRED)
						public void deleteAllByAccountUuid(String uuid) throws ApplicationUncheckException {
							TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
							List<BankAccount> list = Stream.ofNullable(uuid)
								.map(s -> repository.findAllByAccountUuidAndApplicationCustomerUuidAndIsActiveTrue(uuid, getPrincipalUuid()))
								.flatMap(Collection::stream)
								.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
								.collect(Collectors.toList());
							if(!list.isEmpty()){
								repository.deleteAll(list);
							}else {
								throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
							}
						}
															
}

