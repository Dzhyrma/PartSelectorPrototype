package com.techsoft.partselector.util.reflect;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.util.SimpleLogger;

public class ClassReader extends URLClassLoader {

	private static final Logger LOGGER = SimpleLogger.getLogger(ClassReader.class);
	private static volatile ClassReader instance;
	//private static URLClassLoader classLoader;
	private static URL[] urls;
	static {
		File dir = new File(Paths.CLASS_PATH);
		try {
			urls = new URL[] { dir.toURI().toURL() };
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, "Wrong class path");
			System.exit(0);
		}
	}

	private ClassReader() {
		super(urls, ClassLoader.getSystemClassLoader());
	}

	private Class<?> loadWithPrimitives(String className) throws ClassNotFoundException {
		if (className == null || className.length() == 0)
			return null;
		className = className.replaceFirst(";", "");
		int arrayDimension = 0;
		while (className.charAt(arrayDimension) == '[')
			arrayDimension++;
		String type = className.substring(arrayDimension);
		if (type.length() == 0)
			return null;
		Class<?> clazz;
		if (type.length() == 1) {
			switch (type.charAt(0)) {
			case 'Z':
				clazz = boolean.class;
				break;
			case 'B':
				clazz = byte.class;
				break;
			case 'C':
				clazz = char.class;
				break;
			case 'D':
				clazz = double.class;
				break;
			case 'F':
				clazz = float.class;
				break;
			case 'I':
				clazz = int.class;
				break;
			case 'J':
				clazz = long.class;
				break;
			case 'S':
				clazz = short.class;
				break;
			default:
				return null;
			}
		} else if (type.charAt(0) == 'L')
			clazz = this.loadClass(type.substring(1), true);
		else
			clazz = this.loadClass(type, true);
		if (arrayDimension > 0)
			return Array.newInstance(clazz, new int[arrayDimension]).getClass();
		else
			return clazz;
	}

	public synchronized static ClassReader getInstance() {
		ClassReader localInstance = instance;
		if (localInstance == null) {
			synchronized (ClassReader.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new ClassReader();
				}
			}
		}
		return localInstance;
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {
		return loadWithPrimitives(className);
	}

	public String[] getClassNames() {
		File dir = new File(Paths.CLASS_PATH);
		String[] classNames = dir.list(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return name.endsWith(".class");
			}
		});
		for (int i = 0; i < classNames.length; i++)
			classNames[i] = classNames[i].substring(0, classNames[i].length() - 6);
		return classNames;
	}

	public Map<String, Class<?>> getClassMap() {
		Map<String, Class<?>> result = new HashMap<String, Class<?>>();
		for (String className : this.getClassNames()) {
			try {
				Class<?> clazz = this.loadClass(className);
				result.put(className, clazz);
			} catch (ClassNotFoundException e) {
				LOGGER.log(Level.INFO, "Class was not found while mapping.");
			}
		}
		return result;
	}
}
