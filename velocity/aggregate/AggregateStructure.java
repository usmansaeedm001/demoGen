
#set($basePackage = ${Base_package})
#set($unitPackage = ${Unit_package})
#set($apiContextPath = ${Api_context_path})
#set($mainComponent = ${Main_component})

#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import ${unitPackage}.${mainComponent.toLowerCase()}.${mainComponent}Dto;
#if($Child_list && $Child_list != "")
    #foreach($child in $Child_list.split(","))
    import ${unitPackage}.${child.toLowerCase()}.${child}Dto;
    #end
#end

import java.util.List;
import lombok.*;

#parse("File Header.java")


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class ${NAME} {

#if($mainComponent && $mainComponent != "")
#set($camelCaseMainComponent = $mainComponent.substring(0,1).toLowerCase()+$mainComponent.substring(1))
    private ${mainComponent}Dto ${camelCaseMainComponent}Dto;
#end
#if($Child_list && $Child_list != "")
    #foreach($child in $Child_list.split(","))
    #set($camelCaseChild =${child.substring(0,1).toLowerCase()}+$child.substring(1))
    private List<${child}Dto> ${camelCaseChild}DtoList;
    #end
#end
}

