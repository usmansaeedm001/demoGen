package com.digitify.ob.velo.unit.applicationcustomer;
import com.digitify.ob.config.ApplicationConfig;
import com.digitify.ob.enums.EntityType;
import com.digitify.ob.enums.ErrorCode;
import com.digitify.framework.util.Rethrow;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
@Slf4j
@Service
public class ApplicationCustomerServiceImpl implements ApplicationCustomerService {
		@Autowired private ApplicationConfig appConfig;
		@Autowired private ApplicationCustomerDataServiceImpl dataService;
		@Autowired private ApplicationCustomerDtoValidator validator;
		@Autowired private ApplicationCustomerDtoMapper  dtoMapper;

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
		public ApplicationCustomerDto get(String uuid) {
			return dataService.getByUuid(uuid).orElse(null);
		}
	
		@Override
		public List<ApplicationCustomerDto> search(ApplicationCustomerSearchDto searchDto, int pageNo, int pageSize) {
			pageNo = Math.max(pageNo, 0); 
			pageSize = Math.max(pageSize, 1);
			pageSize = Math.min(Integer.parseInt(appConfig.getMaxPageSize()), pageSize);
			PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
			return Optional.ofNullable(searchDto)
				.filter(dto -> validator.validateSearchDto(dto))
				.map(dto -> dtoMapper.fromSearchDto(dto))
				.map(dto -> dataService.search(dto, pageRequest))
				.orElse(new ArrayList<>());
		}

		@Override
		public ApplicationCustomerDto save(ApplicationCustomerCreateDto createDto) throws ApplicationUncheckException {
			TrackCode trackCode = trackCode(RequestType.POST);
			return Optional.ofNullable(createDto)
				.filter(dto ->  validator.validateCreateDto(dto))
				.map(dto -> dtoMapper.fromCreateDto(dto))
				.flatMap(Rethrow.rethrowFunction(dto -> dataService.save(dto)))
				.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
		}

		@Override
		public ApplicationCustomerDto update(String uuid, ApplicationCustomerUpdateDto updateDto) throws ApplicationUncheckException {
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
		public ApplicationCustomerDto partialUpdate(ApplicationCustomerPartialUpdateDto partialUpdateDto) throws ApplicationUncheckException {
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

						
}