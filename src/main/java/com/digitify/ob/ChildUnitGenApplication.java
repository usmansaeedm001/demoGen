package com.digitify.ob;

import com.digitify.ob.service.ChildUnitGenService;
import com.digitify.ob.service.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

@SpringBootApplication
public class ChildUnitGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChildUnitGenApplication.class, args);
		generate(Constants.account, Constants.mainComponent);
		generate(Constants.bankAccount, Constants.account + "," + Constants.mainComponent);
	}

	public static void generate(String name, String parent){
		ChildUnitGenService childApiGenService = new ChildUnitGenService(name, parent);
		try {
			childApiGenService.generate();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}


}
