package com.digitify.ob.velo.unit.bankaccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.*;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class BankAccountSearchDto {
    
    @JsonProperty("uuid")
    private String uuid;

        	@JsonProperty("childNumber")
    	private String childNumber;
	
			@JsonProperty("applicationCustomerUuid")
		private String applicationCustomerUuid;
    											@JsonProperty("accountUuid")
				private String accountUuid;
															
	@JsonProperty("active") private Boolean isActive;
    
    @Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		BankAccountDto that = (BankAccountDto) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}
	}