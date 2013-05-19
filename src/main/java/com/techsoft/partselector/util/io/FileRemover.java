package com.techsoft.partselector.util.io;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.util.SimpleLogger;
import com.techsoft.partselector.util.knowledgebase.PartLibrary;
import com.techsoft.partselector.util.reflect.ClassReader;

public class FileRemover {
	private final static Logger LOGGER = SimpleLogger.getLogger(FileRemover.class);

	private static boolean removeFiles(String className, Class<?> clazz) {
		File javaFile = new File(Paths.JAVA_PATH + className + ".java");
		File classFile = new File(Paths.CLASS_PATH + className + ".class");
		if (javaFile.exists())
			javaFile.delete();
		if (classFile.exists())
			classFile.delete();
		if (clazz != null)
			PartLibrary.getInstance().removeClass(clazz);
		return !javaFile.exists() && !classFile.delete();
	}

	public static boolean removeClass(String className) {
		Class<?> clazz = null;
		try {
			clazz = ClassReader.getInstance().loadClass(className);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "Part library still may have parts of deleted class : " + className);
		}
		boolean result = removeFiles(className, clazz);
		String[] classNames = ClassReader.getInstance().getClassNames();
		if (classNames == null)
			return result;
		for (String string : classNames) {
			try {
				Class<?> inheritedClass = ClassReader.getInstance().loadClass(string);
				if (inheritedClass != null && clazz.isAssignableFrom(inheritedClass))
					removeFiles(string, inheritedClass);
			} catch (ClassNotFoundException e) {
				LOGGER.log(Level.WARNING, "Part library still may have parts of deleted class : " + className);
			}
		}
		return result;
	}
}
