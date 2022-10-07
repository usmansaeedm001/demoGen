#set($PARENT = ${Parent})
#set($principal = ${Principal})
#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import ${basePackage}.enums.ErrorCode;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.annotation.DataService;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.util.Rethrow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

#parse("File Header.java")

@Slf4j
@DataService
public class ${NAME}DataServiceImpl implements ${NAME}DataService {
#if(${NAME} && ${NAME} != "")
	@Autowired private ${NAME}Repository repository;
	@Autowired private ${NAME}Mapper mapper;
	@Autowired private ${NAME}EntityValidator validator;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	private List<${NAME}Dto> processList(List<${NAME}Dto> list, TrackCode trackCode, Function<${NAME}Dto, ${NAME}> function) throws ApplicationUncheckException {
		return Optional.ofNullable(list)
			.filter(dtoList -> !dtoList.isEmpty())
			.map(Rethrow.rethrowFunction(dtoList -> dtoList.stream().map(function).collect(Collectors.toList())))
			.map(entityList -> repository.saveAll(entityList))
			.map(entityList -> mapper.toDtoList(entityList))
			.orElseThrow(
				() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public boolean existsBy(${NAME}Dto by) {
		return Optional.ofNullable(by)
			.map(dto -> mapper.toEntity(dto))
			.map(Example::of)
			.map(example -> repository.exists(example))
			.orElse(false);
	}
	


	#if(${principal} && ${principal} != "")
			@Override
			public boolean existsByUuid(String uuid) {
				return repository.existsByUuidAnd${principal}UuidAndIsActiveTrue(uuid, getPrincipalUuid());
			}

			@Override
			@Transactional(readOnly = true)
			public Optional<${NAME}Dto> getByUuid(String uuid) {
				return Optional.ofNullable(uuid)
					.filter(StringUtils::hasLength)
					.flatMap(s -> repository.findByUuidAnd${principal}UuidAndIsActiveTrue(s, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> mapper.toDto(entity));
			}

			@Override
			public Page<${NAME}Dto> search(${NAME}Dto dto, PageRequest pageRequest) {
				return Optional.ofNullable(dto)
					.filter(dto1 -> Objects.nonNull(pageRequest))
					.map(aDto -> {
						${NAME} entity = mapper.toEntity(aDto);
						entity.set${principal}Uuid(getPrincipalUuid());  /** search only loggedIn customer's ${NAME} */
						entity.setIsActive(true);
						return entity;
					})
					.map(Example::of)
					.map(example -> repository.findAll(example, pageRequest))
					.map(page -> new PageImpl<>(Optional.of(page)
							.map(Slice::getContent)
							.stream()
							.flatMap(Collection::stream)
							.filter(entity -> validator.validate(entity))
							.map(entity -> mapper.toDto(entity))
							.collect(Collectors.toList()), pageRequest, page.getTotalElements()))
					.orElse(new PageImpl<>(new ArrayList<>()));
			}

			@Override
			public Optional<${NAME}Dto> save(${NAME}Dto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToSave))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private ${NAME} prepareEntityToSave(${NAME}Dto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.POST);
				if (dto == null) {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				Optional<${NAME}> optionalEntity = Optional.of(dto)
				.filter(userCardDto -> validator.validateDto(dto))
					.filter(aDto -> validator.validatePrincipalUuid(aDto.get${principal}Uuid(), ErrorCode.NOT_AUTHORIZED, trackCode))
					.filter(aDto -> validator.validateDto(aDto))
					.map(${NAME}Dto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAnd${principal}Uuid(dtoUuid, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
				if (optionalEntity.isPresent()) {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				return mapper.toEntity(dto);
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<${NAME}Dto> save(List<${NAME}Dto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.POST_ALL);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToSave));
			}

			@Override
			public Optional<${NAME}Dto> update(${NAME}Dto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToUpdate))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private ${NAME} prepareEntityToUpdate(${NAME}Dto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PUT);
				return Optional.of(dto)
					.filter(aDto -> validator.validatePrincipalUuid(aDto.get${principal}Uuid(), ErrorCode.NOT_AUTHORIZED, trackCode))
					.map(${NAME}Dto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAnd${principal}UuidAndIsActiveTrue(dtoUuid, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity)))
					.map(entity -> mapper.update(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<${NAME}Dto> update(List<${NAME}Dto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PUT_ALL);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToUpdate));
			}

			@Override
			public Optional<${NAME}Dto> partialUpdate(${NAME}Dto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private ${NAME} prepareEntityToPartialUpdate(${NAME}Dto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PATCH);
				return Optional.ofNullable(dto)
					.filter(aDto -> validator.validatePrincipalIfPresent(aDto.get${principal}Uuid(), ErrorCode.NOT_AUTHORIZED, trackCode))
					.map(${NAME}Dto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAnd${principal}UuidAndIsActiveTrue(dtoUuid, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity)))
					.map(entity -> mapper.partialUpdate(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<${NAME}Dto> partialUpdate(List<${NAME}Dto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PATCH);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate));
			}

			@Override
			public void delete(String uuid) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.DELETE);
				Optional<${NAME}> optionalEntity = Optional.ofNullable(uuid)
				.filter(StringUtils::hasLength)
				.flatMap(s -> repository.findByUuidAnd${principal}UuidAndIsActiveTrue(s, getPrincipalUuid()))
				.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
				if (optionalEntity.isPresent()) {
					repository.delete(optionalEntity.get());
				} else {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
			}

			@Override
			public void delete(${NAME}Dto dto) throws ApplicationUncheckException {
				if (dto != null && StringUtils.hasLength(dto.getUuid() )) {
					delete(dto.getUuid());
				}
			}

			@Override
			@Transactional(readOnly = true)
			public List<${NAME}Dto> getAllBy${principal}Uuid(String uuid) {
				TrackCode trackCode = trackCode(RequestType.GET_ALL);
				return Stream.ofNullable(uuid)
				.filter(StringUtils::hasLength)
				.filter(s -> validator.validatePrincipalUuid(s, ErrorCode.NOT_AUTHORIZED, trackCode))
				.map(s ->  repository.findAllBy${principal}UuidAndIsActiveTrue(s))
				.flatMap(Collection::stream)
				.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
				.map(entity -> mapper.toDto(entity))
				.collect(Collectors.toList());
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public void deleteAllBy${principal}Uuid(String uuid) throws ApplicationUncheckException {

				TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
				List<${NAME}> list = Stream.ofNullable(uuid)
					.filter(StringUtils::hasLength)
					.filter(s -> validator.validatePrincipalUuid(s, ErrorCode.NOT_AUTHORIZED, trackCode))
					.map(s -> repository.findAllBy${principal}UuidAndIsActiveTrue(uuid))
					.flatMap(Collection::stream)
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.collect(Collectors.toList());
				if(!list.isEmpty()){
					repository.deleteAll(list);
				}else {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
			}

			#foreach($parent in $PARENT.split(","))
				#if(${parent} != ${principal})
						@Override
						@Transactional(readOnly = true)
						public List<${NAME}Dto> getAllBy${parent}Uuid(String uuid) {
							return Stream.ofNullable(uuid)
							.map(s ->  repository.findAllBy${parent}UuidAnd${principal}UuidAndIsActiveTrue(s, getPrincipalUuid()))
							.flatMap(Collection::stream)
							.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
							.map(entity -> mapper.toDto(entity))
							.collect(Collectors.toList());
						}

						@Override
						@Transactional(propagation = Propagation.REQUIRED)
						public void deleteAllBy${parent}Uuid(String uuid) throws ApplicationUncheckException {
							TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
							List<${NAME}> list = Stream.ofNullable(uuid)
								.map(s -> repository.findAllBy${parent}UuidAnd${principal}UuidAndIsActiveTrue(uuid, getPrincipalUuid()))
								.flatMap(Collection::stream)
								.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
								.collect(Collectors.toList());
							if(!list.isEmpty()){
								repository.deleteAll(list);
							}else {
								throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
							}
						}
				#end
			#end
	#else
			@Override
			public boolean existsByUuid(String uuid) {
				return Optional.ofNullable(uuid)
				.filter(StringUtils::hasLength)
				.map(s -> repository.existsByUuid(uuid))
				.orElse(false);
			}

			@Override
			@Transactional(readOnly = true)
			public Optional<${NAME}Dto> getByUuid(String uuid) {
				return Optional.ofNullable(uuid)
					.filter(StringUtils::hasLength)
					.flatMap(s -> repository.findByUuidAndIsActiveTrue(s))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> mapper.toDto(entity));
			}

			@Override
			public Page<${NAME}Dto> search(${NAME}Dto dto, PageRequest pageRequest) {
			return Optional.ofNullable(dto)
				.filter(dto1 -> Objects.nonNull(pageRequest))
				.map(aDto -> {
						${NAME} entity = mapper.toEntity(aDto);
						entity.setIsActive(true);
						return entity;
					})
				.map(Example::of)
				.map(example -> repository.findAll(example, pageRequest))
				.map(page ->new PageImpl<>(Optional.of(page)
						.map(Slice::getContent)
						.stream()
						.flatMap(Collection::stream)
						.filter(entity -> validator.validate(entity))
						.map(entity -> mapper.toDto(entity))
						.collect(Collectors.toList()), pageRequest, page.getTotalElements()))
				.orElse(new PageImpl<>(new ArrayList<>()));
			}

			@Override
			public Optional<${NAME}Dto> save(${NAME}Dto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToSave))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private ${NAME} prepareEntityToSave(${NAME}Dto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.POST);
				if (dto == null) {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				Optional<${NAME}> optionalEntity = Optional.of(dto)
					.map(${NAME}Dto::getUuid)
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
			public List<${NAME}Dto> save(List<${NAME}Dto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.POST_ALL);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToUpdate));
			}

			@Override
			public Optional<${NAME}Dto> update(${NAME}Dto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToUpdate))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private ${NAME} prepareEntityToUpdate(${NAME}Dto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PUT);
				return Optional.of(dto)
					.map(${NAME}Dto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity)))
					.map(entity -> mapper.update(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<${NAME}Dto> update(List<${NAME}Dto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PUT_ALL);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToUpdate));
			}

			@Override
			public Optional<${NAME}Dto> partialUpdate(${NAME}Dto dto) throws ApplicationUncheckException {
				return Optional.ofNullable(dto)
					.map(Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate))
					.map(entity -> repository.save(entity))
					.map(entity -> mapper.toDto(entity));
			}

			private ${NAME} prepareEntityToPartialUpdate(${NAME}Dto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PATCH);
				return Optional.ofNullable(dto)
					.map(${NAME}Dto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity)))
					.map(entity -> mapper.partialUpdate(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<${NAME}Dto> partialUpdate(List<${NAME}Dto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.PATCH);
				return processList(list, trackCode, Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate));
			}

			@Override
			public void delete(String uuid) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.DELETE);
				Optional<${NAME}> optionalEntity = Optional.ofNullable(uuid)
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
			public void delete(${NAME}Dto dto) throws ApplicationUncheckException {
				if (dto != null && StringUtils.hasLength(dto.getUuid() )) {
					delete(dto.getUuid());
				}
			}
			#if($PARENT && $PARENT != ""))
				#foreach($parent in $PARENT.split(","))
					@Override
					@Transactional(readOnly = true)
					public List<${NAME}Dto> getAllBy${parent}Uuid(String uuid) {
						return Stream.ofNullable(uuid)
						.map(s ->  repository.findAllBy${parent}UuidAndIsActiveTrue(s))
						.flatMap(Collection::stream)
						.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
						.map(entity -> mapper.toDto(entity))
						.collect(Collectors.toList());
					}

					@Override
					@Transactional(propagation = Propagation.REQUIRED)
					public void deleteAllBy${parent}Uuid(String uuid) throws ApplicationUncheckException {
							TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
							List<${NAME}> list = Stream.ofNullable(uuid)
								.map(s -> new ArrayList<>(repository.findAllBy${parent}UuidAndIsActiveTrue(uuid)))
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
				#end
			#end
		#end
#end

}

