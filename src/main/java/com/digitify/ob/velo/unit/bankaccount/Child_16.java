package com.digitify.ob.velo.unit.bankaccount;

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
@RequestMapping(path = "/api/bankaccount")
public class BankAccountController implements RequestValidationAdviser {
		@Autowired private BankAccountService service;

	@GetMapping(path = "/{uuid}")
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','READ_BANKACCOUNT','ADMIN_READ_BANKACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<BankAccountDto>> getBankAccount(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','READ_BANKACCOUNT','ADMIN_READ_BANKACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<Page<BankAccountDto>>> searchBankAccount(@PathVariable("page-no") int pageNo,
																   @PathVariable("page-size") int pageSize,
																   @RequestBody BankAccountSearchDto dto) {
	   //todo: BankAccountUpdateDto must include only those fields for which search is allowed.
		return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
	}
    
	@PostMapping
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','WRITE_BANKACCOUNT','ADMIN_WRITE_BANKACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<BankAccountDto>> saveBankAccount(@Valid @RequestBody BankAccountCreateDto dto, BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: BankAccountUpdateDto must include only those fields for which create is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add an instance in EntityType with name BANKACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.POST, new EnumerationWrapper<>(EntityType.BANKACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}
    
	@PutMapping(path = "/{uuid}")
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','UPDATE_BANKACCOUNT','ADMIN_UPDATE_BANKACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<BankAccountDto>> updateBankAccount(@PathVariable String uuid,@Valid @RequestBody BankAccountUpdateDto dto, BindingResult bindingResult) throws ApplicationUncheckException {

		//todo: BankAccountUpdateDto must include only those fields for which update is allowed.
		//todo: All the field in BankAccountUpdateDto atleast annotated with javax.validation.constraints.NotNull
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add an instance in EntityType with name BANKACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PUT, new EnumerationWrapper<>(EntityType.BANKACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}
    
	@PatchMapping
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','UPDATE_BANKACCOUNT','ADMIN_UPDATE_BANKACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<BankAccountDto>> partialUpdateBankAccount(@Valid @RequestBody BankAccountPartialUpdateDto partialDto, BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: BankAccountUpdateDto must include only those fields for which partialUpdate is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add an instance in EntityType with name BANKACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.digitify.ob.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PATCH, new EnumerationWrapper<>(EntityType.BANKACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}
    
	@DeleteMapping(path = "/{uuid}")
	@PreAuthorize("hasAnyAuthority({'ADMIN','ADMIN_DELETE_BANKACCOUNT' })" +
			"|| hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<BankAccountDto>> deleteBankAccount( @PathVariable String uuid) throws ApplicationUncheckException {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}
	
			@PreAuthorize("hasAnyAuthority({'USER','ADMIN','READ_BANKACCOUNT','ADMIN_READ_BANKACCOUNT' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
		@GetMapping(path = "/applicationCustomer/{uuid}")
		public ResponseEntity<ApiResponse<List<BankAccountDto>>> getAllBankAccountByApplicationCustomerUuid(@PathVariable String uuid) {
			return new ResponseEntity<>(new ApiResponse<>(null, service.getAllByApplicationCustomerUuid(uuid)), HttpStatus.OK);
		}

		@DeleteMapping(path = "/applicationCustomer/{uuid}")
		@PreAuthorize("hasAnyAuthority({'ADMIN','ADMIN_DELETE_BANKACCOUNT' }) " +
			" || hasAnyRole( 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
		public ResponseEntity<ApiResponse<BankAccountDto>> deleteAllBankAccountByApplicationCustomerUuid(@PathVariable String uuid) throws ApplicationUncheckException {
			service.deleteAllByApplicationCustomerUuid(uuid);
			return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
		}
		}

