package com.helpers;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class PartReader {
	private Object[] parts;
	private String[] names;
	private HashMap<String, Integer> indexMap;
	private Vector<String[]> instances;

	public static Object toObject(Class<?> clazz, String value) {
		if (Boolean.class.isAssignableFrom(clazz))
			return Boolean.parseBoolean(value);
		if (Byte.class.isAssignableFrom(clazz))
			return Byte.parseByte(value);
		if (Short.class.isAssignableFrom(clazz))
			return Short.parseShort(value);
		if (Integer.class.isAssignableFrom(clazz))
			return Integer.parseInt(value);
		if (Long.class.isAssignableFrom(clazz))
			return Long.parseLong(value);
		if (Float.class.isAssignableFrom(clazz))
			return Float.parseFloat(value);
		if (Double.class.isAssignableFrom(clazz))
			return Double.parseDouble(value);
		return value;
	}

	public boolean convertToObjects(Class<?> partClass, Map<String, String> attributeMap) {
		if (this.instances == null || this.names == null || this.indexMap == null)
			return false;
		this.parts = new Object[this.instances.size()];
		for (int i = 0; i < this.parts.length; i++) {
			Object newPart;
			try {
				newPart = partClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				System.err.println("Object was not initialized");
				continue;
			}
			for (String key : attributeMap.keySet()) {
				try {
					Field field = partClass.getField(key);
					field.set(newPart, toObject(field.getType(), this.instances.get(i)[indexMap.get(attributeMap.get(key).toLowerCase())]));
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					System.out.println("Field " + key + " was not found");
				}
			}
			this.parts[i] = newPart;
		}
		return true;
	}

	public boolean loadInstances(String fileName) {
		if (fileName == null)
			return false;
		File file = new File(fileName);
		try {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(file);
			boolean namesInit = false;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().replaceAll("\u0000", "");
				if (line.length() > 1 && line.charAt(0) == '%') {
					if (line.charAt(1) == 'N' || line.charAt(1) == 'n') {
						if (namesInit) {
							System.err.println("Names have been initialized allready from the file \"" + fileName + "\"...");
							return false;
						}
						names = line.substring(3).toLowerCase().split("(','|')");
						indexMap = new HashMap<String, Integer>();
						for (int i = 0; i < names.length; i++) {
							if (indexMap.keySet().contains(names[i])) {
								System.err.println("Column with name \"" + names[i] + "\" was found two times in the file \"" + fileName + "\"...");
								return false;
							}
							indexMap.put(names[i], i);
						}
						instances = new Vector<String[]>();
						namesInit = true;
					}
				} else if (namesInit && line.length() > 0) {
					String[] values = line.substring(1).split("(','|')");
					String[] instance = Arrays.copyOf(values, names.length);
					instances.add(instance);
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}