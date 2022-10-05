package com.digitify.ob;

import com.digitify.ob.service.AggregateApiGenService;
import com.digitify.ob.service.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class AggregateApiGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggregateApiGenApplication.class, args);
		generate(Constants.name, Constants.mainComponent, Constants.childList);
	}


	public static void generate(String name, String mainComponent, String childList){
		AggregateApiGenService aggregateGenService = new AggregateApiGenService(name, mainComponent, childList);
		try {
			aggregateGenService.generate();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
