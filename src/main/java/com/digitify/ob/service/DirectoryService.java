package com.digitify.ob.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Usman
 * @created 8/29/2022 - 3:14 AM
 * @project demoGen
 */
public class DirectoryService {

	public static void createDir(String outputFileBase){

		String path = outputFileBase;
		File pathAsFile = new File(path);

		if (!Files.exists(Paths.get(path))) {
			pathAsFile.mkdir();
		}

	}


}
