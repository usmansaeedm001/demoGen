package com.digitify.ob.velo.unit.account;
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
public class AccountMapperImpl implements AccountMapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}

	@Override
	public Account toEntity(AccountDto dto) {
		Account account = new Account();
	    account.setUuid(dto.getUuid());
	    return account;
	}

	@Override
	public AccountDto toDto(Account account) {
		AccountDto dto = new AccountDto();
	    dto.setUuid(account.getUuid());
	    return dto;
	}

	@Override
	public Account update(AccountDto dto, Account account) {
		return account;
	}

	@Override
	public Account partialUpdate(AccountDto dto, Account account) {
		return account;
	}
	}
