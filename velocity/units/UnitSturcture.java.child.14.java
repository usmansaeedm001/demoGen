#set($PARENT = ${Parent})
#set($principal = ${Principal})
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
	boolean existsByUuid(String uuid);
    Optional<${NAME}> findByUuid(String uuid);
	List<${NAME}> findAllByUuidIn(List<String> uuidList);
	void deleteAllByUuidIn(List<String> uuids);


	boolean existsByUuidAndIsActiveTrue(String uuid);
    Optional<${NAME}> findByUuidAndIsActiveTrue(String uuid);
	List<${NAME}> findAllByUuidInAndIsActiveTrue(List<String> uuidList);
	void deleteAllByUuidInAndIsActiveTrue(List<String> uuids);

	#if(${principal} && ${principal} != "")
		boolean existsByUuidAnd${principal}Uuid(String uuid, String principalUuid);
		Optional<${NAME}> findByUuidAnd${principal}Uuid(String uuid, String principalUuid);
		List<${NAME}> findAllBy${principal}UuidAndUuidIn(String principalUuid, List<String> uuidList);
		List<${NAME}> findAllBy${principal}Uuid(String uuid);

		boolean existsByUuidAnd${principal}UuidAndIsActiveTrue(String uuid, String principalUuid);
		Optional<${NAME}> findByUuidAnd${principal}UuidAndIsActiveTrue(String uuid, String principalUuid);
		List<${NAME}> findAllBy${principal}UuidAndUuidInAndIsActiveTrue(String principalUuid, List<String> uuidList);
		List<${NAME}> findAllBy${principal}UuidAndIsActiveTrue(String uuid);

		#foreach($parent in $PARENT.split(","))
			#if($parent  && $parent != "" && $parent != $principal)
				boolean existsBy${parent}UuidAnd${principal}UuidAndIsActiveTrue(String uuid, String principalUuid);
				Optional<${NAME}> findBy${parent}UuidAnd${principal}UuidAndIsActiveTrue(String uuid, String principalUuid);
				List<${NAME}> findAllBy${parent}UuidAnd${principal}UuidAndUuidInAndIsActiveTrue(String uuid, String principalUuid, List<String> uuidList);
				List<${NAME}> findAllBy${parent}UuidAnd${principal}UuidAndIsActiveTrue(String uuid, String principalUuid);
			#end
		#end
	#else
		#if($parent  && $parent != "")
			#foreach($parent in $PARENT.split(","))
					boolean existsBy${parent}UuidAndIsActiveTrue(String uuid);
					Optional<${NAME}> findBy${parent}UuidAndIsActiveTrue(String uuid);
					List<${NAME}> findAllBy${parent}UuidAndUuidInAndIsActiveTrue(String uuid, String principalUuid);
					List<${NAME}> findAllBy${parent}UuidAndIsActiveTrue(String uuid);
			#end
		#end
	#end
#end
}