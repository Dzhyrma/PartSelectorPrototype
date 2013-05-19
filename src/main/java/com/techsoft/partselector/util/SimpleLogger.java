package com.techsoft.partselector.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.techsoft.partselector.consts.Paths;

public class SimpleLogger {

	public static Logger getLogger(Class<?> clazz) {
		Logger result = Logger.getLogger(clazz.getName());
		new File(Paths.LOG_PATH).mkdirs();
		try {
			FileHandler fileHandler = new FileHandler(Paths.LOG_PATH + clazz.getSimpleName() + ".txt", true);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			result.addHandler(fileHandler);
			result.setLevel(Level.ALL);
		} catch (SecurityException | IOException e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
}
