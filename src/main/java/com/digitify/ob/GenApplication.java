package com.digitify.ob;

import com.digitify.ob.service.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenApplication.class, args);
		ParentUnitGenApplication.generate(Constants.mainComponent);
		ChildUnitGenApplication.generate(Constants.account, Constants.mainComponent);
		ChildUnitGenApplication.generate(Constants.bankAccount, Constants.account + "," + Constants.mainComponent);
		AggregateApiGenApplication.generate(Constants.name, Constants.mainComponent, Constants.childList);
		CompositeUnitGenApplication.generate(Constants.account,Constants.mainComponent, Constants.basePackege);
		CompositeAggregateGenApplication.generate(Constants.name, Constants.mainComponent, Constants.childList);
	}


}
