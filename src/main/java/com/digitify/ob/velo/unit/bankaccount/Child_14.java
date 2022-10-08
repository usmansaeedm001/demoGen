package com.digitify.ob.velo.unit.bankaccount;
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
public interface BankAccountRepository extends JpaRepository<BankAccount, Long>{
	/******
	*
	* Query By Uuid
	*
	**********/
	boolean existsByUuid(String uuid);
	boolean existsByUuidAndIsActiveTrue(String uuid);

    Optional<BankAccount> findByUuid(String uuid);
    Optional<BankAccount> findByUuidAndIsActiveTrue(String uuid);

	List<BankAccount> findAllByUuidIn(List<String> uuidList);
	List<BankAccount> findAllByUuidInAndIsActiveTrue(List<String> uuidList);

	void deleteAllByUuidIn(List<String> uuids);
	void deleteAllByUuidInAndIsActiveTrue(List<String> uuids);

			/******
		*
		* Query By ApplicationCustomer Uuid
		*
		**********/
		boolean existsByUuidAndApplicationCustomerUuid(String uuid, String principalUuid);
		boolean existsByUuidAndApplicationCustomerUuidAndIsActiveTrue(String uuid, String principalUuid);
		Optional<BankAccount> findByUuidAndApplicationCustomerUuid(String uuid, String principalUuid);
		Optional<BankAccount> findByUuidAndApplicationCustomerUuidAndIsActiveTrue(String uuid, String principalUuid);
		List<BankAccount> findAllByApplicationCustomerUuidAndUuidIn(String principalUuid, List<String> uuidList);
		List<BankAccount> findAllByApplicationCustomerUuidAndUuidInAndIsActiveTrue(String principalUuid, List<String> uuidList);
		List<BankAccount> findAllByApplicationCustomerUuid(String uuid);
		List<BankAccount> findAllByApplicationCustomerUuidAndIsActiveTrue(String uuid);
				Boolean existsByApplicationCustomerUuidAndUuidNot(String applicationCustomerUuid, String uuid);
		Boolean existsByApplicationCustomerUuidAndUuidNotAndIsActiveTrue(String applicationCustomerUuid, String uuid);
											/******
				*
				* Query By AccountUuidAndApplicationCustomerUuid
				*
				**********/
				boolean existsByAccountUuidAndApplicationCustomerUuid(String uuid, String principalUuid);
				boolean existsByAccountUuidAndApplicationCustomerUuidAndIsActiveTrue(String uuid, String principalUuid);
				Optional<BankAccount> findByUuidAndAccountUuidAndApplicationCustomerUuid(String uuid, String parentUuid, String principalUuid);
				Optional<BankAccount> findByUuidAndAccountUuidAndApplicationCustomerUuidAndIsActiveTrue(String uuid, String parentUuid, String principalUuid);
				List<BankAccount> findAllByAccountUuidAndApplicationCustomerUuidAndUuidIn(String uuid, String principalUuid, List<String> uuidList);
				List<BankAccount> findAllByAccountUuidAndApplicationCustomerUuidAndUuidInAndIsActiveTrue(String uuid, String principalUuid, List<String> uuidList);
				List<BankAccount> findAllByAccountUuidAndApplicationCustomerUuid(String uuid, String principalUuid);
				List<BankAccount> findAllByAccountUuidAndApplicationCustomerUuidAndIsActiveTrue(String uuid, String principalUuid);
								Boolean existsByAccountUuidAndUuidNot(String accountUuid, String uuid);
				Boolean existsByAccountUuidAndUuidNotAndIsActiveTrue(String accountUuid, String uuid);
													
			/******
		*
		* Query By unique ChildNumber
		*
		**********/
		Optional<BankAccount> findByChildNumberUuid(String unique);
		Optional<BankAccount> findByChildNumberUuidAndIsActiveTrue(String unique);
		boolean existsByChildNumberAndUuidNot(String childNumber, String uuid);
		boolean existsByChildNumberAndUuidNotAndIsActiveTrue(String childNumber, String uuid);
	
}