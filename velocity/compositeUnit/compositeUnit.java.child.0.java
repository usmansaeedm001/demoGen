#set($PARENT = ${Parent})
#set($basePackage = ${Base_package})
#set($apiContextPath = ${Api_context_path})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end


import com.digitify.framework.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

#parse("File Header.java")


@RestController
@RequestMapping(path = "${apiContextPath.toLowerCase()}")
public class ${NAME}Controller {
	#if(${NAME} && ${NAME} != "")
	@Autowired private ${NAME}IntegrationService service;

    
	@GetMapping
	public ResponseEntity<ApiResponse<Flux<${NAME}Dto>>> getAll${NAME}() {
		return new ResponseEntity<>(new ApiResponse<>(null, service.getAll()), HttpStatus.OK);
	}
	
	#if(${PARENT} && ${PARENT} != "" )
		#foreach($parent in $PARENT.split(","))
			#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
			@GetMapping(path = "/${parentCamelCase}/{uuid}")
			public ResponseEntity<ApiResponse<Flux<${NAME}Dto>>> getAll${NAME}By${parent}Uuid(@PathVariable String uuid) {
				return new ResponseEntity<>(new ApiResponse<>(null, service.getAllBy${parent}Uuid(uuid)), HttpStatus.OK);
			}
		#end
	#end
	
    
	@GetMapping(path = "/{uuid}")
	public ResponseEntity<ApiResponse<Mono<${NAME}Dto>>> get${NAME}(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
	public ResponseEntity<ApiResponse<Flux<${NAME}Dto>>> searchCustomer(@PathVariable("page-no") int pageNo,
																   @PathVariable("page-size") int pageSize,
																   @RequestBody ${NAME}Dto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.CREATED);
	}
    
	@PostMapping
	public ResponseEntity<ApiResponse<Mono<${NAME}Dto>>> save${NAME}( @RequestBody ${NAME}Dto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}
    
	@PutMapping(path = "/{uuid}")
	public ResponseEntity<ApiResponse<Mono<${NAME}Dto>>> update${NAME}(@PathVariable String uuid, @RequestBody ${NAME}Dto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}
    
	@PatchMapping
	public ResponseEntity<ApiResponse<Mono<${NAME}Dto>>> partialUpdate${NAME}( @RequestBody ${NAME}Dto partialDto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}
    
	@DeleteMapping(path = "/{uuid}")
	public ResponseEntity<ApiResponse<Mono<${NAME}Dto>>> delete${NAME}( @PathVariable String uuid) {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}
	
	#if(${PARENT} && ${PARENT} != "" )
	#foreach($parent in $PARENT.split(","))
	#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
	@DeleteMapping(path = "/${parentCamelCase}/{uuid}")
	public ResponseEntity<ApiResponse<Mono<${NAME}Dto>>> deleteAllBy${parent}Uuid(@PathVariable String uuid) {
		service.deleteAllBy${parent}Uuid(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}
	#end
	#end
	#end
}
