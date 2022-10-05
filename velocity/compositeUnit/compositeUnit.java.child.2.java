#set($PARENT = ${Parent})
#set($basePackage = ${Base_package})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end


import ${basePackage}.enums.EntityType;
import com.digitify.framework.annotation.IntegrationService;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationException;
import com.digitify.framework.handler.TrackCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.logging.Level.FINE;

#parse("File Header.java")

@Slf4j
@IntegrationService
public class ${NAME}IntegrationServiceImpl implements ${NAME}IntegrationService {
#if(${NAME} && ${NAME} != "")

	@Autowired WebClient.Builder webClientBuilder;
	@Value("{app.${NAME.toLowerCase()}-service.host}") String serviceHost;
	@Value("{app.${NAME.toLowerCase()}-service.port}") String servicePort;

	@Override
	public Flux<${NAME}Dto> getAll() {
		String uri = String.format("http://%s:%s", serviceHost, servicePort);
		Flux<${NAME}Dto> flux = webClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToFlux(${NAME}Dto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Flux.empty());
		return flux;
	}

	@Override
	public Flux<${NAME}Dto> search(${NAME}Dto dto, int pageNo, int pageSize) {
		String uri = String.format("http://%s:%s/%s/%s", serviceHost, servicePort, pageNo, pageSize);
		Flux<${NAME}Dto> flux = webClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToFlux(${NAME}Dto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Flux.empty());
		return flux;
	}

	#if(${PARENT} && ${PARENT} != "")
		#foreach($parent in $PARENT.split(","))
			#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
			@Override
			public Flux<${NAME}Dto> getAllBy${parent}Uuid(String uuid) {
				String uri = String.format("http://%s:%s/${PARENT}/%s", serviceHost, servicePort, uuid);
				Flux<${NAME}Dto> flux = webClientBuilder.build()
					.get()
					.uri(uri)
					.retrieve()
					.bodyToFlux(${NAME}Dto.class)
					.log(log.getName(), FINE)
					.onErrorResume(throwable -> Flux.empty());
				return flux;
			}
		#end
	#end



	@Override
	public Mono<${NAME}Dto> get(String uuid) {
		String uri = String.format("http://%s:%s/%s", serviceHost, servicePort, uuid);
		Mono<${NAME}Dto> mono = webClientBuilder.build().get().uri(uri).retrieve().bodyToMono(${NAME}Dto.class).log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.empty());
		return mono;
	}

	@Override
	public Mono<${NAME}Dto> save(${NAME}Dto dto) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE).with(RequestType.POST).with(LayerType.AGGREGATION_LAYER).with(EntityType.${NAME.toUpperCase()}.toString())
			.build();
		String uri = String.format("http://%s:%s", serviceHost, servicePort);
		Mono<${NAME}Dto> mono = webClientBuilder.build().post().uri(uri).accept(MediaType.APPLICATION_JSON).bodyValue(dto).retrieve()
			.bodyToMono(${NAME}Dto.class).log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(trackCode, "Unable to save customer dto.")));
		return mono;
	}

	@Override
	public Mono<${NAME}Dto> update(String uuid, ${NAME}Dto dto) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE).with(RequestType.PUT).with(LayerType.AGGREGATION_LAYER).with(EntityType.${NAME.toUpperCase()}.toString())
			.build();
		String uri = String.format("http://%s:%s/%s", serviceHost, servicePort, uuid);
		Mono<${NAME}Dto> mono = webClientBuilder.build().put().uri(uri).accept(MediaType.APPLICATION_JSON).bodyValue(dto).retrieve()
			.bodyToMono(${NAME}Dto.class).log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(trackCode, "Unable to update customer dto.")));
		return mono;
	}

	@Override
	public Mono<${NAME}Dto> partialUpdate(${NAME}Dto dto) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE).with(RequestType.PATCH).with(LayerType.AGGREGATION_LAYER).with(EntityType.${NAME.toUpperCase()}.toString())
			.build();
		String uri = String.format("http://%s:%s", serviceHost, servicePort);
		Mono<${NAME}Dto> mono = webClientBuilder.build().patch().uri(uri).accept(MediaType.APPLICATION_JSON).bodyValue(dto).retrieve()
			.bodyToMono(${NAME}Dto.class).log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(trackCode, "Unable to partial update customer dto.")));
		return mono;
	}

	@Override
	public void delete(String uuid) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE).with(RequestType.DELETE).with(LayerType.AGGREGATION_LAYER).with(EntityType.${NAME.toUpperCase()}.toString())
			.build();
		String uri = String.format("http://%s:%s/%s", serviceHost, servicePort, uuid);
		webClientBuilder.build().delete().uri(uri).retrieve().bodyToMono(${NAME}Dto.class).log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(trackCode, "Unable to delete.")));
	}

	#if(${PARENT} && ${PARENT} != "")
		#foreach($parent in $PARENT.split(","))
			#set($parentCamelCase = $parent.substring(0,1).toLowerCase()+$parent.substring(1))
		@Override
		public void deleteAllBy${parent}Uuid(String uuid) {
			TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE).with(RequestType.DELETE).with(LayerType.AGGREGATION_LAYER).with(EntityType.${NAME.toUpperCase()}.toString())
				.build();
			String uri = String.format("http://%s:%s/${NAME.toLowerCase()}/%s", serviceHost, servicePort, uuid);
			webClientBuilder.build().delete().uri(uri).retrieve().bodyToMono(${NAME}Dto.class).log(log.getName(), FINE)
				.onErrorResume(throwable -> Mono.error(new ApplicationException(trackCode, "Unable to delete.")));
		}
		#end
	#end

#end

}

