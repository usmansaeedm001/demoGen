#set($PARENT = ${Parent})
#set($uniqueField = ${Unique_field})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.*;

#parse("File Header.java")


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class ${NAME}CreateDto {
#if(${NAME} && ${NAME} != "")
    
    @JsonProperty("uuid")
    private String uuid;

    #if(${uniqueField} && ${uniqueField} != "")
	#set($uniqueFieldCamelCase = $uniqueField.substring(0,1).toLowerCase()+$uniqueField.substring(1))
    	@JsonProperty("${uniqueFieldCamelCase}")
    	private String ${uniqueFieldCamelCase};
	#end
    
#if( ${PARENT} && ${PARENT} != "")
#foreach($parent in $PARENT.split(","))
#if( ${principal} !=  ${parent})
	#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
	@JsonProperty("${parentCamelCase}Uuid")
	private String ${parentCamelCase}Uuid;
#end
#end
#end

    @JsonProperty("active") private Boolean isActive;
    
    @Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		${NAME}Dto that = (${NAME}Dto) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}
	#end
}