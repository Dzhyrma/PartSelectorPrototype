package com.techsoft.partselector.model;

import java.io.Serializable;

/** The parent class for all parts in the prototype.
 * 
 * @author Andrii Dzhyrma */
public class Part implements Serializable {

	private static final long serialVersionUID = 3148540286211732408L;

	/** @uml.property name="name" */
	public String name;

	/** Creates the part.
	 * 
	 * @param name - name of the part (works as an ID) */
	public Part(String name) {
		this.name = name;
	}

	/** @return name of the current part.
	 * @uml.property name="name" */
	public final String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
