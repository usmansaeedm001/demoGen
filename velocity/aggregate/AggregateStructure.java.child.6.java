#set($basePackage = ${Base_package})
#set($unitPackage = ${Unit_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.face.AggregateMapper;

#parse("File Header.java")


public interface ${NAME}Mapper extends AggregateMapper<${NAME}Dto, ${NAME}> {

}

