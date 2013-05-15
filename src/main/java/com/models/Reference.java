package com.models;

public class Reference {
	private double x;
	private double y;
	private double z;

	public Reference(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public final double getX() {
		return this.x;
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final double getY() {
		return this.y;
	}

	public final void setY(double y) {
		this.y = y;
	}

	public final double getZ() {
		return this.z;
	}

	public final void setZ(double z) {
		this.z = z;
	}

}
