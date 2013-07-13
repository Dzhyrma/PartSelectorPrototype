package com.techsoft.partselector.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/** Represents combination of parts (e.g., Screw, ScrewNut and Washer).
 * 
 * @author Andrii Dzhyrma */
public class HashVector implements Serializable {

	private static final long serialVersionUID = -285110250839741151L;

	// Prime number that is used to generate a hashcode.
	private int prime = 31;
	private int hashCode = 31;
	/** Vector of parts in the current combination.
	 * 
	 * @uml.property name="parts"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
	 *                     inverse="hashVector:com.techsoft.partselector.model.Part" */
	private Vector<Part> parts = new Vector<Part>();

	/** Adds new part to the current combination and recalculates the hashcode.
	 * 
	 * @param part - part to be added. */
	public void addPart(Part part) {
		if (part == null)
			return;
		this.parts.add(part);
		// sort the current collection of parts by name.
		Collections.sort(this.parts, new Comparator<Part>() {
			@Override
			public int compare(Part o1, Part o2) {
				return ((Part) o1).getName().compareTo(((Part) o2).getName());
			}
		});
		// generate a hashcode for the sorted collection and store it in the field for future lookups
		this.hashCode = 1;
		for (Part element : this.parts) {
			this.hashCode = this.hashCode * this.prime + element.getName().hashCode();
		}
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		HashVector other = (HashVector) obj;
		if (this.parts == other.parts)
			return true;
		if (other.parts == null || this.parts == null || other.parts.size() != this.parts.size())
			return false;
		for (int i = 0; i < other.parts.size(); i++) {
			if (this.parts.get(i).getName().compareTo(other.parts.get(i).getName()) != 0)
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Part part : this.parts)
			if (!first)
				result.append(", ").append(part.getClass().getName()).append(": ").append(part.getName());
			else {
				result.append('[').append(part.getClass().getName()).append(": ").append(part.getName());
				first = false;
			}
		result.append(']');
		return result.toString();
	}

}
