#set($basePackage = ${Base_package})
#set($integrationPackage = ${Integration_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.face.CompositeMapper;

#parse("File Header.java")


public interface ${NAME}Mapper extends CompositeMapper<${NAME}Dto> {

}

