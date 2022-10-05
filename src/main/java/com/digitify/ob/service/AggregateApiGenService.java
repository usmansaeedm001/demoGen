package com.digitify.ob.service;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Usman
 * @created 8/29/2022 - 2:14 AM
 * @project demoGen
 */
public class AggregateApiGenService {

	private final String name;
	private final String parent;
	private final String childList;
	private final String outputFileBase;
	private final String PACKAGE_NAME;
	private final String basePackage;
	private final String unitPackage;

	public AggregateApiGenService(String name, String parent, String childString) {
		this.name = name;
		this.parent = parent;
		this.childList = childString;
		this.outputFileBase = "src/main/java/com/digitify/ob/velo/aggregate/"+ this.parent.toLowerCase()+"/";
		this.PACKAGE_NAME = "com.digitify.ob.velo.aggregate."+this.parent.toLowerCase() ;
		this.basePackage = "com.digitify.ob";
		this.unitPackage = "com.digitify.ob.velo.unit";

	}


	private VelocityContext getVelocityContext(){
		VelocityContext context = new VelocityContext();
		context.put("PACKAGE_NAME", this.PACKAGE_NAME);
		context.put("className", this.parent);
		context.put("NAME", this.name);
		context.put("Base_package", this.basePackage);
		context.put("Unit_package", this.unitPackage);
		context.put("Table_name", name.toLowerCase());
		context.put("Main_component", this.parent);
		context.put("Child_list", this.childList);
		context.put("Api_context_path", "/aggregate/"+ this.name.toLowerCase());

		return context;
	}
	public void generate() throws IOException {
		DirectoryService.createDir(outputFileBase);
		System.out.println("Hello world!");
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();
		VelocityContext context = getVelocityContext();
		String template = "velocity/aggregate/AggregateStructure.java";
		String outputFile = outputFileBase + "CompositeStructure.java";
		Writer writer1 = new FileWriter(new File(outputFile));
		Velocity.mergeTemplate(template, "UTF-8", context, writer1);
		writer1.flush();
		writer1.close();
		System.out.println("Generated " + outputFile);
		for (int i = 0; i <= 6 ; i++) {
			template = "velocity/aggregate/AggregateStructure.java.child."+i+".java";
			outputFile = outputFileBase + "aggregate"+i+".java";
			writer1 = new FileWriter(new File(outputFile));
			Velocity.mergeTemplate(template, "UTF-8", context, writer1);
			writer1.flush();
			writer1.close();
			System.out.println("Generated " + outputFile);
		}

	}

}
