#set($basePackage = ${Base_package})
#set($integrationPackage = ${Integration_package})
#set($mainComponent = ${Main_component})
#set($PARENT = ${Main_component})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.annotation.AggregateDataService;
import ${integrationPackage}.card.CardIntegrationService;
import ${integrationPackage}.customer.CustomerIntegrationService;
import ${integrationPackage}.transaction.TransactionIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

#parse("File Header.java")



@AggregateDataService
public class ${NAME}AdapterImpl implements ${NAME}Adapter {

	@Autowired private ${NAME}Mapper aggregateMapper;
	#set($camelCaseParent =${PARENT.substring(0,1).toLowerCase()}+$PARENT.substring(1))
	@Autowired private ${PARENT}IntegrationService ${camelCaseParent}IntegrationService;
	#if($Child_list && $Child_list != "")
		#foreach($child in $Child_list.split(","))
			#set($camelCaseChild =${child.substring(0,1).toLowerCase()}+$child.substring(1))
    		@Autowired private ${child}IntegrationService ${camelCaseChild}IntegrationService;
    	#end
	#end


	@Override
	public Flux<${NAME}Dto> getAll() {
		return null;
	}

	@Override
	public Flux<${NAME}Dto> search(${NAME}Dto dto, int pageNo, int pageSize) {
		return null;
	}

	@Override
	public Mono<${NAME}Dto> get(String uuid) {
		return null;
	}

	@Override
	public Mono<${NAME}Dto> save(${NAME}Dto dto) {
		return null;
	}

	@Override
	public Mono<${NAME}Dto> update(String uuid, ${NAME}Dto dto) {
		return null;
	}

	@Override
	public Mono<${NAME}Dto> partialUpdate(${NAME}Dto dto) {
		return null;
	}

	@Override
	public void delete(String uuid) {

	}
}
