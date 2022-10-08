package com.digitify.ob.velo.unit.bankaccount;
import com.digitify.framework.annotation.Mapper;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
@Mapper
public class BankAccountMapperImpl implements BankAccountMapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	@Override
	public BankAccount toEntity(BankAccountDto dto) {
		BankAccount bankAccount = new BankAccount();
	    bankAccount.setUuid(dto.getUuid());
	    return bankAccount;
	}

	@Override
	public BankAccountDto toDto(BankAccount bankAccount) {
		BankAccountDto dto = new BankAccountDto();
	    dto.setUuid(bankAccount.getUuid());
	    return dto;
	}

	@Override
	public BankAccount update(BankAccountDto dto, BankAccount bankAccount) {
		return bankAccount;
	}

	@Override
	public BankAccount partialUpdate(BankAccountDto dto, BankAccount bankAccount) {
		return bankAccount;
	}
	}
