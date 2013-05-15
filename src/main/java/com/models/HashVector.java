package com.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import classes.Part;

public class HashVector {
	private int prime = 31;
	private int hashCode = 31;
	private Vector<Part> parts;

	public HashVector() {
		this.parts = new Vector<>();
	}

	public void addPart(Part part) {
		if (part == null)
			return;
		this.parts.add(part);
		Collections.sort(this.parts, new Comparator<Part>() {
			@Override
			public int compare(Part o1, Part o2) {
				return ((Part) o1).getName().compareTo(((Part) o2).getName());
			}
		});
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HashVector other = (HashVector) obj;
		return this.hashCode() == other.hashCode();
	}

}
