package com.models;

import classes.Part;

public class AssemblyNode {
	
	private Part part;
	private Reference ref;
	
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
}
