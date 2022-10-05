#set($basePackage = ${Base_package})
#set($integrationPackage = ${Integration_package})
#set($mainComponent = ${Main_component})
#set($camelCaseMainComponent = $mainComponent.substring(0,1).toLowerCase()+$mainComponent.substring(1))
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end


import ${integrationPackage}.${mainComponent.toLowerCase()}.${mainComponent}Dto;
import ${integrationPackage}.${mainComponent.toLowerCase()}.${mainComponent}DataService;
#if($Child_list && $Child_list != "")
#foreach($child in $Child_list.split(","))
#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
import ${integrationPackage}.${child.toLowerCase()}.${child}Dto;
import ${integrationPackage}.${child.toLowerCase()}.${child}DataService;

#end
#end

import com.digitify.framework.annotation.AggregateService;
import ${basePackage}.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationException;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.util.Rethrow;
import com.digitify.framework.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

#parse("File Header.java")


@Slf4j
@AggregateService
public class ${NAME}Service {

@Autowired private ${NAME}Adapter adapter;
#if($Child_list && $Child_list != "")
	#foreach ($child in $Child_list.split(","))
	#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))

	#end
#end
}
