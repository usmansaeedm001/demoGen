#set($basePackage = ${Base_package})
#set($integrationPackage = ${Integration_package})
#set($mainComponent = ${Main_component})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end


#if($Child_list && $Child_list != "")
	#foreach($child in $Child_list.split(","))
		#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
		import ${integrationPackage}.${child.toLowerCase()}.${child}Dto;
	#end
#end
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.*;

#parse("File Header.java")


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class ${NAME}Dto {

	@JsonProperty("uuid") private String uuid;
	@JsonProperty("active") private Boolean isActive;
	//todo: add ${mainComponent}dto required fields
#if($Child_list && $Child_list != "")
	#foreach ($child in $Child_list.split(","))
	#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
    private List<${child}Dto> ${camelCaseChild}DtoList;
	#end
#end
}

