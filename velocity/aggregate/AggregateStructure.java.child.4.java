#set($basePackage = ${Base_package})
#set($unitPackage = ${Unit_package})
#set($PARENT = ${Main_component})
#set($apiContextPath = ${Api_context_path})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

#parse("File Header.java")


@Slf4j
@RestController
@RequestMapping(path = "${apiContextPath.toLowerCase()}")
public class ${NAME}Controller {

@Autowired private ${NAME}Service service;


@GetMapping
public ResponseEntity<ApiResponse<List<${NAME}Dto>>> getAll${NAME}() {
return new ResponseEntity<>(new ApiResponse<>(null, service.getAll${NAME}()), HttpStatus.OK);
}

@GetMapping(path = "/{uuid}")
public ResponseEntity<ApiResponse<${NAME}Dto>> get${NAME}(@PathVariable String uuid) {
return new ResponseEntity<>(new ApiResponse<>(null, service.get${NAME}(uuid)), HttpStatus.OK);
}

@PostMapping
public ResponseEntity<ApiResponse<${NAME}Dto>> save${NAME}(@RequestBody ${NAME}Dto appUserDto) {
return new ResponseEntity<>(new ApiResponse<>(null, service.save${NAME}(appUserDto)), HttpStatus.CREATED);
}

@DeleteMapping(path = "/{uuid}")
public ResponseEntity<ApiResponse<${NAME}Dto>> delete${NAME}(@PathVariable String uuid) {
service.delete${NAME}(uuid);
return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
}
}