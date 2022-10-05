#set($basePackage = ${Base_package})
#set($unitPackage = ${Unit_package})
#set($mainComponent = ${Main_component})
#set($camelCaseMainComponent = $mainComponent.substring(0,1).toLowerCase()+$mainComponent.substring(1))
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end


import ${unitPackage}.${mainComponent.toLowerCase()}.${mainComponent}Dto;
import ${unitPackage}.${mainComponent.toLowerCase()}.${mainComponent}DataService;
#if($Child_list && $Child_list != "")
#foreach($child in $Child_list.split(","))
#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
import ${unitPackage}.${child.toLowerCase()}.${child}Dto;
import ${unitPackage}.${child.toLowerCase()}.${child}DataService;

#end
#end

import com.digitify.framework.annotation.AggregateService;
import ${basePackage}.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationException;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.TrackCode;
import com.digitify.framework.util.Rethrow;
import com.digitify.framework.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

#parse("File Header.java")


@Slf4j
@AggregateService
public class ${NAME}Service {

@Autowired private ${NAME}Mapper aggregateMapper;
@Autowired private ${mainComponent}DataService ${camelCaseMainComponent}DataService;
#foreach ($child in $Child_list.split(","))
#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
@Autowired private ${child}DataService ${camelCaseChild}DataService;
#end


public List<${NAME}Dto> getAll${NAME}() {
List<${NAME}Dto> aggregateDtoList = null;
try {
aggregateDtoList = Stream.ofNullable(${camelCaseMainComponent}DataService.getAll())
.flatMap(Collection::stream)
.map(Rethrow.rethrowFunction(${camelCaseMainComponent}Dto -> {
${NAME} aggregate = new ${NAME}();
aggregate.set${mainComponent}Dto(${camelCaseMainComponent}Dto);
#foreach ($child in $Child_list.split(","))
#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
aggregate.set${child}DtoList(${camelCaseChild}DataService.getAllBy${mainComponent}Uuid(${camelCaseMainComponent}Dto.getUuid()));
#end
return aggregate;
})).map(${camelCaseMainComponent}Aggregate -> aggregateMapper.toAggregateDto(${camelCaseMainComponent}Aggregate)).collect(Collectors.toList());
} catch (ApplicationUncheckException e) {
throw new ApplicationException(e.getCode(), e.getMessage());
}
return aggregateDtoList;
}

public ${NAME}Dto get${NAME}(String uuid) {
TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE).with(RequestType.GET).with(LayerType.SERVICE_LAYER).with(EntityType.${mainComponent.toUpperCase()}.toString()).build();
${NAME}Dto aggregateDto = null;
try {
if (StringUtils.hasLength(uuid)) {
${NAME} aggregate = new ${NAME}();
aggregate.set${mainComponent}Dto(${camelCaseMainComponent}DataService.getByUuid(uuid)
.orElseThrow(() -> new ApplicationException(trackCode, "Invalid ${camelCaseMainComponent} uuid.")));
#foreach ($child in $Child_list.split(","))
#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
aggregate.set${child}DtoList(${camelCaseChild}DataService.getAllBy${mainComponent}Uuid(uuid));
#end
aggregateDto = aggregateMapper.toAggregateDto(aggregate);
}
} catch (ApplicationUncheckException e) {
throw new ApplicationException(e.getCode(), e.getMessage());
}
return aggregateDto;
}

@Transactional
public ${NAME}Dto save${NAME}(${NAME}Dto dto) {
TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE).with(RequestType.POST).with(LayerType.SERVICE_LAYER).with(EntityType.${mainComponent.toUpperCase()}.toString()).build();
try {
return Optional.ofNullable(dto)
.map(${camelCaseMainComponent}AggregateDto -> aggregateMapper.toAggregate(${camelCaseMainComponent}AggregateDto))
.flatMap(Rethrow.rethrowFunction(${camelCaseMainComponent}Aggregate -> ${camelCaseMainComponent}DataService.save(${camelCaseMainComponent}Aggregate.get${mainComponent}Dto())))
.map(savedDto -> {
${mainComponent}Aggregate aggregate = new ${mainComponent}Aggregate();
aggregate.set${mainComponent}Dto(savedDto);
#foreach ($child in $Child_list.split(","))
#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
List<${child}Dto> ${camelCaseChild}DtoList = save${child}s(dto.get${child}DtoList(), savedDto)
.filter(dtoList1 -> !dtoList1.isEmpty())
.orElseThrow(() -> new BadRequestException(trackCode, "Unable to save ${camelCaseChild}s."));
aggregate.set${child}DtoList(${camelCaseChild}DtoList);
#end
return aggregateMapper.toAggregateDto(aggregate);
})
.orElseThrow(() -> new BadRequestException(trackCode, "Unable to save."));
} catch (ApplicationUncheckException e) {
throw new ApplicationException(e.getCode(), e.getMessage());
}
}

@Transactional
public void delete${mainComponent}Aggregate(String uuid) {
try {
if(StringUtils.hasLength(uuid)){
#foreach ($child in $Child_list.split(","))
#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
${camelCaseChild}DataService.delete(${camelCaseChild}DataService.getAllBy${mainComponent}Uuid(uuid));
#end
${camelCaseMainComponent}DataService.delete(uuid);
}
} catch (ApplicationUncheckException e) {
throw new ApplicationException(e.getCode(), e.getMessage());
}
}

#foreach ($child in $Child_list.split(","))
#set($camelCaseChild =$child.substring(0,1).toLowerCase()+$child.substring(1))
private Optional<List<${child}Dto>> save${child}s(List<${child}Dto> ${camelCaseChild}DtoList, ${mainComponent}Dto ${mainComponent.toLowerCase()}Dto) {
try {
return Optional.ofNullable(${mainComponent.toLowerCase()}Dto.getUuid())
.map(uuid -> Stream.ofNullable(${camelCaseChild}DtoList)
.flatMap(Collection::stream)
.peek(dto -> dto.set${mainComponent}Uuid(uuid))
.collect(Collectors.toList()))
.map(Rethrow.rethrowFunction(dtoList1 -> ${camelCaseChild}DataService.save(dtoList1)));
} catch (ApplicationUncheckException e) {
throw new ApplicationException(e.getCode(), e.getMessage());
}
}
#end
}
