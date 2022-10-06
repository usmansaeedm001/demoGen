package com.digitify.ob.velo.unit.account;
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
public class AccountUpdateDto {

	    	@JsonProperty("childNumber")
    	private String childNumber;
				
	@JsonProperty("active") private Boolean isActive;
}