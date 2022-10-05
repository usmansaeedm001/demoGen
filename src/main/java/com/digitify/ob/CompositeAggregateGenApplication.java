package com.digitify.ob;

import com.digitify.ob.service.CompositeAggregateGenService;
import com.digitify.ob.service.CompositeUnitGenService;
import com.digitify.ob.service.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CompositeAggregateGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompositeAggregateGenApplication.class, args);
		generate(Constants.account,Constants.mainComponent, Constants.basePackege);
	}


	public static void generate(String name, String mainComponent, String childList){
		CompositeAggregateGenService compositeAggregateGenService = new CompositeAggregateGenService(name, mainComponent, childList);
		try {
			compositeAggregateGenService.generate();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
