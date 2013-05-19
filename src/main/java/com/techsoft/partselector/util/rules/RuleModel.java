package com.techsoft.partselector.util.rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RuleModel implements Serializable, Comparable<RuleModel> {

	private static final long serialVersionUID = -5143479027815819459L;

	private String fileName;
	private Map<String, Class<?>> inputParameters;
	private Set<Class<?>> classes;

	public List<String> getParameters() {
		return new ArrayList<String>(this.inputParameters.keySet());
	}

	public RuleModel(String fileName) {
		this.fileName = fileName;
		this.inputParameters = new HashMap<String, Class<?>>();
		this.classes = new HashSet<Class<?>>();
	}

	public Class<?> getParameter(String name) {
		return this.inputParameters.get(name);
	}

	public final void setParameter(String name, Class<?> type) {
		if (name != null && name.length() > 0 && type != null)
			this.inputParameters.put(name, type);
	}

	public final void removeParameter(String name) {
		this.inputParameters.remove(name);
	}

	public final void addClass(Class<?> clazz) {
		this.classes.add(clazz);
	}

	public final void removeClass(Class<?> clazz) {
		this.classes.remove(clazz);
	}

	public final Set<Class<?>> getClasses() {
		return new HashSet<Class<?>>(this.classes);
	}

	public final String getFileName() {
		return this.fileName;
	}

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
