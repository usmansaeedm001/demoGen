#set($mainComponent = ${Main_component})
#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import ${basePackage}.face.CompositeAdapter;
import reactor.core.publisher.Flux;

#parse("File Header.java")


public interface ${NAME}Adapter extends CompositeAdapter<${NAME}Dto> {
#if(${NAME} && ${NAME} != "")
	#if(${PARENT} && ${PARENT} != "")
		#foreach($parent in $PARENT.split(","))
			#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
			Flux<${NAME}Dto> getAllBy${parent}Uuid(String uuid);
		#end
		#foreach($parent in $PARENT.split(","))
			#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
			void deleteAllBy${parent}Uuid(String uuid);
		#end
	#end
#end



}