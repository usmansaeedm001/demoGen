package com.digitify.ob.velo.unit.bankaccount;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BankAccountUpdateDto {

	    	@JsonProperty("childNumber")
    	private String childNumber;
				@JsonProperty("accountUuid")
	private String accountUuid;
				
	@JsonProperty("active") private Boolean isActive;
}