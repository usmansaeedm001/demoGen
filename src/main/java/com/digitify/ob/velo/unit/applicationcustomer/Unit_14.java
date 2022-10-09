package com.digitify.ob.velo.unit.applicationcustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
public interface ApplicationCustomerRepository extends JpaRepository<ApplicationCustomer, Long>{
	/******
	*
	* Query By Uuid
	*
	**********/
	boolean existsByUuid(String uuid);
	boolean existsByUuidAndIsActiveTrue(String uuid);

    Optional<ApplicationCustomer> findByUuid(String uuid);
    Optional<ApplicationCustomer> findByUuidAndIsActiveTrue(String uuid);

	List<ApplicationCustomer> findAllByUuidIn(List<String> uuidList);
	List<ApplicationCustomer> findAllByUuidInAndIsActiveTrue(List<String> uuidList);

	void deleteAllByUuidIn(List<String> uuids);
	void deleteAllByUuidInAndIsActiveTrue(List<String> uuids);

				
			/******
		*
		* Query By unique ParentNumber
		*
		**********/
		Optional<ApplicationCustomer> findByParentNumber(String unique);
		Optional<ApplicationCustomer> findByParentNumberAndIsActiveTrue(String unique);
		boolean existsByParentNumberAndUuidNot(String parentNumber, String uuid);
		boolean existsByParentNumberAndUuidNotAndIsActiveTrue(String parentNumber, String uuid);
	
}