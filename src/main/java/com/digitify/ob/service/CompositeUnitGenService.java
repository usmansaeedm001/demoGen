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
public class CompositeUnitGenService {

	private String name;
	private String parent;
	private String basePackage;
	static String outputFileBase;

	public CompositeUnitGenService(String name, String parent, String basePackage) {
		this.name = name;
		this.parent = parent;
		this.basePackage = basePackage;
		this.outputFileBase = "src/main/java/com/digitify/ob/velo/compositeUnit/"+ this.name.toLowerCase()+"/";
	}

	private VelocityContext getVelocityContext(){
		VelocityContext context = new VelocityContext();
		context.put("PACKAGE_NAME", basePackage+".velo.aggregation."+name.toLowerCase());
		context.put("className", this.name);
		context.put("NAME", this.name);
		context.put("Base_package", this.basePackage);
		context.put("Parent", this.parent);

		return context;
	}
	public void generate() throws IOException {
		DirectoryService.createDir(outputFileBase);
		System.out.println("Hello world!");
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();
		VelocityContext context = getVelocityContext();
		String template0 = "velocity/compositeUnit/compositeUnit.java";
		String outputFile = outputFileBase + "compositeUnit.java";
		Writer writer = new FileWriter(new File(outputFile));
		Velocity.mergeTemplate(template0, "UTF-8", context, writer);
		writer.flush();
		writer.close();
		for (int i = 0; i <= 2 ; i++) {
			String template1 = "velocity/compositeUnit/compositeUnit.java.child."+i+".java";
			String outputFile1 = outputFileBase + "compositeUnit"+i+".java";
			Writer writer1 = new FileWriter(new File(outputFile1));
			Velocity.mergeTemplate(template1, "UTF-8", context, writer1);
			writer1.flush();
			writer1.close();
			System.out.println("Generated " + outputFile1);
		}


	}

}
