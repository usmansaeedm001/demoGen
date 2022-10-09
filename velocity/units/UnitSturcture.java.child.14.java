#set($PARENT = ${Parent})
#set($principal = ${Principal})
#set($uniqueField = ${Unique_field})
#set($uniqueFieldCamelCase = $uniqueField.substring(0,1).toLowerCase()+$uniqueField.substring(1))
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

#parse("File Header.java")

public interface ${NAME}Repository extends JpaRepository<${NAME}, Long>{
#if(${NAME} && ${NAME} != "")
	/******
	*
	* Query By Uuid
	*
	**********/
	boolean existsByUuid(String uuid);
	boolean existsByUuidAndIsActiveTrue(String uuid);

    Optional<${NAME}> findByUuid(String uuid);
    Optional<${NAME}> findByUuidAndIsActiveTrue(String uuid);

	List<${NAME}> findAllByUuidIn(List<String> uuidList);
	List<${NAME}> findAllByUuidInAndIsActiveTrue(List<String> uuidList);

	void deleteAllByUuidIn(List<String> uuids);
	void deleteAllByUuidInAndIsActiveTrue(List<String> uuids);

	#if(${principal} && ${principal} != "")
		/******
		*
		* Query By ${principal} Uuid
		*
		**********/
		boolean existsByUuidAnd${principal}Uuid(String uuid, String principalUuid);
		boolean existsByUuidAnd${principal}UuidAndIsActiveTrue(String uuid, String principalUuid);
		Optional<${NAME}> findByUuidAnd${principal}Uuid(String uuid, String principalUuid);
		Optional<${NAME}> findByUuidAnd${principal}UuidAndIsActiveTrue(String uuid, String principalUuid);
		List<${NAME}> findAllBy${principal}UuidAndUuidIn(String principalUuid, List<String> uuidList);
		List<${NAME}> findAllBy${principal}UuidAndUuidInAndIsActiveTrue(String principalUuid, List<String> uuidList);
		List<${NAME}> findAllBy${principal}Uuid(String uuid);
		List<${NAME}> findAllBy${principal}UuidAndIsActiveTrue(String uuid);
		#set($principalCamelCase = $principal.substring(0,1).toLowerCase()+$principal.substring(1))
		Boolean existsBy${principal}UuidAndUuidNot(String ${principalCamelCase}Uuid, String uuid);
		Boolean existsBy${principal}UuidAndUuidNotAndIsActiveTrue(String ${principalCamelCase}Uuid, String uuid);
		#if(${PARENT} && ${PARENT} != "")
		#foreach($parent in $PARENT.split(","))
			#if($parent  && $parent != "" && $parent != $principal)
				/******
				*
				* Query By ${parent}UuidAnd${principal}Uuid
				*
				**********/
				boolean existsBy${parent}UuidAnd${principal}Uuid(String uuid, String principalUuid);
				boolean existsBy${parent}UuidAnd${principal}UuidAndIsActiveTrue(String uuid, String principalUuid);
				Optional<${NAME}> findByUuidAnd${parent}UuidAnd${principal}Uuid(String uuid, String parentUuid, String principalUuid);
				Optional<${NAME}> findByUuidAnd${parent}UuidAnd${principal}UuidAndIsActiveTrue(String uuid, String parentUuid, String principalUuid);
				List<${NAME}> findAllBy${parent}UuidAnd${principal}UuidAndUuidIn(String uuid, String principalUuid, List<String> uuidList);
				List<${NAME}> findAllBy${parent}UuidAnd${principal}UuidAndUuidInAndIsActiveTrue(String uuid, String principalUuid, List<String> uuidList);
				List<${NAME}> findAllBy${parent}UuidAnd${principal}Uuid(String uuid, String principalUuid);
				List<${NAME}> findAllBy${parent}UuidAnd${principal}UuidAndIsActiveTrue(String uuid, String principalUuid);
				#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
				Boolean existsBy${parent}UuidAndUuidNot(String ${parentCamelCase}Uuid, String uuid);
				Boolean existsBy${parent}UuidAndUuidNotAndIsActiveTrue(String ${parentCamelCase}Uuid, String uuid);
			#end
		#end
		#end
	#else
		#if($PARENT  && $PARENT != "")
			#foreach($parent in $PARENT.split(","))
			/******
			*
			* Query By ${parent}Uuid
			*
			**********/
					boolean existsBy${parent}Uuid(String uuid);
					boolean existsBy${parent}UuidAndIsActiveTrue(String uuid);
					Optional<${NAME}> findByUuidAnd${parent}Uuid(String uuid, String parentUuid);
					Optional<${NAME}> findByUuidAnd${parent}UuidAndIsActiveTrue(String uuid, String parentUuid);
					Optional<${NAME}> findBy${parent}Uuid(String uuid);
					Optional<${NAME}> findBy${parent}UuidAndIsActiveTrue(String uuid);
					List<${NAME}> findAllBy${parent}UuidAndUuidIn(String uuid, String principalUuid);
					List<${NAME}> findAllBy${parent}UuidAndUuidInAndIsActiveTrue(String uuid, String principalUuid);
					List<${NAME}> findAllBy${parent}Uuid(String uuid);
					List<${NAME}> findAllBy${parent}UuidAndIsActiveTrue(String uuid);
					#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
					Boolean existsBy${parent}UuidAndUuidNot(String ${parentCamelCase}Uuid, String uuid);
					Boolean existsBy${parent}UuidAndUuidNotAndIsActiveTrue(String ${parentCamelCase}Uuid, String uuid);
			#end
		#end
	#end

	#if(${uniqueField} && ${uniqueField} != "")
		/******
		*
		* Query By unique ${uniqueField}
		*
		**********/
		Optional<${NAME}> findBy${uniqueField}(String unique);
		Optional<${NAME}> findBy${uniqueField}AndIsActiveTrue(String unique);
		boolean existsBy${uniqueField}AndUuidNot(String ${uniqueFieldCamelCase}, String uuid);
		boolean existsBy${uniqueField}AndUuidNotAndIsActiveTrue(String ${uniqueFieldCamelCase}, String uuid);
	#end

#end
}