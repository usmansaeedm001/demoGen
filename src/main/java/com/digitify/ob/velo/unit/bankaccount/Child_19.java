package com.digitify.ob.velo.unit.bankaccount;
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
public class BankAccountDtoMapperImpl implements BankAccountDtoMapper {

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto)authentication.getPrincipal()).getUuid();
	}
	/**
	 * @param dto
	 * @return BankAccountDto
	 */
	@Override
	public BankAccountDto fromCreateDto(BankAccountCreateDto dto) {
		BankAccountDto bankAccountDto = new BankAccountDto();
		bankAccountDto.setUuid(UUID.randomUUID().toString());
		return bankAccountDto;

	}

	/**
	 * @param dto
	 * @return BankAccountDto
	 */
	@Override
	public BankAccountDto fromSearchDto(BankAccountSearchDto dto) {
		BankAccountDto bankAccountDto = new BankAccountDto();
		bankAccountDto.setUuid(dto.getUuid());
						bankAccountDto.setAccountUuid(dto.getAccountUuid());
				bankAccountDto.setApplicationCustomerUuid(dto.getApplicationCustomerUuid());
						return bankAccountDto;

	}

	/**
	 * @param dto
	 * @return BankAccountDto
	 */
	@Override
	public BankAccountDto fromUpdateDto(BankAccountUpdateDto dto) {
		BankAccountDto bankAccountDto = new BankAccountDto();
		return bankAccountDto;

	}

	/**
	 * @param dto
	 * @return BankAccountDto
	 */
	@Override
	public BankAccountDto fromPartialUpdateDto(BankAccountPartialUpdateDto dto) {
		BankAccountDto bankAccountDto = new BankAccountDto();
		return bankAccountDto;

	}
}
