package com.techsoft.partselector.util.reflect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.model.Part;

public class ClassGenerator {
	private static final String CLASS_TEMPLATE = "public class %s {%n%s}";
	//private static final String DEFAULT_CONSTRUCTOR_TEMPLATE = "\tpublic String name;%n%n\tpublic %s(String name) {%n\t\tthis.name = name;%n\t}%n%n";
	private static final String FIELD_TEMPLATE = "\tpublic %s %s;%n";
	private static final String GETTER_TEMPLATE = "\tpublic final %s get%s() {%n\t\treturn this.%s;%n\t}%n%n";
	private static final String NAME_STRING = "name";
	private static final String SETTER_TEMPLATE = "\tpublic final void set%s(%s %s) {%n\t\tthis.%s = %s;%n\t}%n%n";
	private static final String SUPER_CLASS_CONSTRUCTOR_TEMPLATE = "%n\tpublic %s(String name) {%n\t\tsuper(name);%n\t}%n%n";

	private Set<String> declaredFields = new HashSet<String>();
	private Map<String, Class<?>> fields = new HashMap<String, Class<?>>();
	private String name;
	private Class<?> superClass = Part.class;

	public ClassGenerator(Class<?> someClass) {
		if (someClass == null)
			return;
		this.name = someClass.getName();
		this.superClass = someClass.getSuperclass();
		for (Field field : someClass.getDeclaredFields())
			this.fields.put(field.getName(), field.getType());
		Class<?> superClass = this.superClass;
		while (superClass != null) {
			for (Field field : superClass.getDeclaredFields())
				if (field.getModifiers() == Modifier.PUBLIC)
					this.declaredFields.add(field.getName());
			superClass = superClass.getSuperclass();
		}
	}

	public ClassGenerator(String name) {
		this.name = name;
	}

	public void addField(String fieldName, Class<?> fieldType) {
		if (isFieldExists(fieldName)) {
			System.err.println("Field with this name already exists.");
			return;
		}
		this.fields.put(fieldName, fieldType);
	}

	public final String getName() {
		return this.name;
	}

	public final Class<?> getSuperClass() {
		return this.superClass;
	}

	public boolean isFieldExists(String fieldName) {
		if (fieldName.equals(NAME_STRING) || this.declaredFields.contains(fieldName) || this.fields.containsKey(fieldName))
			return true;
		return false;
	}

	public void removeField(String fieldName) {
		if (!this.fields.containsKey(fieldName))
			return;
		this.fields.remove(fieldName);
	}

	public void saveClass() {
		StringBuilder fields = new StringBuilder();
		StringBuilder getters = new StringBuilder();
		StringBuilder setters = new StringBuilder();
		for (String fieldName : this.fields.keySet()) {
			if (fieldName.equals(NAME_STRING))
				continue;
			String fieldNameFirstCapital = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			String fieldType = this.fields.get(fieldName).getName();
			fields.append(String.format(FIELD_TEMPLATE, fieldType, fieldName));
			getters.append(String.format(GETTER_TEMPLATE, fieldType, fieldNameFirstCapital, fieldName));
			setters.append(String.format(SETTER_TEMPLATE, fieldNameFirstCapital, fieldType, fieldName, fieldName, fieldName));
		}

		StringBuilder classBody = new StringBuilder();
		classBody.append(fields);
		//classBody.append((this.superClass == null) ? String.format(DEFAULT_CONSTRUCTOR_TEMPLATE, this.name) : String.format(SUPER_CLASS_CONSTRUCTOR_TEMPLATE, this.name));
		classBody.append(String.format(SUPER_CLASS_CONSTRUCTOR_TEMPLATE, this.name));
		classBody.append(getters);
		classBody.append(setters);

		String classFullText =
		    String.format(CLASS_TEMPLATE, (this.superClass == null) ? this.name : (this.name + " extends " + this.superClass.getName()), classBody);
		File file = new File(Paths.JAVA_PATH + File.separator + this.name + ".java");
		try {
			PrintWriter fileOut = new PrintWriter(file);
			fileOut.write(classFullText);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final void setSuperClass(Class<? extends Part> superClass) {
		if (superClass == null)
			this.superClass = Part.class;
		this.superClass = superClass;
	}

	public void updateFieldName(String oldFieldName, String newFieldName) {
		if (oldFieldName.equals(NAME_STRING) || isFieldExists(newFieldName) || !this.fields.containsKey(oldFieldName))
			return;
		this.fields.put(newFieldName, this.fields.get(oldFieldName));
		this.fields.remove(oldFieldName);
	}

	public void updateFieldType(String fieldName, Class<?> newFieldType) {
		if (fieldName.equals(NAME_STRING) || !this.fields.containsKey(fieldName))
			return;
		this.fields.put(fieldName, newFieldType);
	}

	public String[] getFieldNames() {
		synchronized (this) {
			if (this.declaredFields == null)
				return new String[0];
			String[] result = new String[this.declaredFields.size() + this.fields.size()];
			int index = 0;
			for (String string : this.declaredFields)
				result[index++] = string;
			for (String string : this.fields.keySet())
				result[index++] = string;
			return result;
		}
	}
}
