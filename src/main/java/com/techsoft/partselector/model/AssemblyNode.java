package com.techsoft.partselector.model;

/** Represents node of the assembly that contain an object from the part library
 * and reference point that contains coordinates.
 * 
 * @author Andrii Dzhyrma */
public class AssemblyNode {

	/** @uml.property name="part"
	 * @uml.associationEnd multiplicity="(1 1)" */
	private Part part = null;
	/** @uml.property name="ref"
	 * @uml.associationEnd multiplicity="(1 1)" */
	private Reference ref = null;

	/** Creates a new assembly node.
	 * 
	 * @param part - a part of this node.
	 * @param ref - coordinates for the part. */
	public AssemblyNode(Part part, Reference ref) {
		this.part = part;
		this.ref = ref;
	}

	/** @return the current part.
	 * @uml.property name="part" */
	public Part getPart() {
		return this.part;
	}

	/** @return the current coordinates as instance of class Reference.
	 * @uml.property name="ref" */
	public final Reference getRef() {
		return this.ref;
	}

	/** @param part - a part to be inserted.
	 * @uml.property name="part" */
	public void setPart(Part part) {
		this.part = part;
	}

	/** @param ref - coordinates to be changed.
	 * @uml.property name="ref" */
	public final void setRef(Reference ref) {
		this.ref = ref;
	}

	@Override
	public String toString() {
		return "AssemblyNode [ref=" + this.ref + ", part=" + (this.part == null ? null : this.part.getName()) + "]";
	}
}
