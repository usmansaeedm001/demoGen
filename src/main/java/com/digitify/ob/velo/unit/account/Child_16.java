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
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','READ_ACCOUNT','ADMIN_READ_ACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<AccountDto>> getAccount(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','READ_ACCOUNT','ADMIN_READ_ACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<Page<AccountDto>>> searchAccount(@PathVariable("page-no") int pageNo,
																   @PathVariable("page-size") int pageSize,
																   @RequestBody AccountSearchDto dto) {
	   //todo: AccountUpdateDto must include only those fields for which search is allowed.
		return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
	}
    
	@PostMapping
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','WRITE_ACCOUNT','ADMIN_WRITE_ACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
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
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','UPDATE_ACCOUNT','ADMIN_UPDATE_ACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
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
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','UPDATE_ACCOUNT','ADMIN_UPDATE_ACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<AccountDto>> partialUpdateAccount(@Valid @RequestBody AccountPartialUpdateDto partialDto, BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: AccountUpdateDto must include only those fields for which partialUpdate is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add an instance in EntityType with name ACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PATCH, new EnumerationWrapper<>(EntityType.ACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}
    
	@DeleteMapping(path = "/{uuid}")
	@PreAuthorize("hasAnyAuthority({'ADMIN','ADMIN_DELETE_ACCOUNT' })" +
			"|| hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<AccountDto>> deleteAccount( @PathVariable String uuid) throws ApplicationUncheckException {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}
	
			@PreAuthorize("hasAnyAuthority({'USER','ADMIN','READ_ACCOUNT','ADMIN_READ_ACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
		@GetMapping(path = "/applicationCustomer/{uuid}")
		public ResponseEntity<ApiResponse<List<AccountDto>>> getAllAccountByApplicationCustomerUuid(@PathVariable String uuid) {
			return new ResponseEntity<>(new ApiResponse<>(null, service.getAllByApplicationCustomerUuid(uuid)), HttpStatus.OK);
		}

		@DeleteMapping(path = "/applicationCustomer/{uuid}")
		@PreAuthorize("hasAnyAuthority({'ADMIN','ADMIN_DELETE_ACCOUNT' }) " +
			" || hasAnyRole( 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
		public ResponseEntity<ApiResponse<AccountDto>> deleteAllAccountByApplicationCustomerUuid(@PathVariable String uuid) throws ApplicationUncheckException {
			service.deleteAllByApplicationCustomerUuid(uuid);
			return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
		}
		}

