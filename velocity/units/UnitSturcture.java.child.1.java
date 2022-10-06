#set($basePackage = ${Base_package})
#set($tableName = ${Table_name})
#set($typeEnumList = ${Type_Enum_List})
#set($statusEnumList = ${Status_Enum_List})
#set($principal = ${Principal})
#set($PARENT = ${Parent})
#set($apiContextPath = ${Api_context_path})
#set($uniqueField = ${Unique_field})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import javax.persistence.*;
import com.digitify.framework.config.AbstractAuditing;
import lombok.*;
import java.util.Objects;

#parse("File Header.java")


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Entity
@Table(name = "${tableName}")
public class ${NAME} extends AbstractAuditing{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    #if(${uniqueField} && ${uniqueField} != "")
		#set($uniqueFieldCamelCase = $uniqueField.substring(0,1).toLowerCase()+$uniqueField.substring(1))
    	@Column(name = "${uniqueFieldCamelCase}")
    	private String ${uniqueFieldCamelCase};
	#end

    
    #if( ${principal} && ${principal} != "")
		#set($principalCamelCase = $principal.substring(0,1).toLowerCase()+$principal.substring(1))
    	@Column(name = "${principalCamelCase}Uuid")
	    private String ${principalCamelCase}Uuid;
    #end
    #if( ${PARENT} && ${PARENT} != "")
		#foreach($parent in $PARENT.split(","))
			#if( ${principal} !=  ${parent})
				#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
		    	@Column(name = "${parentCamelCase}Uuid")
		    	private String ${parentCamelCase}Uuid;
    		#end
		#end
	#end

	@Column(name = "active") private Boolean isActive = true;
    
    @Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		${NAME} that = (${NAME}) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}

}
