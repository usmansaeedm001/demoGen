package com.digitify.ob.velo.unit.applicationcustomer;
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
public class ApplicationCustomerDataServiceImpl implements ApplicationCustomerDataService {
	@Autowired private ApplicationCustomerRepository repository;
	@Autowired private ApplicationCustomerMapper mapper;
	@Autowired private ApplicationCustomerEntityValidator validator;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	private List<ApplicationCustomerDto> processList(List<ApplicationCustomerDto> list, TrackCode trackCode, Function<ApplicationCustomerDto, ApplicationCustomer> function) throws ApplicationUncheckException {
		return Optional.ofNullable(list)
			.filter(dtoList -> !dtoList.isEmpty())
			.map(Rethrow.rethrowFunction(dtoList -> dtoList.stream().map(function).collect(Collectors.toList())))
			.map(entityList -> repository.saveAll(entityList))
			.map(entityList -> mapper.toDtoList(entityList))
			.orElseThrow(
				() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public boolean existsBy(ApplicationCustomerDto by) {
		return Optional.ofNullable(by)
			.map(dto -> mapper.toEntity(dto))
			.map(Example::of)
			.map(example -> repository.exists(example))
			.orElse(false);
	}
	


				@Override
			public boolean existsByUuid(String uuid) {
				return Optional.ofNullable(uuid)
				.filter(StringUtils::hasLength)
				.map(s -> repository.existsByUuid(uuid))
				.orElse(false);
			}

			@Override
			@Transactional(readOnly = true)
			public Optional<ApplicationCustomerDto> getByUuid(String uuid) {
				return Optional.ofNullable(uuid)
					.filter(StringUtils::hasLength)
					.flatMap(s -> repository.findByUuidAndIsActiveTrue(s))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> mapper.toDto(entity));
			}

			@Override
			public List<ApplicationCustomerDto> search(ApplicationCustomerDto dto, PageRequest pageRequest) {
			return Optional.ofNullable(dto)
				.filter(dto1 -> Objects.nonNull(pageRequest))
				.map(aDto -> {
						ApplicationCustomer entity = mapper.toEntity(aDto);
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
			public Optional<ApplicationCustomerDto> save(ApplicationCustomerDto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToSave))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private ApplicationCustomer prepareEntityToSave(ApplicationCustomerDto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.POST);
				if (dto == null) {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				Optional<ApplicationCustomer> optionalEntity = Optional.of(dto)
					.map(ApplicationCustomerDto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
				if (optionalEntity.isPresent()) {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				return mapper.toEntity(dto);
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<ApplicationCustomerDto> save(List<ApplicationCustomerDto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.POST_ALL);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToUpdate));
			}

			@Override
			public Optional<ApplicationCustomerDto> update(ApplicationCustomerDto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToUpdate))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private ApplicationCustomer prepareEntityToUpdate(ApplicationCustomerDto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PUT);
				return Optional.of(dto)
					.map(ApplicationCustomerDto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity)))
					.map(entity -> mapper.update(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<ApplicationCustomerDto> update(List<ApplicationCustomerDto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PUT_ALL);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToUpdate));
			}

			@Override
			public Optional<ApplicationCustomerDto> partialUpdate(ApplicationCustomerDto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private ApplicationCustomer prepareEntityToPartialUpdate(ApplicationCustomerDto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PATCH);
				return Optional.ofNullable(dto)
					.map(ApplicationCustomerDto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity)))
					.map(entity -> mapper.partialUpdate(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<ApplicationCustomerDto> partialUpdate(List<ApplicationCustomerDto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PATCH);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate));
			}

			@Override
			public void delete(String uuid) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.DELETE);
				Optional<ApplicationCustomer> optionalEntity = Optional.ofNullable(uuid)
				.filter(StringUtils::hasLength)
				.flatMap(s -> repository.findByUuidAndIsActiveTrue(s))
				.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
				if (optionalEntity.isPresent()) {
					repository.delete(optionalEntity.get());
				} else {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
			}

			@Override
			public void delete(ApplicationCustomerDto dto) throws ApplicationUncheckException {
				if (dto != null && StringUtils.hasLength(dto.getUuid() )) {
					delete(dto.getUuid());
				}
			}
					
}

