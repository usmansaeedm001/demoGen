package com.digitify.ob.velo.unit.account;
import javax.persistence.*;
import com.digitify.framework.config.AbstractAuditing;
import lombok.*;
import java.util.Objects;

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
@Entity
@Table(name = "account")
public class Account extends AbstractAuditing{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

        	@Column(name = "childNumber")
    	private String childNumber;
	
    
        	@Column(name = "applicationCustomerUuid")
	    private String applicationCustomerUuid;
        								
	@Column(name = "active") private Boolean isActive = true;
    
    @Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		Account that = (Account) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}

}
