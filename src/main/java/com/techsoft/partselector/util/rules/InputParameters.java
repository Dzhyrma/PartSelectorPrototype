package com.techsoft.partselector.util.rules;

import java.util.Map;

/** Class used to represent additional parameters in rules.
 * 
 * @author Andrii Dzhyrma */
public class InputParameters {
	/** @uml.property name="parameters" */
	private Map<String, Object> parameters;

	/** @return current map with parameters.
	 * @uml.property name="parameters" */
	public final Map<String, Object> getParameters() {
		return this.parameters;
	}

	/** Set new map with new parameters to the current instance.
	 * 
	 * @param parameters - parameters to be updated.
	 * @uml.property name="parameters" */
	public final void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}
