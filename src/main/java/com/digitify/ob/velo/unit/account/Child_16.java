package com.digitify.ob.velo.unit.account;

import com.digitify.ob.enums.EntityType;
import com.digitify.ob.enums.ErrorCode;
import com.digitify.framework.dto.ApiResponse;
import com.digitify.framework.dto.EnumerationWrapper;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.face.RequestValidationAdviser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/

@RestController
@RequestMapping(path = "/api/account")
public class AccountController implements RequestValidationAdviser {
		@Autowired private AccountService service;

	@GetMapping(path = "/{uuid}")
	@PreAuthorize("hasAuthority('READ_ACCOUNT')")
	public ResponseEntity<ApiResponse<AccountDto>> getAccount(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
	@PreAuthorize("hasAuthority('READ_ACCOUNT')")
	public ResponseEntity<ApiResponse<Page<AccountDto>>> searchAccount(@PathVariable("page-no") int pageNo,
																   @PathVariable("page-size") int pageSize,
																   @RequestBody AccountSearchDto dto) {
	   //todo: AccountUpdateDto must include only those fields for which search is allowed.
		return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
	}
    
	@PostMapping
	@PreAuthorize("hasAuthority('WRITE_ACCOUNT')")
	public ResponseEntity<ApiResponse<AccountDto>> saveAccount(@Valid @RequestBody AccountCreateDto dto, BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: AccountUpdateDto must include only those fields for which create is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add an instance in EntityType with name ACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.POST, new EnumerationWrapper<>(EntityType.ACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}
    
	@PutMapping(path = "/{uuid}")
	@PreAuthorize("hasAuthority('UPDATE_ACCOUNT')")
	public ResponseEntity<ApiResponse<AccountDto>> updateAccount(@PathVariable String uuid,@Valid @RequestBody AccountUpdateDto dto, BindingResult bindingResult) throws ApplicationUncheckException {

		//todo: AccountUpdateDto must include only those fields for which update is allowed.
		//todo: All the field in AccountUpdateDto atleast annotated with javax.validation.constraints.NotNull
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add an instance in EntityType with name ACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PUT, new EnumerationWrapper<>(EntityType.ACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}
    
	@PatchMapping
	@PreAuthorize("hasAuthority('UPDATE_ACCOUNT')")
	public ResponseEntity<ApiResponse<AccountDto>> partialUpdateAccount(@Valid @RequestBody AccountPartialUpdateDto partialDto, BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: AccountUpdateDto must include only those fields for which partialUpdate is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add an instance in EntityType with name ACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PATCH, new EnumerationWrapper<>(EntityType.ACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}
    
	
	
		@PreAuthorize("hasAuthority('READ_ACCOUNT')")
		@GetMapping(path = "/applicationCustomer/{uuid}")
		public ResponseEntity<ApiResponse<List<AccountDto>>> getAllAccountByApplicationCustomerUuid(@PathVariable String uuid) {
			return new ResponseEntity<>(new ApiResponse<>(null, service.getAllByApplicationCustomerUuid(uuid)), HttpStatus.OK);
		}

		}
