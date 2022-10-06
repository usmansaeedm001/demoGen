package com.digitify.ob.velo.unit.bankaccount;
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
public class BankAccountServiceImpl implements BankAccountService {
		@Autowired private ApplicationConfig appConfig;
		@Autowired private BankAccountDataServiceImpl dataService;
		@Autowired private BankAccountDtoValidator validator;
		@Autowired private BankAccountDtoMapper  dtoMapper;

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
		public BankAccountDto get(String uuid) {
			return dataService.getByUuid(uuid).orElse(null);
		}
	
		@Override
		public List<BankAccountDto> search(BankAccountSearchDto searchDto, int pageNo, int pageSize) {
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
		public BankAccountDto save(BankAccountCreateDto createDto) throws ApplicationUncheckException {
			TrackCode trackCode = trackCode(RequestType.POST);
			return Optional.ofNullable(createDto)
				.filter(dto ->  validator.validateCreateDto(dto))
				.map(dto -> dtoMapper.fromCreateDto(dto))
				.flatMap(Rethrow.rethrowFunction(dto -> dataService.save(dto)))
				.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
		}

		@Override
		public BankAccountDto update(String uuid, BankAccountUpdateDto updateDto) throws ApplicationUncheckException {
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
		public BankAccountDto partialUpdate(BankAccountPartialUpdateDto partialUpdateDto) throws ApplicationUncheckException {
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

			@Override
		public List<BankAccountDto> getAllByApplicationCustomerUuid(String uuid) {
			return Optional.ofNullable(uuid)
			.filter(StringUtils::hasLength)
			.map(Rethrow.rethrowFunction(s -> dataService.getAllByApplicationCustomerUuid(s)))
			.orElse(new ArrayList<>());
		}

		@Override
		public void deleteAllByApplicationCustomerUuid(String uuid) throws ApplicationUncheckException {
			if (StringUtils.hasLength(uuid)) {
				dataService.deleteAllByApplicationCustomerUuid(uuid);
			}else {
				throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.DELETE_ALL));
			}
		}
								@Override
					public List<BankAccountDto> getAllByAccountUuid(String uuid) {
						return Optional.ofNullable(uuid)
						.filter(StringUtils::hasLength)
						.map(Rethrow.rethrowFunction(s -> dataService.getAllByAccountUuid(s)))
						.orElse(new ArrayList<>());
					}

					@Override
					public void deleteAllByAccountUuid(String uuid) throws ApplicationUncheckException {
						if(StringUtils.hasLength(uuid)){
							dataService.deleteAllByAccountUuid(uuid);
						} else {
							throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.DELETE_ALL));
						}
					}
											
}