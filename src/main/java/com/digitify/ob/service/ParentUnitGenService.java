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
public class ParentUnitGenService {
	private String name;
	private String parent;
	static String outputFileBase;

	public ParentUnitGenService(String name) {
		this.name = name;
		this.parent = "ApplicationCustomer";
		outputFileBase = "src/main/java/com/digitify/ob/velo/unit/" + this.name.toLowerCase() + "/";
	}

	private VelocityContext getVelocityContext() {
		VelocityContext context = new VelocityContext();
		context.put("PACKAGE_NAME", "com.digitify.ob.velo.unit." + this.name.toLowerCase());
		context.put("className", this.name);
		context.put("NAME", this.name);
		context.put("Base_package", "com.digitify.ob");
		context.put("Table_name", this.name.toLowerCase());
		context.put("Type_Enum_List", "Type1,Type2,Type3");
		context.put("Status_Enum_List", "Status1,Status2,Status3");
//		context.put("Parent", parent);
		context.put("Parent", "");
		context.put("Principal", "ApplicationCustomer");
		context.put("Api_context_path", "/api/" + this.name.toLowerCase());

		return context;
	}

	public void generate() throws IOException {
		DirectoryService.createDir(outputFileBase);
		System.out.println("Hello world!");
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();
		VelocityContext context = getVelocityContext();
		String template0 = "velocity/units/UnitSturcture.java";
		String outputFile = outputFileBase + "Unit_0.java";
		Writer writer = new FileWriter(new File(outputFile));
		Velocity.mergeTemplate(template0, "UTF-8", context, writer);
		writer.flush();
		writer.close();
		for (int i = 1; i <= 22; i++) {
			String template1 = "velocity/units/UnitSturcture.java.child." + i + ".java";
			String outputFile1 = outputFileBase + "Unit_" + i + ".java";
			Writer writer1 = new FileWriter(new File(outputFile1));
			Velocity.mergeTemplate(template1, "UTF-8", context, writer1);
			writer1.flush();
			writer1.close();
			System.out.println("Generated " + outputFile1);
		}

	}

}
