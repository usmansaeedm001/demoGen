package com.digitify.ob.velo.unit.applicationcustomer;
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
public class ApplicationCustomerDto {
    
    @JsonProperty("uuid")
    private String uuid;

        	@JsonProperty("parentNumber")
    	private String parentNumber;
	
    	
    @JsonProperty("active") private Boolean isActive;
    
    @Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		ApplicationCustomerDto that = (ApplicationCustomerDto) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}
	}