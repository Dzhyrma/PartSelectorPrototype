package com.techsoft.partselector.model;

import java.io.Serializable;

public class Part implements Serializable {

	private static final long serialVersionUID = 3148540286211732408L;

	public String name;

	public Part(String name) {
		this.name = name;
	}

	public final String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
