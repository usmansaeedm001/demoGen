package com.digitify.ob.velo.unit.applicationcustomer;
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
public class ApplicationCustomerUpdateDto {

	    	@JsonProperty("parentNumber")
    	private String parentNumber;
	
	@JsonProperty("active") private Boolean isActive;
}