#set($PARENT = ${Parent})
#set($principal = ${Principal})
#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import ${basePackage}.config.ApplicationConfig;
import ${basePackage}.enums.EntityType;
import ${basePackage}.enums.ErrorCode;
import com.digitify.framework.util.Rethrow;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

#parse("File Header.java")

@Slf4j
@Service
public class ${NAME}ServiceImpl implements ${NAME}Service {
#if(${NAME} && ${NAME} != "")
		@Autowired private ApplicationConfig appConfig;
		@Autowired private ${NAME}DataServiceImpl dataService;
		@Autowired private ${NAME}DtoValidator validator;
		@Autowired private ${NAME}DtoMapper  dtoMapper;

		@Override
		public String getPrincipalUuid() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			return ((UserDto)authentication.getPrincipal()).getUuid();
		}

		@Override
		public boolean existsByUuid(String uuid) {
			return dataService.existsByUuid(uuid);
		}
	
		@Override
		public ${NAME}Dto get(String uuid) {
			return dataService.getByUuid(uuid).orElse(null);
		}
	
		@Override
		public Page<${NAME}Dto> search(${NAME}SearchDto searchDto, int pageNo, int pageSize) {
			pageNo = Math.max(pageNo, 0); 
			pageSize = Math.max(pageSize, 1);
			pageSize = Math.min(Integer.parseInt(appConfig.getMaxPageSize()), pageSize);
			PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
			return Optional.ofNullable(searchDto)
				.filter(dto -> validator.validateSearchDto(dto))
				.map(dto -> dtoMapper.fromSearchDto(dto))
				.map(dto -> dataService.search(dto, pageRequest))
				.orElse(new PageImpl<>(new ArrayList<>()));
		}

		@Override
		public ${NAME}Dto save(${NAME}CreateDto createDto) throws ApplicationUncheckException {
			TrackCode trackCode = trackCode(RequestType.POST);
			return Optional.ofNullable(createDto)
				.filter(dto ->  validator.validateCreateDto(dto))
				.map(dto -> dtoMapper.fromCreateDto(dto))
				.flatMap(Rethrow.rethrowFunction(dto -> dataService.save(dto)))
				.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
		}

		@Override
		public ${NAME}Dto update(String uuid, ${NAME}UpdateDto updateDto) throws ApplicationUncheckException {
			TrackCode trackCode = trackCode(RequestType.PUT);
			return Optional.ofNullable(updateDto)
				.filter(dto -> StringUtils.hasLength(uuid))
				.filter(dto -> validator.validateUpdateDto(uuid, dto))
				.map(dto -> dtoMapper.fromUpdateDto(dto))
				.map(dto -> {
					dto.setUuid(uuid);
					return dto;
				}).flatMap(Rethrow.rethrowFunction(dto -> dataService.update(dto)))
				.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
		}

		@Override
		public ${NAME}Dto partialUpdate(${NAME}PartialUpdateDto partialUpdateDto) throws ApplicationUncheckException {
			TrackCode trackCode = trackCode(RequestType.PATCH);
			return Optional.ofNullable(partialUpdateDto)
				.filter(dto -> validator.validatePartialUpdateDto(partialUpdateDto))
				.map(dto -> dtoMapper.fromPartialUpdateDto(dto))
				.flatMap(Rethrow.rethrowFunction(dto -> dataService.partialUpdate(dto)))
				.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
		}
	
		@Override
		public void delete(String uuid) throws ApplicationUncheckException {
			if(uuid == null){
				throw  new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.DELETE));
			}
			dataService.delete(uuid);
		}

	#if(${principal} && ${principal} != "")
		@Override
		public List<${NAME}Dto> getAllBy${principal}Uuid(String uuid) {
			return Optional.ofNullable(uuid)
			.filter(StringUtils::hasLength)
			.map(Rethrow.rethrowFunction(s -> dataService.getAllBy${principal}Uuid(s)))
			.orElse(new ArrayList<>());
		}

		@Override
		public void deleteAllBy${principal}Uuid(String uuid) throws ApplicationUncheckException {
			if (StringUtils.hasLength(uuid)) {
				dataService.deleteAllBy${principal}Uuid(uuid);
			}else {
				throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.DELETE_ALL));
			}
		}
		#if(${PARENT} && ${PARENT} != "")
		#foreach($parent in $PARENT.split(","))
		#if($parent  && $parent != "" && $parent != $principal)
				@Override
					public List<${NAME}Dto> getAllBy${parent}Uuid(String uuid) {
						return Optional.ofNullable(uuid)
						.filter(StringUtils::hasLength)
						.map(Rethrow.rethrowFunction(s -> dataService.getAllBy${parent}Uuid(s)))
						.orElse(new ArrayList<>());
					}

					@Override
					public void deleteAllBy${parent}Uuid(String uuid) throws ApplicationUncheckException {
						if(StringUtils.hasLength(uuid)){
							dataService.deleteAllBy${parent}Uuid(uuid);
						} else {
							throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.DELETE_ALL));
						}
					}
			#end
		#end
		#end
		#else
			#if($PARENT  && $PARENT != "")
				#foreach($parent in $PARENT.split(","))
					@Override
					public List<${NAME}Dto> getAllBy${parent}Uuid(String uuid) {
						return Optional.ofNullable(uuid)
						.filter(StringUtils::hasLength)
						.map(Rethrow.rethrowFunction(s -> dataService.getAllBy${parent}Uuid(s)))
						.orElse(new ArrayList<>());
					}

					@Override
					public void deleteAllBy${parent}Uuid(String uuid) throws ApplicationUncheckException {
						if(StringUtils.hasLength(uuid)){
							dataService.deleteAllBy${parent}Uuid(uuid);
						} else {
							throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.DELETE_ALL));
						}
					}
				#end
			#end
		#end
#end

}