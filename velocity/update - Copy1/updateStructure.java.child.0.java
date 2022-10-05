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

	boolean existsByUuid(String uuid);
    Optional<${NAME}> findByUuid(String uuid);
	List<${NAME}> findAllByUuidIn(List<String> uuidList);
	void deleteAllByUuidIn(List<String> uuids);

	#foreach($parent in $PARENT.split(","))
		#if(${principal} && ${principal} != "" && $parent == $principal)
				boolean existsByUuidAnd${principal}Uuid(String uuid, String principalUuid);
				Optional<${NAME}> findByUuidAnd${principal}Uuid(String uuid, String principalUuid);
				List<${NAME}> findAllBy${principal}UuidAndUuidIn(String principalUuid, List<String> uuidList);
				List<${NAME}> findAllBy${principal}Uuid(String uuid);
			#else
				boolean existsBy${parent}UuidAnd${principal}Uuid(String uuid, String principalUuid);
				Optional<${NAME}> findBy${parent}UuidAnd${principal}Uuid(String uuid, String principalUuid);
				List<${NAME}> findAllBy${parent}UuidAnd${principal}UuidAndUuidIn(String uuid, String principalUuid, List<String> uuidList);
				List<${NAME}> findAllBy${parent}UuidAnd${principal}Uuid(String uuid, String principalUuid);
			#end
	#end
#end
}