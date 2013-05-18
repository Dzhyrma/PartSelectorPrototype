package com.techsoft.partselector.model;

public class AssemblyNode {
	
	private Part part;
	private Reference ref;
	
	public AssemblyNode(Part part, Reference ref) {
		this.part = part;
		this.ref = ref;
	}
	
	public final Part getPart() {
		return this.part;
	}
	public final Reference getRef() {
		return this.ref;
	}
	public final void setPart(Part part) {
		this.part = part;
	}
	public final void setRef(Reference ref) {
		this.ref = ref;
	}

	@Override
    public String toString() {
	    return "AssemblyNode [ref=" + this.ref + ", part=" + (this.part == null ? null : this.part.getName()) + "]";
    }
}
