package com.digitify.ob;

import com.digitify.ob.service.Constants;
import com.digitify.ob.service.ParentUnitGenService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ParentUnitGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParentUnitGenApplication.class, args);
		generate(Constants.mainComponent);
	}


	public static void generate(String name){
		ParentUnitGenService apiStructureGenService = new ParentUnitGenService(name);
		try {
			apiStructureGenService.generate();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
