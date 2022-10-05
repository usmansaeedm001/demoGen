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
public class ChildUnitGenService {
	private final String name;
	private final String parent;
	private final String outputFileBase;

	public ChildUnitGenService(String name, String parent) {
		this.name = name;
		this.parent = parent;
		this.outputFileBase = "src/main/java/com/digitify/ob/velo/unit/" + this.name.toLowerCase() + "/";
	}

	private VelocityContext getVelocityContext() {
		VelocityContext context = new VelocityContext();
		context.put("PACKAGE_NAME", "com.digitify.ob.velo.unit." + name.toLowerCase());
		context.put("className", name);
		context.put("NAME", name);
		context.put("Base_package", "com.digitify.ob");
		context.put("Table_name", name.toLowerCase());
//		context.put("Type_Enum_List", "Type1,Type2,Type3");
		context.put("Type_Enum_List", "");
//		context.put("Status_Enum_List", "Status1,Status2,Status3");
		context.put("Status_Enum_List", "");
		context.put("Principal", "ApplicationCustomer");
		//		context.put("Principal", "");
		context.put("Parent", parent);
//		context.put("Parent", "");
		context.put("Api_context_path", "/api/" + name.toLowerCase());

		return context;
	}

	public void generate() throws IOException {
		DirectoryService.createDir(outputFileBase);
		System.out.println("Hello world!");
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();
		VelocityContext context = getVelocityContext();
		String template = "velocity/units/UnitSturcture.java";
		String outputFile = outputFileBase + "Child_0.java";
		Writer writer1 = new FileWriter(new File(outputFile));
		Velocity.mergeTemplate(template, "UTF-8", context, writer1);
		writer1.flush();
		writer1.close();
		for (int i = 1; i <= 22; i++) {
			template = "velocity/units/UnitSturcture.java.child." + i + ".java";
			outputFile = outputFileBase + "Child_" + i + ".java";
			writer1 = new FileWriter(new File(outputFile));
			Velocity.mergeTemplate(template, "UTF-8", context, writer1);
			writer1.flush();
			writer1.close();
			System.out.println("Generated " + outputFile);
		}

	}

}
