package com.techsoft.partselector.util.io;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import com.techsoft.partselector.model.Part;

/** Reads parts from the files using mapping technique.
 * 
 * @author Andrii Dzhyrma */
public class PartReader {
	/** @uml.property name="parts"
	 * @uml.associationEnd multiplicity="(0 -1)" */
	private Part[] parts;
	/** @uml.property name="names" */
	private String[] names;
	private HashMap<String, Integer> indexMap;
	private Vector<String[]> instances;

	private static Object toObject(Class<?> clazz, String value) {
		if (boolean.class.isAssignableFrom(clazz))
			return Boolean.parseBoolean(value);
		if (byte.class.isAssignableFrom(clazz))
			return Byte.parseByte(value);
		if (short.class.isAssignableFrom(clazz))
			return Short.parseShort(value);
		if (int.class.isAssignableFrom(clazz))
			return Integer.parseInt(value);
		if (long.class.isAssignableFrom(clazz))
			return Long.parseLong(value);
		if (float.class.isAssignableFrom(clazz))
			return Float.parseFloat(value);
		if (double.class.isAssignableFrom(clazz))
			return Double.parseDouble(value);
		return value;
	}

	/** Converts instances read from the file to the objects of the given class
	 * using attribute map.
	 * 
	 * @param clazz - class convert instances to.
	 * @param attributeMap - map of the attribute names (name in the file ->
	 *            name in the object)
	 * @return true, if conversion was successful. */
	public boolean convertToObjects(Class<? extends Part> clazz, Map<String, String> attributeMap) {
		if (this.instances == null || this.names == null || this.indexMap == null)
			return false;
		this.parts = new Part[this.instances.size()];
		for (int i = 0; i < this.parts.length; i++) {
			if (!attributeMap.containsKey("name")) {
				System.out.println("Missing attribute 'name'");
				continue;
			}
			Part newPart;
			try {
				Integer index = this.indexMap.get(attributeMap.get("name").toLowerCase());
				if (index == null) {
					System.out.println("Missing attribute 'name'");
					continue;
				}
				newPart = clazz.getConstructor(new Class<?>[] { String.class }).newInstance(this.instances.get(i)[index]);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
			                | SecurityException e) {
				System.err.println("Object was not initialized: " + e.getMessage());
				continue;
			}
			for (String key : attributeMap.keySet()) {
				try {
					Integer index = this.indexMap.get(attributeMap.get(key).toLowerCase());
					if (index == null)
						continue;
					Field field = clazz.getField(key);
					field.set(newPart, toObject(field.getType(), this.instances.get(i)[index]));
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					System.out.println("Field " + key + " was not found");
				}
			}
			this.parts[i] = newPart;
		}
		return true;
	}

	/** Loads instances to the current object (rows in the file)
	 * 
	 * @param fileName - name of the file with information about parts.
	 * @return true, if instances were read successfully. */
	public boolean loadInstances(String fileName) {
		if (fileName == null)
			return false;
		File file = new File(fileName);
		if (!file.exists())
			return false;
		return loadInstances(file);
	}

	/** Loads instances to the current object (rows in the file)
	 * 
	 * @param file - file with information about parts.
	 * @return true, if instances were read successfully. */
	@SuppressWarnings("resource")
	public boolean loadInstances(File file) {
		try {
			Scanner scanner = new Scanner(file);
			boolean namesInit = false;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().replaceAll("\u0000", "");
				if (line.length() > 1 && line.charAt(0) == '%') {
					if (line.charAt(1) == 'N' || line.charAt(1) == 'n') {
						if (namesInit) {
							System.err.println("Names have been initialized allready from the file \"" + file.getName() + "\"...");
							return false;
						}
						names = line.substring(3).toLowerCase().split("(','|')");
						indexMap = new HashMap<String, Integer>();
						for (int i = 0; i < names.length; i++) {
							if (indexMap.keySet().contains(names[i])) {
								System.err.println("Column with name \"" + names[i] + "\" was found two times in the file \"" + file.getName() + "\"...");
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

	/** Gets the list of converted instances to objects.
	 * 
	 * @return
	 * @uml.property name="parts" */
	public List<Part> getParts() {
		List<Part> result = new ArrayList<Part>();
		if (this.parts == null)
			return result;
		for (Part part : this.parts)
			result.add(part);
		return result;
	}

	/** @return
	 * @uml.property name="names" */
	public String[] getNames() {
		return Arrays.copyOf(this.names, this.names.length);
	}
}