#set($principal = ${Parent})
#set($principal = ${Principal})
#set($basePackage = ${Base_package})
#set($apiContextPath = ${Api_context_path})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end


import ${basePackage}.enums.EntityType;
import ${basePackage}.enums.ErrorCode;
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

#parse("File Header.java")


@RestController
@RequestMapping(path = "${apiContextPath.toLowerCase()}")
public class ${NAME}Controller implements RequestValidationAdviser {
	#if(${NAME} && ${NAME} != "")
	@Autowired private ${NAME}Service service;

	@GetMapping(path = "/{uuid}")
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','READ_${NAME.toUpperCase()}','ADMIN_READ_${NAME.toUpperCase()}' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<${NAME}Dto>> get${NAME}(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','READ_${NAME.toUpperCase()}','ADMIN_READ_${NAME.toUpperCase()}' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<Page<${NAME}Dto>>> search${NAME}(@PathVariable("page-no") int pageNo,
																   @PathVariable("page-size") int pageSize,
																   @RequestBody ${NAME}SearchDto dto) {
	   //todo: ${NAME}UpdateDto must include only those fields for which search is allowed.
		return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
	}
    
	@PostMapping
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','WRITE_${NAME.toUpperCase()}','ADMIN_WRITE_${NAME.toUpperCase()}' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<${NAME}Dto>> save${NAME}(@Valid @RequestBody ${NAME}CreateDto dto, BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: ${NAME}UpdateDto must include only those fields for which create is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under ${basePackage}.enums and
		//todo: add an instance in EntityType with name ${NAME.toUpperCase()}
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under ${basePackage}.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.POST, new EnumerationWrapper<>(EntityType.${NAME.toUpperCase()}), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}
    
	@PutMapping(path = "/{uuid}")
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','UPDATE_${NAME.toUpperCase()}','ADMIN_UPDATE_${NAME.toUpperCase()}' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<${NAME}Dto>> update${NAME}(@PathVariable String uuid,@Valid @RequestBody ${NAME}UpdateDto dto, BindingResult bindingResult) throws ApplicationUncheckException {

		//todo: ${NAME}UpdateDto must include only those fields for which update is allowed.
		//todo: All the field in ${NAME}UpdateDto atleast annotated with javax.validation.constraints.NotNull
		//todo: create EntityType enum and inherit it with EnumerationFace interface under ${basePackage}.enums and
		//todo: add an instance in EntityType with name ${NAME.toUpperCase()}
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under ${basePackage}.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PUT, new EnumerationWrapper<>(EntityType.${NAME.toUpperCase()}), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}
    
	@PatchMapping
	@PreAuthorize("hasAnyAuthority({'USER','ADMIN','UPDATE_${NAME.toUpperCase()}','ADMIN_UPDATE_${NAME.toUpperCase()}' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<${NAME}Dto>> partialUpdate${NAME}(@Valid @RequestBody ${NAME}PartialUpdateDto partialDto, BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: ${NAME}UpdateDto must include only those fields for which partialUpdate is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under ${basePackage}.enums and
		//todo: add an instance in EntityType with name ${NAME.toUpperCase()}
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under ${basePackage}.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PATCH, new EnumerationWrapper<>(EntityType.${NAME.toUpperCase()}), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}
    
	@DeleteMapping(path = "/{uuid}")
	@PreAuthorize("hasAnyAuthority({'ADMIN','ADMIN_DELETE_${NAME.toUpperCase()}' })" +
			"|| hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<${NAME}Dto>> delete${NAME}( @PathVariable String uuid) throws ApplicationUncheckException {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}
	
	#if(${principal} && ${principal} != "" )
		#set($principalCamelCase = $principal.substring(0,1).toLowerCase()+$principal.substring(1))
		@PreAuthorize("hasAnyAuthority({'USER','ADMIN','READ_${NAME.toUpperCase()}','ADMIN_READ_${NAME.toUpperCase()}' })" +
			"|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
		@GetMapping(path = "/${principalCamelCase}/{uuid}")
		public ResponseEntity<ApiResponse<List<${NAME}Dto>>> getAll${NAME}By${principal}Uuid(@PathVariable String uuid) {
			return new ResponseEntity<>(new ApiResponse<>(null, service.getAllBy${principal}Uuid(uuid)), HttpStatus.OK);
		}

		@DeleteMapping(path = "/${principalCamelCase}/{uuid}")
		@PreAuthorize("hasAnyAuthority({'ADMIN','ADMIN_DELETE_${NAME.toUpperCase()}' }) " +
			" || hasAnyRole( 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
		public ResponseEntity<ApiResponse<${NAME}Dto>> deleteAll${NAME}By${principal}Uuid(@PathVariable String uuid) throws ApplicationUncheckException {
			service.deleteAllBy${principal}Uuid(uuid);
			return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
		}
	#end
	#end
}

