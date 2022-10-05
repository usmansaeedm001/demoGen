#set($basePackage = ${Base_package})
#set($integrationPackage = ${Integration_package})
#set($mainComponent = ${Main_component})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.digitify.framework.annotation.Mapper;
import ${integrationPackage}.${mainComponent.toLowerCase()}.${mainComponent}Dto;

#parse("File Header.java")



@Mapper
public class ${NAME}MapperImpl implements ${NAME}Mapper {


	@Override
	public ${NAME}Dto toAggregateDto(${NAME} aggregate) {
		if(aggregate == null || aggregate.get${mainComponent}Dto() == null){
			return null;
		}
		${NAME}Dto aggregateDto = new ${NAME}Dto();
		#foreach ($child in $Child_list.split(","))
        aggregateDto.set${child}DtoList(aggregate.get${child}DtoList());
		#end


		${mainComponent}Dto ${mainComponent.toLowerCase()}Dto = aggregate.get${mainComponent}Dto();
		aggregateDto.setUuid(${mainComponent.toLowerCase()}Dto.getUuid());
		return aggregateDto;
	}

	@Override
	public ${NAME} toAggregate(${NAME}Dto aggregateDto) {
		if(aggregateDto == null){
			return null;
		}
		${NAME} aggregate = new ${NAME}();
		#foreach ($child in $Child_list.split(","))
		aggregate.set${child}DtoList(aggregateDto.get${child}DtoList());
		#end

		${mainComponent}Dto ${mainComponent.toLowerCase()}Dto = new ${mainComponent}Dto();
		${mainComponent.toLowerCase()}Dto.setUuid(aggregateDto.getUuid());
		return aggregate;
	}
}
