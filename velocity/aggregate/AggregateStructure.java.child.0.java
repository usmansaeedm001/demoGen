#set($basePackage = ${Base_package})
#set($unitPackage = ${Unit_package})
#set($mainComponent = ${Main_component})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.annotation.AggregateDataService;

#parse("File Header.java")



@AggregateDataService
public class ${NAME}DataServiceImpl implements ${NAME}DataService{


}
