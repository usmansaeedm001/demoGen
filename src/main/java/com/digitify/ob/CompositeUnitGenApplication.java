package com.digitify.ob;

import com.digitify.ob.service.CompositeUnitGenService;
import com.digitify.ob.service.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CompositeUnitGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompositeUnitGenApplication.class, args);
		generate(Constants.account,Constants.mainComponent, Constants.basePackege);
	}


	public static void generate(String name, String parent, String basePackage){
		CompositeUnitGenService compositeIntegrationService = new CompositeUnitGenService(name,parent, basePackage);
		try {
			compositeIntegrationService.generate();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
