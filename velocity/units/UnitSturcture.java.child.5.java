#set($PARENT = ${Parent})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

#parse("File Header.java")


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class ${NAME}UpdateDto {
#if(${NAME} && ${NAME} != "")
    
#if( ${PARENT} && ${PARENT} != "")
	#foreach($parent in $PARENT.split(","))
	#if( ${principal} !=  ${parent})
	#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
	@JsonProperty("${parentCamelCase}Uuid")
	private String ${parentCamelCase}Uuid;
	#end
	#end
#end
#end

	@JsonProperty("active") private Boolean isActive;
}