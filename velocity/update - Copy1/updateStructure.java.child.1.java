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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
	


	#if(${principal} && ${principal} != "")

			@Override
			public boolean existsByUuid(String uuid) {
				return repository.existsByUuidAnd${principal}Uuid(uuid, getPrincipalUuid());
			}

			@Override
			@Transactional(readOnly = true)
			public Optional<${NAME}Dto> getByUuid(String uuid) {
				return Optional.ofNullable(uuid)
					.filter(StringUtils::hasLength)
					.flatMap(s -> repository.findByUuidAnd${principal}Uuid(s, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> mapper.toDto(entity));
			}

			@Override
			public List<${NAME}Dto> search(${NAME}Dto dto, PageRequest pageRequest) {
				return Optional.ofNullable(dto)
					.filter(dto1 -> Objects.nonNull(pageRequest))
					.map(aDto -> {
						BankAccount entity = mapper.toEntity(aDto);
						entity.set${principal}Uuid(getPrincipalUuid());
						entity.setIsDeleted(false);
						return entity;
					})
					.map(Example::of)
					.map(example -> repository.findAll(example, pageRequest))
					.stream()
					.map(Slice::getContent)
					.flatMap(Collection::stream)
					.filter(userCard -> validator.validate(userCard))
					.map(entity -> mapper.toDto(entity))
					.collect(Collectors.toList());
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
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				Optional<${NAME}> optionalEntity = Optional.of(dto)
					.map(${NAME}Dto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAnd${principal}Uuid(dtoUuid, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
				if (optionalEntity.isPresent()) {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
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
					.map(${NAME}Dto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuidAnd${principal}Uuid(dtoUuid, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity)))
					.map(entity -> mapper.update(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<${NAME}Dto> update(List<${NAME}Dto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.Put_ALL);
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
					.flatMap(dtoUuid -> repository.findByUuidAnd${principal}Uuid(dtoUuid, getPrincipalUuid()))
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
				Optional.ofNullable(uuid)
					.filter(StringUtils::hasLength)
					.flatMap(s -> repository.findByUuidAnd${principal}Uuid(s, getPrincipalUuid()))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> {
						entity.setIsDeleted(true);
						return entity;
					})
					.map(entity -> repository.save(entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			public void delete(${NAME}Dto dto) throws ApplicationUncheckException {
				if (dto != null && StringUtils.hasLength(dto.getUuid() )) {
					delete(dto.getUuid());
				}
			}

##			@Override
##			@Transactional(propagation = Propagation.REQUIRED)
##			public void delete(List<${NAME}Dto> list) throws ApplicationUncheckException {
##				TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
##				Stream.ofNullable(list)
##					.filter(dtoList -> !dtoList.isEmpty())
##					.map(dtoList -> repository.findAllBy${principal}UuidAndUuidIn(getPrincipalUuid(),
##							dtoList.stream().map(${NAME}Dto::getUuid).collect(Collectors.toList()))
##						.stream()
##						.peek(entity -> entity.setIsDeleted(true))
##						.collect(Collectors.toList()))
##					.map(aList -> repository.saveAll(aList))
##					.findFirst()
##					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
##			}

	#foreach($parent in $PARENT.split(","))
		#if(${principal} && ${principal} != "" && $parent == $principal)
				@Override
				@Transactional(readOnly = true)
				public List<${NAME}Dto> getAllBy${principal}Uuid(String uuid) {
					return Stream.ofNullable(uuid)
					.filter(s -> s.equalsIgnoreCase(getPrincipalUuid()))
					.map(s ->  repository.findAllBy${principal}Uuid(s))
					.flatMap(Collection::stream)
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> mapper.toDto(entity))
					.collect(Collectors.toList());
				}

				@Override
				@Transactional(propagation = Propagation.REQUIRED)
				public void deleteAllBy${principal}Uuid(String uuid) throws ApplicationUncheckException {
					TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
					Optional.ofNullable(uuid)
						.filter(s -> s.equalsIgnoreCase(getPrincipalUuid()))
						.map(s -> repository.findAllBy${principal}Uuid(uuid).stream().peek(entity -> entity.setIsDeleted(true)).collect(Collectors.toList()))
						.map(aList -> repository.saveAll(aList))
						.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
				}
			#else
				@Override
				@Transactional(readOnly = true)
				public List<${NAME}Dto> getAllBy${parent}Uuid(String uuid) {
					return Stream.ofNullable(uuid)
					.map(s ->  repository.findAllBy${parent}UuidAnd${principal}Uuid(s, getPrincipalUuid()))
					.flatMap(Collection::stream)
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> mapper.toDto(entity))
					.collect(Collectors.toList());
				}

				@Override
				@Transactional(propagation = Propagation.REQUIRED)
				public void deleteAllBy${parent}Uuid(String uuid) throws ApplicationUncheckException {
					TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
					Optional.ofNullable(uuid)
						.map(s -> repository.findAllBy${parent}UuidAnd${principal}Uuid(uuid, getPrincipalUuid()).stream().peek(entity -> entity.setIsDeleted(true)).collect(Collectors.toList()))
						.map(aList -> repository.saveAll(aList))
						.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
				}
			#end
		#end
	#else
			@Override
			public boolean existsByUuid(String uuid) {
				return Optional.ofNullable(uuid)
				.filter(StringUtils::hasLength)
				.filter(s -> s.equalsIgnoreCase(getPrincipalUuid()))
				.map(s -> repository.existsByUuid(uuid))
				.orElse(false);
			}

			@Override
			@Transactional(readOnly = true)
			public Optional<${NAME}Dto> getByUuid(String uuid) {
				return Optional.ofNullable(uuid)
					.filter(s -> s.equalsIgnoreCase(getPrincipalUuid()))
					.filter(StringUtils::hasLength)
					.flatMap(s -> repository.findByUuid(s))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> mapper.toDto(entity));
			}

			@Override
			public List<${NAME}Dto> search(${NAME}Dto dto, PageRequest pageRequest) {
				if(dto != null && dto.getUuid() != null && !dto.getUuid().equalsIgnoreCase(getPrincipalUuid())){
					throw new ApplicationException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.SEARCH), HttpStatus.UNPROCESSABLE_ENTITY);
				}
				return Optional.ofNullable(dto)
					.filter(dto1 -> Objects.nonNull(pageRequest))
					.map(aDto -> {
						BankAccount entity = mapper.toEntity(aDto);
						entity.setUuid(getPrincipalUuid());
						entity.setIsDeleted(false);
						return entity;
					})
					.map(Example::of)
					.map(example -> repository.findAll(example, pageRequest))
					.stream()
					.map(Slice::getContent)
					.flatMap(Collection::stream)
					.map(entity -> mapper.toDto(entity))
					.collect(Collectors.toList());
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
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				Optional<${NAME}> optionalEntity = Optional.of(dto)
					.map(${NAME}Dto::getUuid)
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
				if (optionalEntity.isPresent()) {
					throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
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
					.filter(s -> s.equalsIgnoreCase(getPrincipalUuid()))
					.flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity)))
					.map(entity -> mapper.update(dto, entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			@Transactional(propagation = Propagation.REQUIRED)
			public List<${NAME}Dto> update(List<${NAME}Dto> list) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.Put_ALL);
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
					.filter(s -> s.equalsIgnoreCase(getPrincipalUuid()))
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
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
				Optional.ofNullable(uuid)
					.filter(StringUtils::hasLength)
					.filter(s -> s.equalsIgnoreCase(getPrincipalUuid()))
					.flatMap(s -> repository.findByUuid(s))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> {
						entity.setIsDeleted(true);
						return entity;
					})
					.map(entity -> repository.save(entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}

			@Override
			public void delete(${NAME}Dto dto) throws ApplicationUncheckException {
				TrackCode trackCode = trackCode(RequestType.DELETE);
				Optional.ofNullable(dto)
					.map(${NAME}Dto::getUuid)
					.filter(s -> s.equalsIgnoreCase(getPrincipalUuid()))
					.filter(StringUtils::hasLength)
					.flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
					.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
					.map(entity -> {
						entity.setIsDeleted(true);
						return entity;
					})
					.map(entity -> repository.save(entity))
					.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
			}
	#end
#end

	}

