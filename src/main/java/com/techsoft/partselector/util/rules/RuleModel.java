package com.techsoft.partselector.util.rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Model that represents rule in the prototype. Used to store additional
 * information about types of the parts rule is searches for and about
 * additional sizes or whatever.
 * 
 * @author Andrii Dzhyrma */
public class RuleModel implements Serializable, Comparable<RuleModel> {

	private static final long serialVersionUID = -5143479027815819459L;

	/** @uml.property name="fileName" */
	private String fileName;
	private Map<String, Class<?>> inputParameters;
	/** @uml.property name="classes" */
	private Set<String> classes;

	/** @return current parameters. */
	public List<String> getParameters() {
		return new ArrayList<String>(this.inputParameters.keySet());
	}

	/** Creates a new model for the rule with the given name.
	 * 
	 * @param fileName - name of the file of rule. */
	public RuleModel(String fileName) {
		this.fileName = fileName;
		this.inputParameters = new HashMap<String, Class<?>>();
		this.classes = new HashSet<String>();
	}

	/** @param name - name of the parameter.
	 * @return type of the parameter with the given name. */
	public Class<?> getParameter(String name) {
		return this.inputParameters.get(name);
	}

	/** Sets type of the parameter with the given name.
	 * 
	 * @param name - name of the parameter.
	 * @param type - type to be assigned. */
	public final void setParameter(String name, Class<?> type) {
		if (name != null && name.length() > 0 && type != null)
			this.inputParameters.put(name, type);
	}

	/** Removes parameter with the given name.
	 * 
	 * @param name - name of the parameter to be removed. */
	public final void removeParameter(String name) {
		this.inputParameters.remove(name);
	}

	/** Adds type of the part the rule is searching for.
	 * 
	 * @param className - name of the class that represents a type of the part. */
	public final void addClass(String className) {
		this.classes.add(className);
	}

	/** Removes type of the part the rule is searching for.
	 * 
	 * @param className - name of the class that represents a type of the part. */
	public final void removeClass(String className) {
		this.classes.remove(className);
	}

	/** @return all the types rule is searching for.
	 * @uml.property name="classes" */
	public final Set<String> getClasses() {
		return new HashSet<String>(this.classes);
	}

	/** @return the file name of the current rule.
	 * @uml.property name="fileName" */
	public final String getFileName() {
		return this.fileName;
	}

	/** Sets a new file name of the current rule.
	 * 
	 * @param fileName - new file name.
	 * @uml.property name="fileName" */
	public final void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return this.fileName;
	}

	@Override
	public int compareTo(RuleModel o) {
		return this.fileName.compareTo(o.fileName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RuleModel)
			return this.compareTo((RuleModel) obj) == 0;
		return false;
	}

}
