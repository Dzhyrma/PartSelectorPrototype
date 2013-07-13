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

/** Loads classes from the folder using *.class extensions.
 * 
 * @author Andrii Dzhyrma */
public class ClassReader extends URLClassLoader {

	private static final Logger LOGGER = SimpleLogger.getLogger(ClassReader.class);
	/** @uml.property name="instance"
	 * @uml.associationEnd */
	private static volatile ClassReader instance;
	//private static URLClassLoader classLoader;
	private static final Map<String, Class<?>> stringPrimitivesMap;
	private static final Map<Character, Class<?>> charPrimitivesMap;
	private static URL[] urls;
	// initializing static maps for primitives
	static {
		File dir = new File(Paths.CLASS_PATH);
		try {
			urls = new URL[] { dir.toURI().toURL() };
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, "Wrong class path");
			System.exit(0);
		}

		stringPrimitivesMap = new HashMap<String, Class<?>>();
		stringPrimitivesMap.put("boolean", boolean.class);
		stringPrimitivesMap.put("byte", byte.class);
		stringPrimitivesMap.put("char", char.class);
		stringPrimitivesMap.put("double", double.class);
		stringPrimitivesMap.put("float", float.class);
		stringPrimitivesMap.put("int", int.class);
		stringPrimitivesMap.put("long", long.class);
		stringPrimitivesMap.put("short", short.class);

		charPrimitivesMap = new HashMap<Character, Class<?>>();
		charPrimitivesMap.put(Character.valueOf('Z'), boolean.class);
		charPrimitivesMap.put(Character.valueOf('B'), byte.class);
		charPrimitivesMap.put(Character.valueOf('C'), char.class);
		charPrimitivesMap.put(Character.valueOf('D'), double.class);
		charPrimitivesMap.put(Character.valueOf('F'), float.class);
		charPrimitivesMap.put(Character.valueOf('I'), int.class);
		charPrimitivesMap.put(Character.valueOf('J'), long.class);
		charPrimitivesMap.put(Character.valueOf('S'), short.class);
	}

	private ClassReader() {
		super(urls, ClassLoader.getSystemClassLoader());
	}

	private Class<?> loadWithPrimitives(String className) throws ClassNotFoundException {
		if (className == null || className.length() == 0)
			return null;
		if (stringPrimitivesMap.containsKey(className))
			return stringPrimitivesMap.get(className);
		int arrayDimension = 0;
		while (className.charAt(arrayDimension) == '[')
			arrayDimension++;
		String type = className.substring(arrayDimension);
		if (type.length() == 0)
			return null;
		Class<?> clazz;
		// check for the primitives
		if (type.length() == 1 && charPrimitivesMap.containsKey(type.charAt(0)))
			clazz = charPrimitivesMap.get(type.charAt(0));
		else if (type.charAt(0) == 'L' && type.charAt(type.length() - 1) == ';')
			clazz = this.loadClass(type.substring(1, type.length() - 1), true);
		else
			clazz = this.loadClass(type, true);
		if (arrayDimension > 0)
			return Array.newInstance(clazz, new int[arrayDimension]).getClass();
		else
			return clazz;
	}

	/** Gets the instance of current class reader.
	 * 
	 * @return current instance
	 * @uml.property name="instance" */
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

	/** @return names of all classes loaded so far. */
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

	/** @return map of between classes' names and classes themselves. */
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
