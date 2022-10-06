package com.digitify.ob.velo.unit.account;
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
public interface AccountRepository extends JpaRepository<Account, Long>{
	/******
	*
	* Query By Uuid
	*
	**********/
	boolean existsByUuid(String uuid);
	boolean existsByUuidAndIsActiveTrue(String uuid);

    Optional<Account> findByUuid(String uuid);
    Optional<Account> findByUuidAndIsActiveTrue(String uuid);

	List<Account> findAllByUuidIn(List<String> uuidList);
	List<Account> findAllByUuidInAndIsActiveTrue(List<String> uuidList);

	void deleteAllByUuidIn(List<String> uuids);
	void deleteAllByUuidInAndIsActiveTrue(List<String> uuids);

			/******
		*
		* Query By ApplicationCustomer Uuid
		*
		**********/
		boolean existsByUuidAndApplicationCustomerUuid(String uuid, String principalUuid);
		boolean existsByUuidAndApplicationCustomerUuidAndIsActiveTrue(String uuid, String principalUuid);
		Optional<Account> findByUuidAndApplicationCustomerUuid(String uuid, String principalUuid);
		Optional<Account> findByUuidAndApplicationCustomerUuidAndIsActiveTrue(String uuid, String principalUuid);
		List<Account> findAllByApplicationCustomerUuidAndUuidIn(String principalUuid, List<String> uuidList);
		List<Account> findAllByApplicationCustomerUuidAndUuidInAndIsActiveTrue(String principalUuid, List<String> uuidList);
		List<Account> findAllByApplicationCustomerUuid(String uuid);
		List<Account> findAllByApplicationCustomerUuidAndIsActiveTrue(String uuid);
				Boolean existsByApplicationCustomerUuidAndUuidNot(String applicationCustomerUuid, String uuid);
		Boolean existsByApplicationCustomerUuidAndUuidNotAndIsActiveTrue(String applicationCustomerUuid, String uuid);

								
			/******
			*
			* Query By unique ChildNumber
			*
			**********/
		boolean existsByChildNumberAndUuidNot(String childNumber, String uuid);
		boolean existsByChildNumberAndUuidNotAndIsActiveTrue(String childNumber, String uuid);
	
}