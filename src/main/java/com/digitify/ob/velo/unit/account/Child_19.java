package com.digitify.ob.velo.unit.account;
import com.digitify.framework.annotation.DtoMapper;
import com.digitify.validator.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.UUID;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
	


@DtoMapper
public class AccountDtoMapperImpl implements AccountDtoMapper {

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}
	/**
	 * @param dto
	 * @return AccountDto
	 */
	@Override
	public AccountDto fromCreateDto(AccountCreateDto dto) {
		AccountDto accountDto = new AccountDto();
		accountDto.setUuid(UUID.randomUUID().toString());
		return accountDto;

	}

	/**
	 * @param dto
	 * @return AccountDto
	 */
	@Override
	public AccountDto fromSearchDto(AccountSearchDto dto) {
		AccountDto accountDto = new AccountDto();
		accountDto.setUuid(dto.getUuid());
						accountDto.setApplicationCustomerUuid(dto.getApplicationCustomerUuid());
						return accountDto;

	}

	/**
	 * @param dto
	 * @return AccountDto
	 */
	@Override
	public AccountDto fromUpdateDto(AccountUpdateDto dto) {
		AccountDto accountDto = new AccountDto();
		return accountDto;

	}

	/**
	 * @param dto
	 * @return AccountDto
	 */
	@Override
	public AccountDto fromPartialUpdateDto(AccountPartialUpdateDto dto) {
		AccountDto accountDto = new AccountDto();
		return accountDto;

	}
}
